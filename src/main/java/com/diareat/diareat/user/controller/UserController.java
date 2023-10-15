package com.diareat.diareat.user.controller;

import com.diareat.diareat.auth.KakaoAuthService;
import com.diareat.diareat.user.dto.*;
import com.diareat.diareat.user.service.UserService;
import com.diareat.diareat.util.api.ApiResponse;
import com.diareat.diareat.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "1. User")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final KakaoAuthService kakaoAuthService;

    // 카카오 로그인을 위해 회원가입 여부 확인
    @Operation(summary = "[로그인] 카카오 로그인을 위해 회원가입 여부 확인", description = "카카오 로그인을 위해 회원가입 여부를 확인합니다.")
    @GetMapping("/auth")
    public ApiResponse<Long> authCheck(@RequestParam String token) {
        return ApiResponse.success(kakaoAuthService.isSignedUp(token), ResponseCode.USER_READ_SUCCESS.getMessage());
    }

    // 회원정보 저장
    @Operation(summary = "[회원가입] 회원정보 저장", description = "신규 회원정보를 저장합니다.")
    @PostMapping("/save")
    public ApiResponse<Long> saveUser(CreateUserDto createUserDto) {
        return ApiResponse.success(userService.saveUser(createUserDto), ResponseCode.USER_CREATE_SUCCESS.getMessage());
    }

    // 회원 기본정보 조회
    @Operation(summary = "[프로필] 회원 기본정보 조회", description = "회원 기본정보를 조회합니다.")
    @GetMapping("{userId}/info/simple")
    public ApiResponse<ResponseSimpleUserDto> getSimpleUserInfo(@PathVariable Long userId) {
        return ApiResponse.success(userService.getSimpleUserInfo(userId), ResponseCode.USER_CREATE_SUCCESS.getMessage());
    }

    // 회원정보 조회
    @Operation(summary = "[프로필] 회원 정보 조회", description = "회원 정보를 조회합니다.")
    @GetMapping("{userId}/info")
    public ApiResponse<ResponseUserDto> getUserInfo(@PathVariable Long userId) {
        return ApiResponse.success(userService.getUserInfo(userId), ResponseCode.USER_CREATE_SUCCESS.getMessage());
    }

    // 회원정보 수정
    @Operation(summary = "[프로필] 회원 정보 수정", description = "회원 정보를 수정합니다.")
    @PutMapping("/update")
    public ApiResponse<Void> updateUserInfo(UpdateUserDto updateUserDto) {
        userService.updateUserInfo(updateUserDto);
        return ApiResponse.success(null, ResponseCode.USER_UPDATE_SUCCESS.getMessage());
    }

    // 회원 기준섭취량 조회
    @Operation(summary = "[프로필] 회원 기준섭취량 조회", description = "회원 기준섭취량을 조회합니다.")
    @GetMapping("{userId}/nutrition")
    public ApiResponse<ResponseUserNutritionDto> getUserNutrition(@PathVariable Long userId) {
        return ApiResponse.success(userService.getUserNutrition(userId), ResponseCode.USER_READ_SUCCESS.getMessage());
    }

    // 회원 기준섭취량 직접 수정
    @Operation(summary = "[프로필] 회원 기준섭취량 직접 수정", description = "회원 기준섭취량을 직접 수정합니다.")
    @PutMapping("{userId}/nutrition")
    public ApiResponse<Void> updateUserNutrition(UpdateUserNutritionDto updateUserNutritionDto) {
        userService.updateBaseNutrition(updateUserNutritionDto);
        return ApiResponse.success(null, ResponseCode.USER_UPDATE_SUCCESS.getMessage());
    }

    // 회원의 친구 검색 결과 조회
    @Operation(summary = "[주간 랭킹] 회원의 친구 검색 결과 조회", description = "회원의 친구 검색 결과를 조회합니다.")
    @GetMapping("{userId}/search/{name}")
    public ApiResponse<List<ResponseSearchUserDto>> searchUser(@PathVariable Long userId, @RequestParam String name) {
        return ApiResponse.success(userService.searchUser(userId, name), ResponseCode.USER_SEARCH_SUCCESS.getMessage());
    }

    // 실제 팔로잉 유저들의 점수 계산하여 랭킹 형태로 반환하는 API: FoodService에서 계산할지 UserService에서 FoodRepository를 콜해서 처리할지 협의 필요

    // 회원이 특정 회원 팔로우
    @Operation(summary = "[주간 랭킹] 회원이 특정 회원 팔로우", description = "회원이 특정 회원을 팔로우합니다.")
    @PostMapping("{userId}/follow/{followId}")
    public ApiResponse<Void> followUser(@PathVariable Long userId, @PathVariable Long followId) {
        userService.followUser(userId, followId);
        return ApiResponse.success(null, ResponseCode.USER_UPDATE_SUCCESS.getMessage());
    }

    // 회원이 특정 회원 팔로우 취소
    @Operation(summary = "[주간 랭킹] 회원이 특정 회원 팔로우 취소", description = "회원이 특정 회원을 팔로우 취소합니다.")
    @DeleteMapping("{userId}/follow/{followId}")
    public ApiResponse<Void> unfollowUser(@PathVariable Long userId, @PathVariable Long followId) {
        userService.unfollowUser(userId, followId);
        return ApiResponse.success(null, ResponseCode.USER_UPDATE_SUCCESS.getMessage());
    }
}
