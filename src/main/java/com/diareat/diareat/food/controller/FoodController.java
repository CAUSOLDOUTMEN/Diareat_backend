package com.diareat.diareat.food.controller;

import com.diareat.diareat.food.dto.*;
import com.diareat.diareat.food.service.FoodService;
import com.diareat.diareat.user.dto.response.ResponseRankUserDto;
import com.diareat.diareat.util.api.ApiResponse;
import com.diareat.diareat.util.api.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ApiResponse<Long> saveFood(@RequestBody @Valid CreateFoodDto createFoodDto){
        return ApiResponse.success(foodService.saveFood(createFoodDto),ResponseCode.FOOD_CREATE_SUCCESS.getMessage());
    }

    //특정 날짜에 먹은 음식 반환
    @Operation(summary = "[음식] 특정 날짜에 먹은 음식 목록 조회",description = "유저의 특정 날짜에 먹은 음식 목록을 조회합니다.")
    @GetMapping("/{userId}")
    public ApiResponse<List<ResponseFoodDto>> getFoodListByDate(@PathVariable Long userId,
                                                                @RequestParam int yy,
                                                                @RequestParam int mm,
                                                                @RequestParam int dd)
    {
        LocalDate date = LocalDate.of(yy,mm,dd);
        return ApiResponse.success(foodService.getFoodListByDate(userId,date),ResponseCode.FOOD_READ_SUCCESS.getMessage());
    }

    //음식 정보 수정
    @Operation(summary = "[음식] 음식 정보 수정",description = "음식에 대한 정보를 수정합니다.")
    @PostMapping("/update")
    public ApiResponse<Void> updateFood(@RequestBody @Valid UpdateFoodDto updateFoodDto,
                                        @RequestParam int yy,
                                        @RequestParam int mm,
                                        @RequestParam int dd){
        LocalDate date = LocalDate.of(yy,mm,dd);
        foodService.updateFood(updateFoodDto, date);
        return ApiResponse.success(null,ResponseCode.FOOD_UPDATE_SUCCESS.getMessage());
    }
    //음식 삭제
    @Operation(summary = "[음식] 음식 정보 삭제",description = "음식에 대한 정보를 삭제합니다.")
    @DeleteMapping("/{foodId}/delete")
    public ApiResponse<Void> deleteFood(@PathVariable Long foodId, @RequestHeader Long userId,
                                        @RequestParam int yy,
                                        @RequestParam int mm,
                                        @RequestParam int dd){
        LocalDate date = LocalDate.of(yy,mm,dd);
        foodService.deleteFood(foodId, userId, date);
        return ApiResponse.success(null, ResponseCode.FOOD_DELETE_SUCCESS.getMessage());
    }

    //즐겨찾기에 음식 저장
    @Operation(summary = "[즐겨찾기] 즐겨찾기에 저장",description = "유저의 즐겨찾기에 음식을 저장합니다.")
    @PostMapping("/favorite")
    public ApiResponse<Long> saveFavoriteFood(@RequestBody @Valid CreateFavoriteFoodDto createFavoriteFoodDto){
        return ApiResponse.success(foodService.saveFavoriteFood(createFavoriteFoodDto),ResponseCode.FOOD_FAVORITE_CREATE_SUCCESS.getMessage());
    }

    //즐겨찾기 음식 리스트 반환
    @Operation(summary = "[즐겨찾기] 즐겨찾기 목록 조회",description = "유저의 즐겨찾기에 등록된 음식 목록을 조회합니다.")
    @GetMapping("/favorite/{userId}")
    public ApiResponse<List<ResponseFavoriteFoodDto>> getFavoriteFoodList(@PathVariable Long userId){
        return ApiResponse.success(foodService.getFavoriteFoodList(userId), ResponseCode.FOOD_FAVORITE_READ_SUCCESS.getMessage());
    }

    //즐겨찾기 음식 수정
    @Operation(summary = "[즐겨찾기] 즐겨찾기 수정",description = "유저의 즐겨찾기에 등록된 음식의 정보를 수정합니다.")
    @PostMapping("/favorite/update")
    public ApiResponse<Void> updateFavoriteFood(@RequestBody @Valid UpdateFavoriteFoodDto updateFavoriteFoodDto){
        foodService.updateFavoriteFood(updateFavoriteFoodDto);
        return ApiResponse.success(null, ResponseCode.FOOD_FAVORITE_UPDATE_SUCCESS.getMessage());
    }

    //즐겨찾기 음식 해제
    @Operation(summary = "[즐겨찾기] 즐겨찾기 해제",description = "유저의 즐겨찾기에 등록된 음식을 해제합니다.")
    @DeleteMapping("/favorite/{favoriteFoodId}")
    public ApiResponse<Void> deleteFavoriteFood(@PathVariable Long favoriteFoodId, @RequestHeader Long userId){
        foodService.deleteFavoriteFood(favoriteFoodId, userId);
        return ApiResponse.success(null, ResponseCode.FOOD_FAVORITE_DELETE_SUCCESS.getMessage());
    }

    //특정 날짜에 먹은 음식들의 영양성분별 총합 조회
    @Operation(summary = "[음식] 특정 날짜에 먹은 음식들의 영양성분 총합 조회",description = "특정 날짜에 유저가 먹은 음식들의 영양성분별 총합 및 권장섭취량에 대한 비율을 조회합니다.")
    @GetMapping("/{userId}/nutrition")
    public ApiResponse<ResponseNutritionSumByDateDto> getNutritionSumByDate(@PathVariable Long userId,
                                                                            @RequestParam int yy,
                                                                            @RequestParam int mm,
                                                                            @RequestParam int dd){
        LocalDate date = LocalDate.of(yy,mm,dd);
        return ApiResponse.success(foodService.getNutritionSumByDate(userId,date),ResponseCode.FOOD_READ_SUCCESS.getMessage());
    }

    //"" 7일간 총합 조회
    @Operation(summary = "[음식] 최근 7일간 먹은 음식들의 영양성분 총합 조회",description = "최근 7일 간 유저가 먹은 음식들의 영양성분별 총합 및 권장섭취량에 대한 비율을 조회합니다.")
    @GetMapping("/{userId}/nutrition/recentWeek")
    public ApiResponse<ResponseNutritionSumByDateDto> getNutritionSumByWeek(@PathVariable Long userId,
                                                                            @RequestParam int yy,
                                                                            @RequestParam int mm,
                                                                            @RequestParam int dd){
        return ApiResponse.success(foodService.getNutritionSumByWeek(userId, yy, mm, dd),ResponseCode.FOOD_READ_SUCCESS.getMessage());
    }

    //"" 30일간 (1달간) 총합 조회
    @Operation(summary = "[음식] 최근 한달 간 먹은 음식들의 영양성분 총합 조회",description = "최근 한달 간 유저가 먹은 음식들의 영양성분별 총합 및 권장섭취량에 대한 비율을 조회합니다.")
    @GetMapping("/{userId}/nutrition/recentMonth")
    public ApiResponse<ResponseNutritionSumByDateDto> getNutritionSumByMonth(@PathVariable Long userId,
                                                                             @RequestParam int yy,
                                                                             @RequestParam int mm,
                                                                             @RequestParam int dd){
        return ApiResponse.success(foodService.getNutritionSumByMonth(userId, yy, mm, dd),ResponseCode.FOOD_READ_SUCCESS.getMessage());

    }

    //유저의 주간 식습관 점수와 best3, worst3 음식 조회
    @Operation(summary = "[음식] 유저의 주간 식습관 점수와 best3, worst3 음식 조회",description = "유저의 주간 식습관 점수와 best3, worst3 음식을 조회합니다.")
    @GetMapping("/{userId}/score")
    public ApiResponse<ResponseScoreBestWorstDto> getScoreOfUserWithBestAndWorstFoods(@PathVariable Long userId,
                                                                                      @RequestParam int yy,
                                                                                      @RequestParam int mm,
                                                                                      @RequestParam int dd
    ){
        return ApiResponse.success(foodService.getScoreOfUserWithBestAndWorstFoods(userId, yy, mm, dd),ResponseCode.FOOD_READ_SUCCESS.getMessage());
    }

    //유저의 일기 분석 그래프 데이터 및 식습관 totalScore 조회
    @Operation(summary = "[음식] 유저의 일기 분석 그래프 데이터 및 주간 식습관 점수 조회",description = "유저의 일기 분석 그래프 데이터 및 식습관 점수를 조회합니다.")
    @GetMapping("/{userId}/analysis")
    public ApiResponse<ResponseAnalysisDto> getAnalysisOfUser(@PathVariable Long userId,
                                                              @RequestParam int yy,
                                                              @RequestParam int mm,
                                                              @RequestParam int dd){
        return ApiResponse.success(foodService.getAnalysisOfUser(userId, yy, mm, dd),ResponseCode.FOOD_READ_SUCCESS.getMessage());
    }

    //유저의 식습관 점수를 기반으로 한 주간 랭킹 조회
    @Operation(summary = "[음식] 유저의 식습관 점수를 기반으로 한 주간 랭킹 조회",description = "유저의 식습관 점수를 기반으로 한 주간 랭킹을 조회합니다. (팔로잉 상대 기반)")
    @GetMapping("/{userId}/rank")
    public ApiResponse<List<ResponseRankUserDto>> getUserRankByWeek(@PathVariable Long userId,
                                                                    @RequestParam int yy,
                                                                    @RequestParam int mm,
                                                                    @RequestParam int dd){
        return ApiResponse.success(foodService.getUserRankByWeek(userId, yy, mm, dd),ResponseCode.FOOD_RANK_READ_SUCCESS.getMessage());
    }

    @Operation(summary = "[음식] 즐겨찾기 음식으로 음식 생성",description = "즐겨찾기 음식으로 음식을 생성합니다.")
    @PostMapping("/favorite/createfrom")
    public ApiResponse<Long> createFoodFromFavoriteFood(@RequestBody @Valid CreateFoodFromFavoriteFoodDto createFoodFromFavoriteFoodDto){
        return ApiResponse.success(foodService.createFoodFromFavoriteFood(createFoodFromFavoriteFoodDto),ResponseCode.FOOD_CREATE_SUCCESS.getMessage());
    }
}
