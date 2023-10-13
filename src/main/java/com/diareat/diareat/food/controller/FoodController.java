package com.diareat.diareat.food.controller;

import com.diareat.diareat.food.domain.Food;
import com.diareat.diareat.food.dto.*;
import com.diareat.diareat.food.service.FoodService;
import com.diareat.diareat.user.service.UserService;
import com.diareat.diareat.util.api.ApiResponse;
import com.diareat.diareat.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Api(tags = "2. food")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/food")
public class FoodController {

    private final FoodService foodService;

    //음식 정보 저장
    @Operation(summary = "[음식] 음식 정보 저장", description = "촬영한 음식 정보를 저장합니다.")
    @PostMapping("/save")
    public ApiResponse<Long> saveFood(CreateFoodDto createFoodDto){}

    //특정 날짜에 먹은 음식 반환
    @Operation(summary = "[음식] 특정 날짜에 먹은 음식 목록 조회",description = "유저의 특정 날짜에 먹은 음식 목록을 조회합니다.")
    @GetMapping("{userId}/{date}")
    public ApiResponse<List<ResponseFoodDto>> getFoodListByDate(@PathVariable Long userId, @RequestParam LocalDate date){}

    //음식 정보 수정
    @Operation(summary = "[음식] 음식 정보 수정",description = "음식에 대한 정보를 수정합니다.")
    @PostMapping("/update")
    public ApiResponse<Void> updateFood(UpdateFoodDto updateFoodDto){}
    //음식 삭제
    @Operation(summary = "[음식] 음식 정보 삭제",description = "음식에 대한 정보를 삭제합니다.")
    @DeleteMapping("{foodId}/delete")
    public ApiResponse<Void> deleteFood(Long foodId){}

    //즐겨찾기에 음식 저장
    @Operation(summary = "[즐겨찾기] 즐겨찾기에 저장",description = "유저의 즐겨찾기에 음식을 저장합니다.")
    @PostMapping("/favorite")
    public ApiResponse<Long> saveFavoriteFood(CreateFavoriteFoodDto createFavoriteFoodDto){}

    //즐겨찾기 음식 리스트 반환
    @Operation(summary = "[즐겨찾기] 즐겨찾기 목록 조회",description = "유저의 즐겨찾기에 등록된 음식 목록을 조회합니다.")
    @GetMapping("/favorite/{userId}")
    public ApiResponse<List<ResponseFavoriteFoodDto>> getFavoriteFoodList(@PathVariable Long userId){}

    //즐겨찾기 음식 수정
    @Operation(summary = "[즐겨찾기] 즐겨찾기 수정",description = "유저의 즐겨찾기에 등록된 음식의 정보를 수정합니다.")
    @PostMapping("/favorite/update")
    public ApiResponse<Void> updateFavoriteFood(UpdateFavoriteFoodDto updateFavoriteFoodDto){}

    //즐겨찾기 음식 해제
    @Operation(summary = "[즐겨찾기] 즐겨찾기 해제",description = "유저의 즐겨찾기에 등록된 음식을 해제합니다.")
    @DeleteMapping("/favorite/{favoriteFoodId}")
    public ApiResponse<Void> deleteFavoriteFood(@PathVariable Long favoriteFoodId){}

    //특정 날짜에 먹은 음식들의 영양성분별 총합 조회
    @Operation(summary = "[음식] 특정 날짜에 먹은 음식들의 영양성분 총합 조회",description = "특정 날짜에 유저가 먹은 음식들의 영양성분별 총합 및 권장섭취량에 대한 비율을 조회합니다.")
    @GetMapping("{userId}/nutrition/{date}")
    public ApiResponse<ResponseNutritionSumByDateDto> getNutritionSumByDate(@PathVariable Long userId, @RequestParam LocalDate date){}

    //"" 7일간 총합 조회
    @Operation(summary = "[음식] 최근 7일간 먹은 음식들의 영양성분 총합 조회",description = "최근 7일 간 유저가 먹은 음식들의 영양성분별 총합 및 권장섭취량에 대한 비율을 조회합니다.")
    @GetMapping("{userId}/nutrition/recentWeek")
    public ApiResponse<ResponseNutritionSumByDateDto> getNutritionSumByWeek(@PathVariable Long userId){}

    //"" 30일간 (1달간) 총합 조회
    @Operation(summary = "[음식] 최근 한달 간 먹은 음식들의 영양성분 총합 조회",description = "최근 한달 간 유저가 먹은 음식들의 영양성분별 총합 및 권장섭취량에 대한 비율을 조회합니다.")
    @GetMapping("{userId}/nutrition/recentMonth")
    public ApiResponse<ResponseNutritionSumByDateDto> getNutritionSumByMonth(@PathVariable Long userId){}

    //7일간의 Best 3 조회
    @Operation(summary = "[음식] 최근 7일 간 먹은 Top3 음식 조회",description = "최근 7일 간 유저가 먹은 음식들 중에서 Top3에 해당한 음식들을 조회합니다.")
    @GetMapping("{userId}/best")
    public ApiResponse<ResponseFoodRankDto> getBestFoodByWeek(@PathVariable Long userId){}

    //7일간의 Worst 3 조회
    @Operation(summary = "[음식] 최근 7일 간 먹은 Worst3 음식 조회",description = "최근 7일 간 유저가 먹은 음식들 중에서 Worst3에 해당한 음식들을 조회합니다.")
    @GetMapping("{userId}/worst")
    public ApiResponse<ResponseFoodRankDto> getWorstFoodByWeek(@PathVariable Long userId){}

}
