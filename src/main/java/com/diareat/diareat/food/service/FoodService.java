package com.diareat.diareat.food.service;

import com.diareat.diareat.food.domain.FavoriteFood;
import com.diareat.diareat.food.domain.Food;
import com.diareat.diareat.food.dto.*;
import com.diareat.diareat.food.repository.FavoriteFoodRepository;
import com.diareat.diareat.food.repository.FoodRepository;
import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.domain.User;
import com.diareat.diareat.user.dto.response.ResponseRankUserDto;
import com.diareat.diareat.user.repository.FollowRepository;
import com.diareat.diareat.user.repository.UserRepository;
import com.diareat.diareat.util.MessageUtil;
import com.diareat.diareat.util.api.ResponseCode;
import com.diareat.diareat.util.exception.FavoriteException;
import com.diareat.diareat.util.exception.FoodException;
import com.diareat.diareat.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FoodService {

    private final FoodRepository foodRepository; // 유저:음식은 1:다
    private final FavoriteFoodRepository favoriteFoodRepository; // 유저:즐찾음식은 1:다
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 촬영 후, 음식 정보 저장
    // @CacheEvict(value = {"ResponseNutritionSumByDateDto"}, key = "#userId+createFoodDto.getDate()", cacheManager = "diareatCacheManager")
    @Transactional
    public Long saveFood(CreateFoodDto createFoodDto) {
        if (foodRepository.existsByName(createFoodDto.getName())){
            log.info(createFoodDto.getName() + ": 이미 존재하는 음식 이름입니다.");
            throw new FoodException(ResponseCode.FOOD_NAME_ALREADY_EXIST);
        }
        User user = getUserById(createFoodDto.getUserId());
        Food food = Food.createFood(createFoodDto.getName(), user, createFoodDto.getBaseNutrition(), createFoodDto.getYear(), createFoodDto.getMonth(), createFoodDto.getDay());
        log.info("신규 음식 저장: " + food.getName());
        return foodRepository.save(food).getId();
    }

    // 회원이 특정 날짜에 먹은 음식 조회 -> 돌발적인 행동이 많아 캐시 일관성 유지의 가치가 낮아서 캐싱 배제
    @Transactional(readOnly = true)
    public List<ResponseFoodDto> getFoodListByDate(Long userId, LocalDate date){
        validateUser(userId);
        List<Food> foodList = foodRepository.findAllByUserIdAndDateOrderByAddedTimeAsc(userId, date);
        log.info(date.toString() + "의 "+ userId + "에게 조회된 음식 개수: " + foodList.size() + "개");
        return foodList.stream()
                .map(food -> ResponseFoodDto.of(food.getId(), food.getUser().getId(), food.getName(), food.getBaseNutrition(), food.isFavorite())).collect(Collectors.toList());
    }

    // 음식 정보 수정
    // @CacheEvict(value = {"ResponseNutritionSumByDateDto"}, key = "#updateFoodDto.getUserId()+date.toString()", cacheManager = "diareatCacheManager")
    @Transactional
    public void updateFood(UpdateFoodDto updateFoodDto, LocalDate date) {
        Food food = getFoodById(updateFoodDto.getFoodId());
        food.updateFood(updateFoodDto.getName(), updateFoodDto.getBaseNutrition());
        log.info("음식 정보 수정 완료: " + food.getName());
        foodRepository.save(food);
    }

    // 음식 삭제
    // @CacheEvict(value = {"ResponseNutritionSumByDateDto"}, key = "#userId+date.toString()", cacheManager = "diareatCacheManager")
    @Transactional
    public void deleteFood(Long foodId, Long userId, LocalDate date) {
        validateFood(foodId, userId);
        foodRepository.deleteById(foodId);
        log.info("음식 삭제 완료: " + foodId);
    }

    // 즐겨찾기에 음식 저장
    // @CacheEvict(value = "ResponseFavoriteFoodDto", key = "#createFavoriteFoodDto.getUserId()", cacheManager = "diareatCacheManager")
    @Transactional
    public Long saveFavoriteFood(CreateFavoriteFoodDto createFavoriteFoodDto) {
        validateFood(createFavoriteFoodDto.getFoodId(), createFavoriteFoodDto.getUserId());
        User user = getUserById(createFavoriteFoodDto.getUserId());
        Food food = getFoodById(createFavoriteFoodDto.getFoodId());

        if (food.isFavorite()) {// 이미 즐겨찾기에 추가된 음식인 경우 중복 추가 불가능
            log.info(food.getId() + ": 이미 즐겨찾기에 추가된 음식입니다.");
            throw new FavoriteException(ResponseCode.FAVORITE_ALREADY_EXIST);
        }

        FavoriteFood favoriteFood = FavoriteFood.createFavoriteFood(createFavoriteFoodDto.getName(), user, food, createFavoriteFoodDto.getBaseNutrition());
        food.setFavoriteFood(favoriteFood);
        foodRepository.save(food);
        log.info("즐겨찾기 음식 저장 완료: " + favoriteFood.getName());
        return favoriteFoodRepository.save(favoriteFood).getId();
    }

    //즐겨찾기 음식 리스트 반환
    // @Cacheable(value = "ResponseFavoriteFoodDto", key = "#userId", cacheManager = "diareatCacheManager")
    @Transactional(readOnly = true)
    public List<ResponseFavoriteFoodDto> getFavoriteFoodList(Long userId){
        validateUser(userId);
        List<FavoriteFood> foodList = favoriteFoodRepository.findAllByUserId(userId);
        log.info(userId + "의 즐겨찾기 음식 개수: " + foodList.size() + "개 조회 완료");
        return foodList.stream()
                .map(favoriteFood -> ResponseFavoriteFoodDto.of(favoriteFood.getId(),favoriteFood.getName(),
                        favoriteFood.getBaseNutrition(), favoriteFood.getCount())).collect(Collectors.toList());
    }

    // 즐겨찾기 음식 수정
    // @CacheEvict(value = "ResponseFavoriteFoodDto", key = "updateFavoriteFoodDto.getUserId()", cacheManager = "diareatCacheManager")
    @Transactional
    public void updateFavoriteFood(UpdateFavoriteFoodDto updateFavoriteFoodDto) {
        FavoriteFood food = getFavoriteFoodById(updateFavoriteFoodDto.getFavoriteFoodId());
        food.updateFavoriteFood(updateFavoriteFoodDto.getName(), updateFavoriteFoodDto.getBaseNutrition());
        favoriteFoodRepository.save(food);
        log.info("즐겨찾기 음식 수정 완료: " + food.getName());
    }

    // 즐겨찾기 해제
    // @CacheEvict(value = "ResponseFavoriteFoodDto", key = "#userId", cacheManager = "diareatCacheManager")
    @Transactional
    public void deleteFavoriteFood(Long favoriteFoodId, Long userId) {
        validateFavoriteFood(favoriteFoodId, userId);
        FavoriteFood favoriteFood = getFavoriteFoodById(favoriteFoodId);
        favoriteFood.getFoods().forEach(food -> food.setFavoriteFood(null)); // 즐겨찾기 음식으로부터 태어난 음식들의 즐겨찾기 정보를 null로 초기화
        favoriteFoodRepository.deleteById(favoriteFoodId);
        log.info("즐겨찾기 음식 해제 완료: " + favoriteFoodId);
    }

    // @Cacheable(value = "ResponseNutritionSumByDateDto", key = "#userId+#date.toString()", cacheManager = "diareatCacheManager")
    @Transactional(readOnly = true)
    // 유저의 특정 날짜에 먹은 음식들의 영양성분별 총합 조회 (섭취영양소/기준영양소 및 비율까지 계산해서 반환, dto 구체적 협의 필요)
    public ResponseNutritionSumByDateDto getNutritionSumByDate(Long userId, LocalDate date) {
        validateUser(userId);
        List<Food> foodList = foodRepository.findAllByUserIdAndDateOrderByAddedTimeAsc(userId, date);
        log.info(date.toString() + "기준 "+ userId + "의 음식들의 영양성분별 총합 조회 완료");
        return calculateNutritionSumAndRatio(userId, foodList, date, 1);
    }

    @Transactional(readOnly = true)
    // 유저의 최근 7일간의 영양성분별 총합 조회 (섭취영양소/기준영양소 및 비율까지 계산해서 반환, dto 구체적 협의 필요)
    public ResponseNutritionSumByDateDto getNutritionSumByWeek(Long userId, int year, int month, int day) {
        validateUser(userId);
        LocalDate endDate = LocalDate.of(year, month, day);
        List<Food> foodList = foodRepository.findAllByUserIdAndDateBetweenOrderByAddedTimeAsc(userId, endDate.minusWeeks(1), endDate);
        return calculateNutritionSumAndRatio(userId, foodList, endDate, 7);
    }

    @Transactional(readOnly = true)
    // 유저의 최근 1개월간의 영양성분별 총합 조회 (섭취영양소/기준영양소 및 비율까지 계산해서 반환, dto 구체적 협의 필요)
    public ResponseNutritionSumByDateDto getNutritionSumByMonth(Long userId, int year, int month, int day) {
        validateUser(userId);
        LocalDate endDate = LocalDate.of(year, month, day);
        List<Food> foodList = foodRepository.findAllByUserIdAndDateBetweenOrderByAddedTimeAsc(userId, endDate.minusMonths(1), endDate);

        return calculateNutritionSumAndRatio(userId, foodList, endDate, 30);
    }

    @Transactional(readOnly = true)
    // 유저의 최근 7일간의 Best 3 음식 조회 (dto 구체적 협의 필요)
    public ResponseFoodRankDto getBestFoodByWeek(Long userId, int year, int month, int day) {
        validateUser(userId);
        LocalDate endDate = LocalDate.of(year, month, day);
        List<Food> foodList = foodRepository.findAllByUserIdAndDateBetweenOrderByAddedTimeAsc(userId, endDate.minusWeeks(1), endDate);

        List<Food> top3Foods = foodList.stream()
                .sorted(Comparator.comparingDouble((Food food) ->
                        0.7 * food.getBaseNutrition().getProtein()- 0.3 * food.getBaseNutrition().getFat()).reversed())
                .limit(3)
                .collect(Collectors.toList()); //고단백 저지방일수록 점수를 높게 측정되도록 기준을 잡은 후, 그 기준을 기반으로 정렬
        //사용한 기준은, 고단백과 저지방의 점수 반영 비율을 7:3으로 측정하고, 단백질량이 높을 수록, 지방량이 낮을 수록 점수가 높음. 이후, 내림차순 정렬
        // ** Best 3 기준 논의 필요 **

        List<ResponseSimpleFoodDto> top3FoodsDtoList = top3Foods.stream()
                .map(food -> ResponseSimpleFoodDto.of(food.getName(), food.getBaseNutrition().getKcal(), food.getBaseNutrition().getCarbohydrate(),
                        food.getBaseNutrition().getProtein(), food.getBaseNutrition().getFat(), food.getDate())).collect(Collectors.toList());

        return ResponseFoodRankDto.of(userId, top3FoodsDtoList, endDate, true);
    }

    @Transactional(readOnly = true)
    // 유저의 최근 7일간의 Worst 3 음식 조회 (dto 구체적 협의 필요)
    public ResponseFoodRankDto getWorstFoodByWeek(Long userId, int year, int month, int day) {
        validateUser(userId);
        LocalDate endDate = LocalDate.of(year, month, day);
        List<Food> foodList = foodRepository.findAllByUserIdAndDateBetweenOrderByAddedTimeAsc(userId, endDate.minusWeeks(1), endDate);

        List<Food> worst3Foods = foodList.stream()
                .sorted(Comparator.comparingDouble((Food food) ->
                        0.7 * food.getBaseNutrition().getFat() + 0.3 * food.getBaseNutrition().getCarbohydrate()).reversed())
                .limit(3)
                .collect(Collectors.toList());
        //반대로 고지방 고탄수의 경우를 7:3으로 측정하고, 지방이 높을 수록 점수가 급격히 높아짐. 이 경우는 점수가 높은 것이 안좋음.
        //(수정) https://blog.nongshim.com/1961, 탄수화물이 더 영향을 미친다고 하는데...흠...
        // ** 이점은 논의가 필요할 듯? **
        // 우선 임시로 지방 비율을 높게 설정

        List<ResponseSimpleFoodDto> worst3FoodDtoList = worst3Foods.stream()
                .map(food -> ResponseSimpleFoodDto.of(food.getName(), food.getBaseNutrition().getKcal(), food.getBaseNutrition().getCarbohydrate(),
                        food.getBaseNutrition().getProtein(), food.getBaseNutrition().getFat(), food.getDate())).collect(Collectors.toList());

        return ResponseFoodRankDto.of(userId, worst3FoodDtoList, endDate, false);
    }

    // 잔여 기능 구현 부분

    //유저의 식습관 점수 및 Best 3와 Worst 3 계산
    @Transactional(readOnly = true)
    public ResponseScoreBestWorstDto getScoreOfUserWithBestAndWorstFoods(Long userId, int year, int month, int day){
        validateUser(userId); //유저 객체 검증
        double totalScore = 0.0;
        double kcalScore = 0.0;
        double carbohydrateScore = 0.0;
        double proteinScore = 0.0;
        double fatScore = 0.0;

        User targetUser = userRepository.getReferenceById(userId); //검증된 id로 유저 객체 불러오기

        ResponseRankUserDto scoresOfUser = calculateUserScoreThisWeek(targetUser, LocalDate.of(year, month, day).with(DayOfWeek.MONDAY), LocalDate.of(year, month, day));

        totalScore = Math.round((scoresOfUser.getTotalScore() * 100.0))/ 100.0;
        kcalScore = Math.round((scoresOfUser.getCalorieScore() * 100.0)) / 100.0;
        carbohydrateScore = Math.round((scoresOfUser.getCarbohydrateScore() * 100.0)) / 100.0;
        proteinScore = Math.round((scoresOfUser.getProteinScore() * 100.0)) / 100.0;
        fatScore = Math.round((scoresOfUser.getFatScore() * 100.0 ))/ 100.0;


        //Dto의 형식에 맞게 Best3와 Worst3 음식 계산
        List<ResponseSimpleFoodDto> simpleBestFoodList = getBestFoodByWeek(userId, year, month, day).getRankFoodList();
        List<ResponseSimpleFoodDto> simpleWorstFoodList = getWorstFoodByWeek(userId, year, month, day).getRankFoodList();

        return ResponseScoreBestWorstDto.of(kcalScore, carbohydrateScore, proteinScore, fatScore, totalScore, simpleBestFoodList, simpleWorstFoodList);
    }



    // 유저의 일기 분석 그래프 데이터 및 식습관 totalScore 조회
    @Transactional(readOnly = true)
    public ResponseAnalysisDto getAnalysisOfUser(Long userId, int year, int month, int day){
        validateUser(userId);
        User user = userRepository.getReferenceById(userId);

        //현재 날짜
        LocalDate currentDate = LocalDate.of(year,month,day);

        //최근 1주간 유저가 먹은 음식들의 날짜별 HashMap
        HashMap<LocalDate, List<BaseNutrition>> nutritionSumOfUserByWeek = getNutritionSumByDateMap(userId, currentDate.minusWeeks(1), currentDate);
        HashMap<LocalDate, List<BaseNutrition>> nutritionSumOfUserByMonth = getNutritionSumByDateMap(userId, currentDate.minusWeeks(3).with(DayOfWeek.MONDAY), currentDate);

        double totalWeekScore = calculateUserScoreThisWeek(user, LocalDate.of(year, month, day).with(DayOfWeek.MONDAY), LocalDate.of(year, month, day)).getTotalScore();

        List<Map<LocalDate,Double>>calorieLastSevenDays = new ArrayList<>();
        List<Double> calorieLastFourWeek = new ArrayList<>();
        List<Map<LocalDate,Double>> carbohydrateLastSevenDays = new ArrayList<>();
        List<Double> carbohydrateLastFourWeek = new ArrayList<>();
        List<Map<LocalDate,Double>> proteinLastSevenDays = new ArrayList<>();
        List<Double> proteinLastFourWeek = new ArrayList<>();
        List<Map<LocalDate,Double>> fatLastSevenDays = new ArrayList<>();
        List<Double> fatLastFourWeek = new ArrayList<>();

        //최근 7일간의 식습관, 비어있으면 0으로 반환
        for (LocalDate date = currentDate.minusWeeks(1); date.isBefore(currentDate); date = date.plusDays(1)){
            if(!nutritionSumOfUserByWeek.containsKey(date)){ //해당 날짜에 먹은 음식이 없는 경우는 0으로 반환
                calorieLastSevenDays.add(Map.of(date, 0.0));
                carbohydrateLastSevenDays.add(Map.of(date, 0.0));
                proteinLastSevenDays.add(Map.of(date, 0.0));
                fatLastSevenDays.add(Map.of(date, 0.0));
            }else{
                int totalKcal = nutritionSumOfUserByWeek.get(date).stream().mapToInt(BaseNutrition::getKcal).sum();
                int totalCarbohydrate = nutritionSumOfUserByWeek.get(date).stream().mapToInt(BaseNutrition::getCarbohydrate).sum();
                int totalProtein = nutritionSumOfUserByWeek.get(date).stream().mapToInt(BaseNutrition::getProtein).sum();
                int totalFat = nutritionSumOfUserByWeek.get(date).stream().mapToInt(BaseNutrition::getFat).sum();

                calorieLastSevenDays.add(Map.of(date, (double) totalKcal));
                carbohydrateLastSevenDays.add(Map.of(date, (double) totalCarbohydrate));
                proteinLastSevenDays.add(Map.of(date, (double) totalProtein));
                fatLastSevenDays.add(Map.of(date, (double) totalFat));
            }
        }

        //최근 한달간의 식습관. 총 4주간의 데이터를 반환하며, 한주 단위로 묶어서 반환
        for (LocalDate date = currentDate.minusWeeks(3).with(DayOfWeek.MONDAY); date.isBefore(currentDate); date = date.plusWeeks(1)){
            int totalKcal = 0;
            int totalCarbohydrate = 0;
            int totalProtein = 0;
            int totalFat = 0;
            //해당 주에 먹은 음식들을 모두 합해서 계산
            for (LocalDate inner_date = date; inner_date.isBefore(date.plusWeeks(1)); inner_date = inner_date.plusDays(1)){
                if(nutritionSumOfUserByMonth.containsKey(inner_date)){
                    totalKcal += nutritionSumOfUserByMonth.get(inner_date).stream().mapToInt(BaseNutrition::getKcal).sum();
                    totalCarbohydrate += nutritionSumOfUserByMonth.get(inner_date).stream().mapToInt(BaseNutrition::getCarbohydrate).sum();
                    totalProtein += nutritionSumOfUserByMonth.get(inner_date).stream().mapToInt(BaseNutrition::getProtein).sum();
                    totalFat += nutritionSumOfUserByMonth.get(inner_date).stream().mapToInt(BaseNutrition::getFat).sum();
                }
            }
            calorieLastFourWeek.add((double) totalKcal);
            carbohydrateLastFourWeek.add((double) totalCarbohydrate);
            proteinLastFourWeek.add((double) totalProtein);
            fatLastFourWeek.add((double) totalFat);
        }

        totalWeekScore = Math.round(totalWeekScore * 100.0) / 100.0;

        return ResponseAnalysisDto.of(totalWeekScore, calorieLastSevenDays, calorieLastFourWeek, carbohydrateLastSevenDays, carbohydrateLastFourWeek, proteinLastSevenDays, proteinLastFourWeek, fatLastSevenDays, fatLastFourWeek);
    }



    // @Cacheable(value = "ResponseRankUserDto", key = "#userId", cacheManager = "diareatCacheManager")
    @Transactional(readOnly = true)
    // 유저의 식습관 점수를 기반으로 한 주간 랭킹 조회
    public List<ResponseRankUserDto> getUserRankByWeek(Long userId, int year, int month, int day) {
        List<ResponseRankUserDto> rankUserDtos = new ArrayList<>();
        List<User> users = followRepository.findAllByFromUser(userId); // 유저의 팔로우 유저 명단
        rankUserDtos.add(calculateUserScoreThisWeek(getUserById(userId), LocalDate.of(year, month, day).with(DayOfWeek.MONDAY), LocalDate.of(year, month ,day)));
        if (users.isEmpty()) { // 팔로우한 유저가 없는 경우 본인의 점수 및 정보만 반환함
            return rankUserDtos;
        }

        // 팔로우한 유저들의 점수를 계산하여 rankUserDtos에 추가
        for (User user : users) {
            ResponseRankUserDto userDto = calculateUserScoreThisWeek(user, LocalDate.of(year, month, day).with(DayOfWeek.MONDAY), LocalDate.of(year, month, day));
            rankUserDtos.add(userDto);
        }

        // 식습관 총점 기준 내림차순 정렬
        rankUserDtos.sort(Comparator.comparing(ResponseRankUserDto::getTotalScore).reversed());
        return rankUserDtos;
    }

    @Transactional
    // 즐겨찾기 음식으로부터 새로운 음식을 간편 등록
    public Long createFoodFromFavoriteFood(CreateFoodFromFavoriteFoodDto createFoodFromFavoriteFoodDto) {
        validateFavoriteFood(createFoodFromFavoriteFoodDto.getFavoriteFoodId(), createFoodFromFavoriteFoodDto.getUserId());
        FavoriteFood favoriteFood = getFavoriteFoodById(createFoodFromFavoriteFoodDto.getFavoriteFoodId());
        Food food = FavoriteFood.createFoodFromFavoriteFood(favoriteFood);
        food.setFavoriteFood(favoriteFood);
        return foodRepository.save(food).getId();
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

        return ResponseNutritionSumByDateDto.of(userId, checkDate, nutritionSumType, totalKcal,
                                                totalCarbohydrate, totalProtein, totalFat, ratioKcal,
                                                ratioCarbohydrate, ratioProtein, ratioFat, targetUser.getBaseNutrition());
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
        List<Food> foodList = foodRepository.findAllByUserIdAndDateBetweenOrderByAddedTimeAsc(userId, startDate, endDate);
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
