package com.diareat.diareat.food.service;

import com.diareat.diareat.food.domain.FavoriteFood;
import com.diareat.diareat.food.domain.Food;
import com.diareat.diareat.food.dto.*;
import com.diareat.diareat.food.repository.FavoriteFoodRepository;
import com.diareat.diareat.food.repository.FoodRepository;
import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.domain.User;
import com.diareat.diareat.user.repository.UserRepository;
import com.diareat.diareat.util.api.ResponseCode;
import com.diareat.diareat.util.exception.FoodException;
import com.diareat.diareat.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FoodService {

    private final FoodRepository foodRepository; // 유저:음식은 1:다
    private final FavoriteFoodRepository favoriteFoodRepository; // 유저:즐찾음식은 1:다

    private final UserRepository userRepository;

    // 촬영 후, 음식 정보 저장
    @Transactional
    public Long saveFood(CreateFoodDto createFoodDto) {
        User user = getUserById(createFoodDto.getUserId());
        Food food = Food.createFood(createFoodDto.getName(), user, createFoodDto.getBaseNutrition());
        return foodRepository.save(food).getId();
    }

    // 회원이 특정 날짜에 먹은 음식 반환
    @Transactional(readOnly = true)
    public List<ResponseFoodDto> getFoodListByDate(Long userId, LocalDate date){
        List<Food> foodList = foodRepository.findAllByUserIdAndDate(userId, date);
        return foodList.stream()
                .map(food -> ResponseFoodDto.of(food.getId(), food.getUser().getId(), food.getName(), food.getDate(), food.getTime(), food.getBaseNutrition(), food.isFavorite())).collect(Collectors.toList());
    }


    // 음식 정보 수정
    @Transactional
    public void updateFood(UpdateFoodDto updateFoodDto) {
        Food food = getFoodById(updateFoodDto.getFoodId());
        food.updateFood(updateFoodDto.getName(), updateFoodDto.getBaseNutrition());
    }

    // 음식 삭제
    @Transactional
    public void deleteFood(Long foodId) {
        foodRepository.deleteById(foodId);
    }

    // 즐겨찾기에 음식 저장
    @Transactional
    public Long saveFavoriteFood(CreateFavoriteFoodDto createFavoriteFoodDto) {

        User user = getUserById(createFavoriteFoodDto.getUserId());

        FavoriteFood favoriteFood = FavoriteFood.createFavoriteFood(createFavoriteFoodDto.getName(), user, createFavoriteFoodDto.getBaseNutrition());
        return favoriteFoodRepository.save(favoriteFood).getId();
    }

    //즐겨찾기 음식 리스트 반환
    @Transactional(readOnly = true)
    public List<ResponseFavoriteFoodDto> getFavoriteFoodList(Long userId){
        List<FavoriteFood> foodList = favoriteFoodRepository.findAllByUserId(userId);
        return foodList.stream()
                .map(favoriteFood -> ResponseFavoriteFoodDto.of(favoriteFood.getId(), favoriteFood.getName(),
                        favoriteFood.getBaseNutrition(), favoriteFood.getCount())).collect(Collectors.toList());
    }

    // 즐겨찾기 음식 수정
    @Transactional
    public void updateFavoriteFood(UpdateFavoriteFoodDto updateFavoriteFoodDto) {
        FavoriteFood food = getFavoriteFoodById(updateFavoriteFoodDto.getFavoriteFoodId());
        food.updateFavoriteFood(updateFavoriteFoodDto.getName(), updateFavoriteFoodDto.getBaseNutrition());
    }

    // 즐겨찾기 해제
    @Transactional
    public void deleteFavoriteFood(Long favoriteFoodId) {
        favoriteFoodRepository.deleteById(favoriteFoodId);
    }

    @Transactional(readOnly = true)
    // 유저의 특정 날짜에 먹은 음식들의 영양성분별 총합 조회 (섭취영양소/기준영양소 및 비율까지 계산해서 반환, dto 구체적 협의 필요)
    public ResponseNutritionSumByDateDto getNutritionSumByDate(Long userId, LocalDate date) {
        List<Food> foodList = foodRepository.findAllByUserIdAndDate(userId, date);
        return calculateNutritionSumAndRatio(userId, foodList);
    }

    @Transactional(readOnly = true)
    // 유저의 최근 7일간의 영양성분별 총합 조회 (섭취영양소/기준영양소 및 비율까지 계산해서 반환, dto 구체적 협의 필요)
    public ResponseNutritionSumByDateDto getNutritionSumByWeek(Long userId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        List<Food> foodList = foodRepository.findAllByUserIdAndDateBetween(userId, startDate, endDate);

        return calculateNutritionSumAndRatio(userId, foodList);
    }

    @Transactional(readOnly = true)
    // 유저의 최근 1개월간의 영양성분별 총합 조회 (섭취영양소/기준영양소 및 비율까지 계산해서 반환, dto 구체적 협의 필요)
    public ResponseNutritionSumByDateDto getNutritionSumByMonth(Long userId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);
        List<Food> foodList = foodRepository.findAllByUserIdAndDateBetween(userId, startDate, endDate);

        return calculateNutritionSumAndRatio(userId, foodList);
    }

    @Transactional(readOnly = true)
    // 유저의 최근 7일간의 Best 3 음식 조회 (dto 구체적 협의 필요)
    public void getBestFoodByWeek(Long userId) {

    }

    @Transactional(readOnly = true)
    // 유저의 최근 7일간의 Worst 3 음식 조회 (dto 구체적 협의 필요)
    public void getWorstFoodByWeek(Long userId) {

    }

    private User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }

    private Food getFoodById(Long foodId){
        return foodRepository.findById(foodId)
                .orElseThrow(() -> new FoodException(ResponseCode.FOOD_NOT_FOUND));
    }

    private FavoriteFood getFavoriteFoodById(Long foodId){
        return favoriteFoodRepository.findById(foodId)
                .orElseThrow(() -> new FoodException(ResponseCode.FOOD_NOT_FOUND));
    }

    private ResponseNutritionSumByDateDto calculateNutritionSumAndRatio(Long userId, List<Food> foodList){
        User targetUser = getUserById(userId);
        int totalKcal = 0;
        int totalCarbohydrate = 0;
        int totalProtein = 0;
        int totalFat = 0;

        for (Food food : foodList) {
            BaseNutrition targetFoodNutrition = food.getBaseNutrition();
            totalKcal += targetFoodNutrition.getKcal();
            totalCarbohydrate += targetFoodNutrition.getCarbohydrate();
            totalProtein += targetFoodNutrition.getProtein();
            totalFat += targetFoodNutrition.getFat();
        }

        double ratioKcal = Math.round((totalKcal*1.0)/(targetUser.getBaseNutrition().getKcal()*1.0))*10.0;
        double ratioCarbohydrate = Math.round((totalCarbohydrate*1.0)/(targetUser.getBaseNutrition().getCarbohydrate()*1.0))*10.0;
        double ratioProtein = Math.round((totalProtein*1.0)/(targetUser.getBaseNutrition().getProtein()*1.0))*10.0;
        double ratioFat = Math.round((totalFat*1.0)/(targetUser.getBaseNutrition().getFat()*1.0))*10.0;

        return ResponseNutritionSumByDateDto.of(totalKcal,totalCarbohydrate, totalProtein, totalFat, ratioKcal, ratioCarbohydrate, ratioProtein, ratioFat);
    }


    /**
     * 메서드 구현 유의사항
     * 1. 메서드명은 동사로 시작
     * 2. 유효성 검사는 private void validateUser의 형태로 분리
     * 3. Repository에서 가져오고 OrElseThrow로 예외처리하는 부분이 반복되는 경우 private ??? get???ById 형태 메서드로 분리
     */
}
