package com.diareat.diareat.auth.controller;

import com.diareat.diareat.auth.component.JwtTokenProvider;
import com.diareat.diareat.auth.service.KakaoAuthService;
import com.diareat.diareat.user.dto.CreateUserDto;
import com.diareat.diareat.user.service.UserService;
import com.diareat.diareat.util.api.ApiResponse;
import com.diareat.diareat.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(tags = "2. Auth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final KakaoAuthService kakaoAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    // 카카오 로그인을 위해 회원가입 여부 확인, 이미 회원이면 Jwt 토큰 발급
    @Operation(summary = "[로그인] 카카오로그인 및 토큰 발급", description = "카카오 로그인을 위해 회원가입 여부를 확인하고, 이미 회원이면 Jwt 토큰을 발급합니다.")
    @PostMapping("/login")
    public ApiResponse<HashMap<Long, String>> authCheck(@RequestHeader String accessToken) {
        Long userId = kakaoAuthService.isSignedUp(accessToken); // 유저 고유번호 추출
        HashMap<Long, String> map = new HashMap<>();
        map.put(userId, jwtTokenProvider.createToken(userId.toString()));
        return ApiResponse.success(map, ResponseCode.USER_LOGIN_SUCCESS.getMessage());
    }

    // 회원가입 (성공 시 Jwt 토큰 발급)
    @Operation(summary = "[회원가입] 회원가입 및 토큰 발급", description = "신규 회원가입을 처리하고, 회원가입 성공 시 Jwt 토큰을 발급합니다.")
    @PostMapping("/join")
    public ApiResponse<HashMap<Long, String>> saveUser(CreateUserDto createUserDto) {
        Long userId = userService.saveUser(createUserDto);
        HashMap<Long, String> map = new HashMap<>();
        map.put(userId, jwtTokenProvider.createToken(userId.toString()));
        return ApiResponse.success(map, ResponseCode.USER_CREATE_SUCCESS.getMessage());
    }
}
