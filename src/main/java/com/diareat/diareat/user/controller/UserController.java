package com.diareat.diareat.user.controller;

import com.diareat.diareat.auth.component.JwtTokenProvider;
import com.diareat.diareat.user.dto.request.SearchUserDto;
import com.diareat.diareat.user.dto.request.UpdateUserDto;
import com.diareat.diareat.user.dto.request.UpdateUserNutritionDto;
import com.diareat.diareat.user.dto.response.ResponseSearchUserDto;
import com.diareat.diareat.user.dto.response.ResponseSimpleUserDto;
import com.diareat.diareat.user.dto.response.ResponseUserDto;
import com.diareat.diareat.user.dto.response.ResponseUserNutritionDto;
import com.diareat.diareat.user.service.UserService;
import com.diareat.diareat.util.api.ApiResponse;
import com.diareat.diareat.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "1. User")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원 기본정보 조회
    @Operation(summary = "[프로필] 회원 기본정보 조회", description = "회원 기본정보를 조회합니다.")
    @GetMapping("/info/simple")
    public ApiResponse<ResponseSimpleUserDto> getSimpleUserInfo(@RequestHeader String accessToken) {
        Long userId = jwtTokenProvider.getUserPk(accessToken);
        return ApiResponse.success(userService.getSimpleUserInfo(userId), ResponseCode.USER_CREATE_SUCCESS.getMessage());
    }

    // 회원정보 조회
    @Operation(summary = "[프로필] 회원 정보 조회", description = "회원 정보를 조회합니다.")
    @GetMapping("/info")
    public ApiResponse<ResponseUserDto> getUserInfo(@RequestHeader String accessToken) {
        Long userId = jwtTokenProvider.getUserPk(accessToken);
        return ApiResponse.success(userService.getUserInfo(userId), ResponseCode.USER_CREATE_SUCCESS.getMessage());
    }

    // 회원정보 수정
    @Operation(summary = "[프로필] 회원 정보 수정", description = "회원 정보를 수정합니다.")
    @PutMapping("/update")
    public ApiResponse<Void> updateUserInfo(@RequestBody @Valid UpdateUserDto updateUserDto) {
        userService.updateUserInfo(updateUserDto);
        return ApiResponse.success(null, ResponseCode.USER_UPDATE_SUCCESS.getMessage());
    }

    // 회원 기준섭취량 조회
    @Operation(summary = "[프로필] 회원 기준섭취량 조회", description = "회원 기준섭취량을 조회합니다.")
    @GetMapping("/info/nutrition")
    public ApiResponse<ResponseUserNutritionDto> getUserNutrition(@RequestHeader String accessToken) {
        Long userId = jwtTokenProvider.getUserPk(accessToken);
        return ApiResponse.success(userService.getUserNutrition(userId), ResponseCode.USER_READ_SUCCESS.getMessage());
    }

    // 회원 기준섭취량 직접 수정
    @Operation(summary = "[프로필] 회원 기준섭취량 직접 수정", description = "회원 기준섭취량을 직접 수정합니다.")
    @PutMapping("/info/nutrition")
    public ApiResponse<Void> updateUserNutrition(@RequestBody @Valid UpdateUserNutritionDto updateUserNutritionDto) {
        userService.updateBaseNutrition(updateUserNutritionDto);
        return ApiResponse.success(null, ResponseCode.USER_UPDATE_SUCCESS.getMessage());
    }

    // 회원의 친구 검색 결과 조회
    @Operation(summary = "[주간 랭킹] 회원의 친구 검색 결과 조회", description = "회원의 친구 검색 결과를 조회합니다.")
    @PostMapping("/search")
    public ApiResponse<List<ResponseSearchUserDto>> searchUser(@RequestBody @Valid SearchUserDto searchUserDto) {
        return ApiResponse.success(userService.searchUser(searchUserDto.getUserId(), searchUserDto.getInputName()), ResponseCode.USER_SEARCH_SUCCESS.getMessage());
    }

    // 회원이 특정 회원 팔로우
    @Operation(summary = "[주간 랭킹] 회원이 특정 회원 팔로우", description = "회원이 특정 회원을 팔로우합니다.")
    @PostMapping("/follow/{toUserId}")
    public ApiResponse<Void> followUser(@RequestHeader String accessToken, @PathVariable Long toUserId) {
        Long userId = jwtTokenProvider.getUserPk(accessToken);
        userService.followUser(userId, toUserId);
        return ApiResponse.success(null, ResponseCode.USER_FOLLOW_SUCCESS.getMessage());
    }

    // 회원이 특정 회원 팔로우 취소
    @Operation(summary = "[주간 랭킹] 회원이 특정 회원 팔로우 취소", description = "회원이 특정 회원을 팔로우 취소합니다.")
    @DeleteMapping("/follow/{toUserId}")
    public ApiResponse<Void> unfollowUser(@RequestHeader String accessToken, @PathVariable Long toUserId) {
        Long userId = jwtTokenProvider.getUserPk(accessToken);
        userService.unfollowUser(userId, toUserId);
        return ApiResponse.success(null, ResponseCode.USER_UNFOLLOW_SUCCESS.getMessage());
    }
}
