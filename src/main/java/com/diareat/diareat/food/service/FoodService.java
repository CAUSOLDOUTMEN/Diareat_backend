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

import java.rmi.registry.LocateRegistry;
import java.time.LocalDate;
import java.util.Comparator;
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
    public ResponseFoodRankDto getBestFoodByWeek(Long userId, LocalDate endDate) {
        List<Food> foodList = foodRepository.findAllByUserIdAndDateBetween(userId, endDate.minusWeeks(1), endDate);

        List<Food> top3Foods = foodList.stream()
                .sorted(Comparator.comparingDouble((Food food) ->
                        0.7 * food.getBaseNutrition().getProtein()- 0.3 * food.getBaseNutrition().getFat()).reversed())
                .limit(3)
                .collect(Collectors.toList()); //고단백 저지방일수록 점수를 높게 측정되도록 기준을 잡은 후, 그 기준을 기반으로 정렬
        //사용한 기준은, 고단백과 저지방의 점수 반영 비율을 7:3으로 측정하고, 단백질량이 높을 수록, 지방량이 낮을 수록 점수가 높음. 이후, 내림차순 정렬
        // ** Best 3 기준 논의 필요 **

        return ResponseFoodRankDto.of(userId, top3Foods, endDate, true);
    }

    @Transactional(readOnly = true)
    // 유저의 최근 7일간의 Worst 3 음식 조회 (dto 구체적 협의 필요)
    public ResponseFoodRankDto getWorstFoodByWeek(Long userId, LocalDate endDate) {

        List<Food> foodList = foodRepository.findAllByUserIdAndDateBetween(userId, endDate.minusWeeks(1), endDate);

        List<Food> worst3Foods = foodList.stream()
                .sorted(Comparator.comparingDouble((Food food) ->
                        0.7 * food.getBaseNutrition().getFat() + 0.3 * food.getBaseNutrition().getCarbohydrate()).reversed())
                .limit(3)
                .collect(Collectors.toList());
        //반대로 고지방 고탄수의 경우를 7:3으로 측정하고, 지방이 높을 수록 점수가 급격히 높아짐. 이 경우는 점수가 높은 것이 안좋음.
        //(수정) https://blog.nongshim.com/1961, 탄수화물이 더 영향을 미친다고 하는데...흠...
        // ** 이점은 논의가 필요할 듯? **
        // 우선 임시로 지방 비율을 높게 설정

        return ResponseFoodRankDto.of(userId, worst3Foods, endDate, false);
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

        double ratioKcal = Math.round((((double) totalKcal /(double) targetUser.getBaseNutrition().getKcal())*100.0)*10.0)/10.0;
        double ratioCarbohydrate = Math.round((((double) totalCarbohydrate /(double) targetUser.getBaseNutrition().getCarbohydrate())*100.0)*10.0)/10.0;
        double ratioProtein = Math.round((((double) totalProtein /(double) targetUser.getBaseNutrition().getProtein())*100.0)*10.0)/10.0;
        double ratioFat =Math.round((((double) totalFat /(double) targetUser.getBaseNutrition().getFat())*100.0)*10.0)/10.0;

        return ResponseNutritionSumByDateDto.of(totalKcal,totalCarbohydrate, totalProtein, totalFat, ratioKcal, ratioCarbohydrate, ratioProtein, ratioFat);
    }


    /**
     * 메서드 구현 유의사항
     * 1. 메서드명은 동사로 시작
     * 2. 유효성 검사는 private void validateUser의 형태로 분리
     * 3. Repository에서 가져오고 OrElseThrow로 예외처리하는 부분이 반복되는 경우 private ??? get???ById 형태 메서드로 분리
     */
}
