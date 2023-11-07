package com.diareat.diareat.auth.controller;

import com.diareat.diareat.auth.component.JwtTokenProvider;
import com.diareat.diareat.auth.dto.ResponseJwtDto;
import com.diareat.diareat.auth.service.KakaoAuthService;
import com.diareat.diareat.user.dto.request.JoinUserDto;
import com.diareat.diareat.user.service.UserService;
import com.diareat.diareat.util.api.ApiResponse;
import com.diareat.diareat.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "2. Auth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final KakaoAuthService kakaoAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    // 카카오 로그인을 위해 회원가입 여부 확인, 이미 회원이면 Jwt 토큰 발급
    @Operation(summary = "[로그인] 카카오로그인 및 토큰 발급", description = "카카오 로그인을 위해 회원가입 여부를 확인하고, 이미 회원이면 id와 Jwt 토큰을 발급합니다.")
    @PostMapping("/login")
    public ApiResponse<ResponseJwtDto> authCheck(@RequestHeader String accessToken) {
        Long userId = kakaoAuthService.isSignedUp(accessToken); // 유저 고유번호 추출
        String jwt = (userId == null) ? null : jwtTokenProvider.createToken(userId.toString()); // 고유번호가 null이 아니라면 Jwt 토큰 발급
        return ApiResponse.success(ResponseJwtDto.of(userId, jwt), ResponseCode.USER_LOGIN_SUCCESS.getMessage());
    }

    // 회원가입 (성공 시 Jwt 토큰 발급)
    @Operation(summary = "[회원가입] 회원가입 및 토큰 발급", description = "신규 회원가입을 처리하고, 회원가입 성공 시 id와 Jwt 토큰을 발급합니다.")
    @PostMapping("/join")
    public ApiResponse<ResponseJwtDto> saveUser(@Valid @RequestBody JoinUserDto joinUserDto) {
        Long userId = userService.saveUser(kakaoAuthService.createUserDto(joinUserDto));
        String jwt = jwtTokenProvider.createToken(userId.toString());
        return ApiResponse.success(ResponseJwtDto.of(userId, jwt), ResponseCode.USER_CREATE_SUCCESS.getMessage());
    }

    // 토큰 검증 (Jwt 토큰을 서버에 전송하여, 서버가 유효한 토큰인지 확인하고 True 혹은 예외 반환)
    @Operation(summary = "[토큰 검증] 토큰 검증", description = "클라이언트가 가지고 있던 Jwt 토큰을 서버에 전송하여, 서버가 유효한 토큰인지 확인하고 OK 혹은 예외를 반환합니다.")
    @GetMapping("/token")
    public ApiResponse<Boolean> tokenCheck(@RequestHeader String jwtToken) {
        return ApiResponse.success(jwtTokenProvider.validateToken(jwtToken), ResponseCode.TOKEN_CHECK_SUCCESS.getMessage());
    }
}
