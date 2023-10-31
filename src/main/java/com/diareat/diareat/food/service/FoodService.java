package com.diareat.diareat.food.service;

import com.diareat.diareat.food.domain.FavoriteFood;
import com.diareat.diareat.food.domain.Food;
import com.diareat.diareat.food.dto.*;
import com.diareat.diareat.food.repository.FavoriteFoodRepository;
import com.diareat.diareat.food.repository.FoodRepository;
import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.domain.User;
import com.diareat.diareat.user.dto.ResponseRankUserDto;
import com.diareat.diareat.user.repository.FollowRepository;
import com.diareat.diareat.user.repository.UserRepository;
import com.diareat.diareat.util.api.ResponseCode;
import com.diareat.diareat.util.exception.FavoriteException;
import com.diareat.diareat.util.exception.FoodException;
import com.diareat.diareat.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FoodService {

    private final FoodRepository foodRepository; // 유저:음식은 1:다
    private final FavoriteFoodRepository favoriteFoodRepository; // 유저:즐찾음식은 1:다
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 촬영 후, 음식 정보 저장
    @CacheEvict(value = "ResponseFoodDto", key = "#createFoodDto.getUserId()+#createFoodDto.getDate()", cacheManager = "diareatCacheManager")
    @Transactional
    public Long saveFood(CreateFoodDto createFoodDto) {
        User user = getUserById(createFoodDto.getUserId());
        Food food = Food.createFood(createFoodDto.getName(), user, createFoodDto.getBaseNutrition());
        return foodRepository.save(food).getId();
    }

    // 회원이 특정 날짜에 먹은 음식 조회
    @Cacheable(value = "ResponseFoodDto", key = "#userId+#date", cacheManager = "diareatCacheManager")
    @Transactional(readOnly = true)
    public List<ResponseFoodDto> getFoodListByDate(Long userId, LocalDate date){
        validateUser(userId);
        List<Food> foodList = foodRepository.findAllByUserIdAndDate(userId, date);
        return foodList.stream()
                .map(food -> ResponseFoodDto.of(food.getId(), food.getUser().getId(), food.getName(), food.getDate(), food.getTime(), food.getBaseNutrition(), food.isFavorite())).collect(Collectors.toList());
    }

    // 음식 정보 수정
    @CacheEvict(value = "ResponseFoodDto", key = "#updateFoodDto.getUserId()", cacheManager = "diareatCacheManager")
    @Transactional
    public void updateFood(UpdateFoodDto updateFoodDto) {
        Food food = getFoodById(updateFoodDto.getFoodId());
        food.updateFood(updateFoodDto.getName(), updateFoodDto.getBaseNutrition());
        foodRepository.save(food);
    }

    // 음식 삭제
    @CacheEvict(value = "ResponseFoodDto", key = "#userId", cacheManager = "diareatCacheManager")
    @Transactional
    public void deleteFood(Long foodId, Long userId) {
        validateFood(foodId, userId);
        foodRepository.deleteById(foodId);
    }

    // 즐겨찾기에 음식 저장
    @CacheEvict(value = "ResponseFavoriteFoodDto", key = "#createFavoriteFoodDto.getUserId()", cacheManager = "diareatCacheManager")
    @Transactional
    public Long saveFavoriteFood(CreateFavoriteFoodDto createFavoriteFoodDto) {
        User user = getUserById(createFavoriteFoodDto.getUserId());
        if (favoriteFoodRepository.existsByFoodId(createFavoriteFoodDto.getFoodId()))
            throw new FavoriteException(ResponseCode.FAVORITE_ALREADY_EXIST);
        FavoriteFood favoriteFood = FavoriteFood.createFavoriteFood(createFavoriteFoodDto.getName(), user, createFavoriteFoodDto.getBaseNutrition());
        return favoriteFoodRepository.save(favoriteFood).getId();
    }

    //즐겨찾기 음식 리스트 반환
    @Cacheable(value = "ResponseFavoriteFoodDto", key = "#userId", cacheManager = "diareatCacheManager")
    @Transactional(readOnly = true)
    public List<ResponseFavoriteFoodDto> getFavoriteFoodList(Long userId){
        validateUser(userId);
        List<FavoriteFood> foodList = favoriteFoodRepository.findAllByUserId(userId);
        return foodList.stream()
                .map(favoriteFood -> ResponseFavoriteFoodDto.of(favoriteFood.getId(), favoriteFood.getName(),
                        favoriteFood.getBaseNutrition(), favoriteFood.getCount())).collect(Collectors.toList());
    }

    // 즐겨찾기 음식 수정
    @CacheEvict(value = "ResponseFavoriteFoodDto", key = "updateFavoriteFoodDto.getUserId()", cacheManager = "diareatCacheManager")
    @Transactional
    public void updateFavoriteFood(UpdateFavoriteFoodDto updateFavoriteFoodDto) {
        FavoriteFood food = getFavoriteFoodById(updateFavoriteFoodDto.getFavoriteFoodId());
        food.updateFavoriteFood(updateFavoriteFoodDto.getName(), updateFavoriteFoodDto.getBaseNutrition());
        favoriteFoodRepository.save(food);
    }

    // 즐겨찾기 해제
    @CacheEvict(value = "ResponseFavoriteFoodDto", key = "#userId", cacheManager = "diareatCacheManager")
    @Transactional
    public void deleteFavoriteFood(Long favoriteFoodId, Long userId) {
        validateFavoriteFood(favoriteFoodId, userId);
        favoriteFoodRepository.deleteById(favoriteFoodId);
    }

    @Cacheable(value = "ResponseNutritionSumByDateDto", key = "#userId+#date", cacheManager = "diareatCacheManager")
    @Transactional(readOnly = true)
    // 유저의 특정 날짜에 먹은 음식들의 영양성분별 총합 조회 (섭취영양소/기준영양소 및 비율까지 계산해서 반환, dto 구체적 협의 필요)
    public ResponseNutritionSumByDateDto getNutritionSumByDate(Long userId, LocalDate date) {
        validateUser(userId);
        List<Food> foodList = foodRepository.findAllByUserIdAndDate(userId, date);
        return calculateNutritionSumAndRatio(userId, foodList, date, 1);
    }

    @Transactional(readOnly = true)
    // 유저의 최근 7일간의 영양성분별 총합 조회 (섭취영양소/기준영양소 및 비율까지 계산해서 반환, dto 구체적 협의 필요)
    public ResponseNutritionSumByDateDto getNutritionSumByWeek(Long userId) {
        validateUser(userId);
        LocalDate endDate = LocalDate.now();
        List<Food> foodList = foodRepository.findAllByUserIdAndDateBetween(userId, endDate.minusWeeks(1), endDate);

        return calculateNutritionSumAndRatio(userId, foodList, endDate, 7);
    }

    @Transactional(readOnly = true)
    // 유저의 최근 1개월간의 영양성분별 총합 조회 (섭취영양소/기준영양소 및 비율까지 계산해서 반환, dto 구체적 협의 필요)
    public ResponseNutritionSumByDateDto getNutritionSumByMonth(Long userId) {
        validateUser(userId);
        LocalDate endDate = LocalDate.now();
        List<Food> foodList = foodRepository.findAllByUserIdAndDateBetween(userId, endDate.minusWeeks(1), endDate);

        return calculateNutritionSumAndRatio(userId, foodList, endDate, 30);
    }

    @Transactional(readOnly = true)
    // 유저의 최근 7일간의 Best 3 음식 조회 (dto 구체적 협의 필요)
    public ResponseFoodRankDto getBestFoodByWeek(Long userId) {
        validateUser(userId);
        LocalDate endDate = LocalDate.now();
        List<Food> foodList = foodRepository.findAllByUserIdAndDateBetween(userId, endDate.minusWeeks(1), endDate);

        List<Food> top3Foods = foodList.stream()
                .sorted(Comparator.comparingDouble((Food food) ->
                        0.7 * food.getBaseNutrition().getProtein()- 0.3 * food.getBaseNutrition().getFat()).reversed())
                .limit(3)
                .collect(Collectors.toList()); //고단백 저지방일수록 점수를 높게 측정되도록 기준을 잡은 후, 그 기준을 기반으로 정렬
        //사용한 기준은, 고단백과 저지방의 점수 반영 비율을 7:3으로 측정하고, 단백질량이 높을 수록, 지방량이 낮을 수록 점수가 높음. 이후, 내림차순 정렬
        // ** Best 3 기준 논의 필요 **

        List<ResponseFoodDto> top3FoodsDtoList = top3Foods.stream()
                .map(food -> ResponseFoodDto.of(food.getId(), food.getUser().getId(), food.getName(), food.getDate(), food.getTime(), food.getBaseNutrition(), food.isFavorite())).collect(Collectors.toList());

        return ResponseFoodRankDto.of(userId, top3FoodsDtoList, endDate, true);
    }

    @Transactional(readOnly = true)
    // 유저의 최근 7일간의 Worst 3 음식 조회 (dto 구체적 협의 필요)
    public ResponseFoodRankDto getWorstFoodByWeek(Long userId) {
        validateUser(userId);
        LocalDate endDate = LocalDate.now();
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

        List<ResponseFoodDto> worst3FoodDtoList = worst3Foods.stream()
                .map(food -> ResponseFoodDto.of(food.getId(), food.getUser().getId(), food.getName(), food.getDate(), food.getTime(), food.getBaseNutrition(), food.isFavorite())).collect(Collectors.toList());


        return ResponseFoodRankDto.of(userId, worst3FoodDtoList, endDate, false);
    }

    // 잔여 기능 구현 부분

    // 유저의 구체적인 점수 현황과 Best3, Worst3 조회

    // 유저의 일기 분석 그래프 데이터 및 식습관 totalScore 조회


    @Cacheable(value = "ResponseRankUserDto", key = "#userId", cacheManager = "diareatCacheManager")
    @Transactional(readOnly = true)
    // 유저의 식습관 점수를 기반으로 한 주간 랭킹 조회
    public List<ResponseRankUserDto> getUserRankByWeek(Long userId) {
        List<ResponseRankUserDto> rankUserDtos = new ArrayList<>();
        List<User> users = followRepository.findAllByFromUser(userId); // 유저의 팔로우 유저 명단
        rankUserDtos.add(calculateUserScoreThisWeek(getUserById(userId), LocalDate.now().with(DayOfWeek.MONDAY), LocalDate.now()));
        if (users.isEmpty()) { // 팔로우한 유저가 없는 경우 본인의 점수 및 정보만 반환함
            return rankUserDtos;
        }

        // 팔로우한 유저들의 점수를 계산하여 rankUserDtos에 추가
        for (User user : users) {
            ResponseRankUserDto userDto = calculateUserScoreThisWeek(user, LocalDate.now().with(DayOfWeek.MONDAY), LocalDate.now());
            rankUserDtos.add(userDto);
        }

        // 식습관 총점 기준 내림차순 정렬
        rankUserDtos.sort(Comparator.comparing(ResponseRankUserDto::getTotalScore).reversed());
        return rankUserDtos;
    }


    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }

    private Food getFoodById(Long foodId) {
        return foodRepository.findById(foodId)
                .orElseThrow(() -> new FoodException(ResponseCode.FOOD_NOT_FOUND));
    }

    private FavoriteFood getFavoriteFoodById(Long foodId) {
        return favoriteFoodRepository.findById(foodId)
                .orElseThrow(() -> new FoodException(ResponseCode.FOOD_NOT_FOUND));
    }

    private ResponseRankUserDto calculateUserScoreThisWeek(User targetUser, LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, List<BaseNutrition>> maps = getNutritionSumByDateMap(targetUser.getId(), startDate, endDate);
        double kcalScore = 0.0;
        double carbohydrateScore = 0.0;
        double proteinScore = 0.0;
        double fatScore = 0.0;
        double totalScore;

        for (LocalDate date : maps.keySet()) {
            // 해당 날짜에 먹은 음식들의 영양성분 총합 계산
            int totalKcal = maps.get(date).stream().mapToInt(BaseNutrition::getKcal).sum();
            int totalCarbohydrate = maps.get(date).stream().mapToInt(BaseNutrition::getCarbohydrate).sum();
            int totalProtein = maps.get(date).stream().mapToInt(BaseNutrition::getProtein).sum();
            int totalFat = maps.get(date).stream().mapToInt(BaseNutrition::getFat).sum();

            // 기준섭취량 대비 섭취 비율에 매핑되는 식습관 점수 계산
            proteinScore += calculateNutriRatioAndScore(totalProtein, targetUser.getBaseNutrition().getProtein(), 0);
            fatScore += calculateNutriRatioAndScore(totalFat, targetUser.getBaseNutrition().getFat(), 1);
            carbohydrateScore += calculateNutriRatioAndScore(totalCarbohydrate, targetUser.getBaseNutrition().getCarbohydrate(), 1);
            kcalScore += calculateNutriRatioAndScore(totalKcal, targetUser.getBaseNutrition().getKcal(), 1);
        }
        totalScore = (kcalScore + carbohydrateScore + proteinScore + fatScore);
        return ResponseRankUserDto.of(targetUser.getId(), targetUser.getName(), targetUser.getImage(), kcalScore, carbohydrateScore, proteinScore, fatScore, totalScore);
    }

    private ResponseNutritionSumByDateDto calculateNutritionSumAndRatio(Long userId, List<Food> foodList, LocalDate checkDate, int nutritionSumType) {
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

        double ratioKcal = Math.round((((double) totalKcal / (double) targetUser.getBaseNutrition().getKcal()) * 100.0) * 10.0) / 10.0;
        double ratioCarbohydrate = Math.round((((double) totalCarbohydrate / (double) targetUser.getBaseNutrition().getCarbohydrate()) * 100.0) * 10.0) / 10.0;
        double ratioProtein = Math.round((((double) totalProtein / (double) targetUser.getBaseNutrition().getProtein()) * 100.0) * 10.0) / 10.0;
        double ratioFat = Math.round((((double) totalFat / (double) targetUser.getBaseNutrition().getFat()) * 100.0) * 10.0) / 10.0;

        return ResponseNutritionSumByDateDto.of(userId, checkDate, nutritionSumType, totalKcal, totalCarbohydrate, totalProtein, totalFat, ratioKcal, ratioCarbohydrate, ratioProtein, ratioFat);
    }

    private void validateUser(Long userId) {
        if (!userRepository.existsById(userId))
            throw new UserException(ResponseCode.USER_NOT_FOUND);
    }

    private void validateFood(Long foodId, Long userId) {
        if(!foodRepository.existsById(foodId))
            throw new FoodException(ResponseCode.FOOD_NOT_FOUND);
        if (!foodRepository.existsByIdAndUserId(foodId, userId)) // 음식의 주인이 유저인지 아닌지 판정
            throw new FoodException(ResponseCode.NOT_FOOD_OWNER);
    }

    private void validateFavoriteFood(Long favoriteFoodId, Long userId) {
        if(!favoriteFoodRepository.existsById(favoriteFoodId))
            throw new FavoriteException(ResponseCode.FAVORITE_NOT_FOUND);
        if(!favoriteFoodRepository.existsByIdAndUserId(favoriteFoodId, userId)) // 즐겨찾는 음식의 주인이 유저인지 아닌지 판정
            throw new FavoriteException(ResponseCode.NOT_FOOD_OWNER);
    }

    // 1주일동안 먹은 음식들의 영양성분 총합을 요일을 Key로 한 Map을 통해 반환
    private HashMap<LocalDate, List<BaseNutrition>> getNutritionSumByDateMap(Long userId, LocalDate startDate, LocalDate endDate) {
        HashMap<LocalDate, List<BaseNutrition>> maps = new HashMap<>();
        List<Food> foodList = foodRepository.findAllByUserIdAndDateBetween(userId, startDate, endDate);
        for (Food food : foodList) {
            if (maps.containsKey(food.getDate())) {
                maps.get(food.getDate()).add(food.getBaseNutrition());
            } else {
                List<BaseNutrition> baseNutritions = new ArrayList<>();
                baseNutritions.add(food.getBaseNutrition());
                maps.put(food.getDate(), baseNutritions);
            }
        }
        return maps;
    }

    // 영양성분 총합 대비 기준섭취량 비율을 계산하여 성분별 식습관 점수를 반환
    private double calculateNutriRatioAndScore(double total, double standard, int type) {
        double ratio = Math.round(((total / standard) * 100.0) * 10.0) / 10.0;
        if (type == 0) { // 단백질
            if (ratio < 100.0) return ratio;
            else if (ratio <= 150) return 100;
            else return (-2 * ratio + 400 < 0) ? 0 : (-2 * ratio + 400);
        } else { // 칼탄지
            double gradient = 1.11; // (9분의 10)
            if (ratio < 90.0) return ratio * gradient;
            else if (ratio <= 110) return 100;
            else return (-gradient * (ratio - 200) < 0) ? 0 : (-gradient * (ratio - 200));
        }
    }

    /**
     * 메서드 구현 유의사항
     * 1. 메서드명은 동사로 시작
     * 2. 유효성 검사는 private void validateUser의 형태로 분리
     * 3. Repository에서 가져오고 OrElseThrow로 예외처리하는 부분이 반복되는 경우 private ??? get???ById 형태 메서드로 분리
     */
}
