package com.diareat.diareat.service;

import com.diareat.diareat.food.domain.FavoriteFood;
import com.diareat.diareat.food.domain.Food;
import com.diareat.diareat.food.dto.*;
import com.diareat.diareat.food.repository.FavoriteFoodRepository;
import com.diareat.diareat.food.repository.FoodRepository;
import com.diareat.diareat.food.service.FoodService;
import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.domain.User;
import com.diareat.diareat.user.dto.response.ResponseRankUserDto;
import com.diareat.diareat.user.dto.response.ResponseSimpleUserDto;
import com.diareat.diareat.user.repository.FollowRepository;
import com.diareat.diareat.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class FoodServiceTest {

    @InjectMocks
    FoodService foodService;

    @Mock
    FoodRepository foodRepository;

    @Mock
    FavoriteFoodRepository favoriteFoodRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    FollowRepository followRepository;

    @DisplayName("음식 정보 저장")
    @Test
    void testSaveAndGetFood() { // 음식 정보 저장 및 해당 날짜 음식 리스트 불러오기
        // given
        BaseNutrition testBaseNutrition = BaseNutrition.createNutrition(1,1,1,1);
        User user = User.createUser("testUser", "testImage","testPassword", 1,180, 80,18, testBaseNutrition);
        user.setId(1L);

        CreateFoodDto createFoodDto = CreateFoodDto.of(user.getId(), "testFood", testBaseNutrition, LocalDate.now());
        Food food = Food.createFood("testFood", user, testBaseNutrition);
        food.setId(2L);

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.existsById(user.getId())).willReturn(true);
        given(foodRepository.save(any(Food.class))).willReturn(food);
        given(foodRepository.findAllByUserIdAndDate(any(Long.class), any(LocalDate.class))).willReturn(List.of(food));

        //when
        Long foodId = foodService.saveFood(createFoodDto);

        List<ResponseFoodDto> responseFoodDtoList = foodService.getFoodListByDate(user.getId(), food.getDate());

        assertEquals(2L, foodId);
        assertEquals("testFood",responseFoodDtoList.get(0).getName());
        verify(foodRepository, times(1)).save(any(Food.class));
    }

    @Test
    void testUpdateFood() {
        //given
        BaseNutrition testBaseNutrition = BaseNutrition.createNutrition(1,1,1,1);
        User user = User.createUser("testUser", "testImage","tessPassword", 1, 180, 80, 18, testBaseNutrition);
        Food food = Food.createFood("testFood", user, testBaseNutrition);
        food.setId(1L);

        given(foodRepository.findById(food.getId())).willReturn(Optional.of(food));


        //when
        BaseNutrition testChangedBaseNutrition = BaseNutrition.createNutrition(2,3,4,5);
        foodService.updateFood(UpdateFoodDto.of(food.getId(), 1L,"testChangedFood", testChangedBaseNutrition));


        assertEquals("testChangedFood", food.getName());
        assertEquals(2,food.getBaseNutrition().getKcal());
        assertEquals(3,food.getBaseNutrition().getCarbohydrate());
        assertEquals(4,food.getBaseNutrition().getProtein());
        assertEquals(5,food.getBaseNutrition().getFat());
    }
    //
    @Test
    void testDeleteFood() {
        //given
        BaseNutrition testBaseNutrition = BaseNutrition.createNutrition(1,1,1,1);
        User user = User.createUser("testUser", "testImage","tessPassword", 1, 180, 80, 18, testBaseNutrition);
        Food food = Food.createFood("testFood", user, testBaseNutrition);
        food.setId(1L);

        given(foodRepository.existsById(food.getId())).willReturn(true);
        given(foodRepository.existsByIdAndUserId(food.getId(), 1L)).willReturn(true);

        //when
        foodService.deleteFood(food.getId(), 1L);

        verify(foodRepository, times(1)).deleteById(food.getId());
    }

    @Test
    void testSaveAndGetFavoriteFood() {
        BaseNutrition testBaseNutrition = BaseNutrition.createNutrition(1,1,1,1);
        User user = User.createUser("testUser", "testImage","testPassword", 1,180, 80,18, testBaseNutrition);
        FavoriteFood favoriteFood = FavoriteFood.createFavoriteFood("testFavoriteFood", user, testBaseNutrition);
        user.setId(1L);
        favoriteFood.setId(3L);

        CreateFavoriteFoodDto createFavoriteFoodDto = CreateFavoriteFoodDto.of(favoriteFood.getId(), user.getId(),"testFood", testBaseNutrition);

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.existsById(user.getId())).willReturn(true);
        given(favoriteFoodRepository.save(any(FavoriteFood.class))).willReturn(favoriteFood);
        given(favoriteFoodRepository.findAllByUserId(any(Long.class))).willReturn(List.of(favoriteFood));

        //when
        Long foodId = foodService.saveFavoriteFood(createFavoriteFoodDto);

        List<ResponseFavoriteFoodDto> responseFavoriteFoodDtoList = foodService.getFavoriteFoodList(user.getId());

        assertEquals(3L, foodId);
        assertEquals("testFavoriteFood",responseFavoriteFoodDtoList.get(0).getName());
        verify(favoriteFoodRepository, times(1)).save(any(FavoriteFood.class));
    }
    @Test
    void testUpdateFavoriteFood() {
        //given
        BaseNutrition testBaseNutrition = BaseNutrition.createNutrition(1,1,1,1);
        User user = User.createUser("testUser", "testImage","tessPassword", 1, 180, 80, 18, testBaseNutrition);
        FavoriteFood favoriteFood = FavoriteFood.createFavoriteFood("testFavoriteFood", user, testBaseNutrition);
        favoriteFood.setId(1L);

        given(favoriteFoodRepository.findById(favoriteFood.getId())).willReturn(Optional.of(favoriteFood));


        //when
        BaseNutrition testChangedBaseNutrition = BaseNutrition.createNutrition(2,3,4,5);
        foodService.updateFavoriteFood(UpdateFavoriteFoodDto.of(favoriteFood.getId(), 1L,"testChangedFood", testChangedBaseNutrition));

        assertEquals("testChangedFood", favoriteFood.getName());
        assertEquals(2,favoriteFood.getBaseNutrition().getKcal());
        assertEquals(3,favoriteFood.getBaseNutrition().getCarbohydrate());
        assertEquals(4,favoriteFood.getBaseNutrition().getProtein());
        assertEquals(5,favoriteFood.getBaseNutrition().getFat());
    }
    //
    @Test
    void testDeleteFavoriteFood() {
        //given
        BaseNutrition testBaseNutrition = BaseNutrition.createNutrition(1,1,1,1);
        User user = User.createUser("testUser", "testImage","tessPassword", 1, 180, 80, 18, testBaseNutrition);
        FavoriteFood favoriteFood = FavoriteFood.createFavoriteFood("testFood", user, testBaseNutrition);
        favoriteFood.setId(1L);

        given(favoriteFoodRepository.existsById(favoriteFood.getId())).willReturn(true);
        given(favoriteFoodRepository.existsByIdAndUserId(favoriteFood.getId(), 1L)).willReturn(true);

        //when
        foodService.deleteFavoriteFood(favoriteFood.getId(), 1L);

        verify(favoriteFoodRepository, times(1)).deleteById(favoriteFood.getId());
    }
    //
    @Test
    void testNutritionSumByDate(){
        //given
        BaseNutrition testFoodNutrition = BaseNutrition.createNutrition(1400,150,200,250);
        User user = User.createUser("testUser", "testImage","testPassword",1, 180, 80, 18, BaseNutrition.createNutrition(2000,300,80,80));
        user.setId(1L);
        Food food = Food.createFood("testFood", user, testFoodNutrition);
        food.setId(2L);

        given(userRepository.existsById(user.getId())).willReturn(true);
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(foodRepository.findAllByUserIdAndDate(any(Long.class), any(LocalDate.class))).willReturn(List.of(food));


        //when
        ResponseNutritionSumByDateDto responseNutritionSumByDateDto = foodService.getNutritionSumByDate(user.getId(),food.getDate());
        assertEquals(1400, responseNutritionSumByDateDto.getTotalKcal());
        assertEquals(150, responseNutritionSumByDateDto.getTotalCarbohydrate());
        assertEquals(200, responseNutritionSumByDateDto.getTotalProtein());
        assertEquals(250, responseNutritionSumByDateDto.getTotalFat());

        assertEquals(Math.round((((double)1400 / (double)2000) * 100.0)*100.0)/100.0, responseNutritionSumByDateDto.getRatioKcal());
        assertEquals(Math.round((((double)150 / (double)300) * 100.0)*100.0)/100.0, responseNutritionSumByDateDto.getRatioCarbohydrate());
        assertEquals(Math.round((((double)200 / (double)80) * 100.0)*100.0)/100.0, responseNutritionSumByDateDto.getRatioProtein());
        assertEquals(Math.round((((double)250 / (double)80) * 100.0)*100.0)/100.0, responseNutritionSumByDateDto.getRatioFat());
    }
    //
    @Test
    void testNutritionSumByWeek(){
        //given
        BaseNutrition testFoodNutrition = BaseNutrition.createNutrition(1400,150,200,250);
        User user = User.createUser("testUser", "testImage","testPassword",1, 180, 80, 18, BaseNutrition.createNutrition(2000,300,80,80));
        user.setId(1L);
        Food food = Food.createFood("testFood", user, testFoodNutrition);
        food.setId(2L);

        given(userRepository.existsById(user.getId())).willReturn(true);
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(foodRepository.findAllByUserIdAndDateBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class))).willReturn(List.of(food));



        //when
        ResponseNutritionSumByDateDto responseNutritionSumByDateDto = foodService.getNutritionSumByWeek(user.getId());
        assertEquals(1400, responseNutritionSumByDateDto.getTotalKcal());
        assertEquals(150, responseNutritionSumByDateDto.getTotalCarbohydrate());
        assertEquals(200, responseNutritionSumByDateDto.getTotalProtein());
        assertEquals(250, responseNutritionSumByDateDto.getTotalFat());

        assertEquals(Math.round((((double)1400 / (double)2000) * 100.0)*100.0)/100.0, responseNutritionSumByDateDto.getRatioKcal());
        assertEquals(Math.round((((double)150 / (double)300) * 100.0)*100.0)/100.0, responseNutritionSumByDateDto.getRatioCarbohydrate());
        assertEquals(Math.round((((double)200 / (double)80) * 100.0)*100.0)/100.0, responseNutritionSumByDateDto.getRatioProtein());
        assertEquals(Math.round((((double)250 / (double)80) * 100.0)*100.0)/100.0, responseNutritionSumByDateDto.getRatioFat());

    }

    @Test
    void testNutritionSumByMonth(){
        //given
        BaseNutrition testFoodNutrition = BaseNutrition.createNutrition(1400,150,200,250);
        User user = User.createUser("testUser", "testImage","testPassword",1, 180, 80, 18, BaseNutrition.createNutrition(2000,300,80,80));
        user.setId(1L);
        Food food = Food.createFood("testFood", user, testFoodNutrition);
        food.setId(2L);

        given(userRepository.existsById(user.getId())).willReturn(true);
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(foodRepository.findAllByUserIdAndDateBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class))).willReturn(List.of(food));



        //when
        ResponseNutritionSumByDateDto responseNutritionSumByDateDto = foodService.getNutritionSumByMonth(user.getId());
        assertEquals(1400, responseNutritionSumByDateDto.getTotalKcal());
        assertEquals(150, responseNutritionSumByDateDto.getTotalCarbohydrate());
        assertEquals(200, responseNutritionSumByDateDto.getTotalProtein());
        assertEquals(250, responseNutritionSumByDateDto.getTotalFat());

        assertEquals(Math.round((((double)1400 / (double)2000) * 100.0)*100.0)/100.0, responseNutritionSumByDateDto.getRatioKcal());
        assertEquals(Math.round((((double)150 / (double)300) * 100.0)*100.0)/100.0, responseNutritionSumByDateDto.getRatioCarbohydrate());
        assertEquals(Math.round((((double)200 / (double)80) * 100.0)*100.0)/100.0, responseNutritionSumByDateDto.getRatioProtein());
        assertEquals(Math.round((((double)250 / (double)80) * 100.0)*100.0)/100.0, responseNutritionSumByDateDto.getRatioFat());

    }
    //
    @Test
    void getBest3FoodTest() {
        // given
        User user = User.createUser("testUser", "testImage","testPassword", 1, 180, 80, 18, BaseNutrition.createNutrition(1,1,1,1));
        Food food1 = Food.createFood( "Food1", user, BaseNutrition.createNutrition(100, 100 ,10, 1));
        Food food2 = Food.createFood( "Food2", user, BaseNutrition.createNutrition(100, 100 ,8, 2));
        Food food3 = Food.createFood( "Food3", user, BaseNutrition.createNutrition(100, 100 ,6, 3));
        Food food4 = Food.createFood( "Food4", user, BaseNutrition.createNutrition(100, 100 ,4, 4));
        Food food5 = Food.createFood( "Food5", user, BaseNutrition.createNutrition(100, 100 ,2, 5));
        user.setId(1L);

        List<Food> foodList = List.of(food1, food2, food3, food4, food5);

        given(foodRepository.findAllByUserIdAndDateBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class))).willReturn(foodList);
        given(userRepository.existsById(user.getId())).willReturn(true);

        // when
        ResponseFoodRankDto response = foodService.getBestFoodByWeek(user.getId());
        List<ResponseSimpleFoodDto> top3Foods = response.getRankFoodList();

        // then
        assertEquals(3, top3Foods.size());
        assertEquals("Food1", top3Foods.get(0).getName());
        assertEquals("Food2", top3Foods.get(1).getName());
        assertEquals("Food3", top3Foods.get(2).getName());
    }
    //
    @Test
    void getWorst3FoodsTest() {
        // given
        User user = User.createUser("testUser", "testImage","testPassword", 1, 180, 80, 18, BaseNutrition.createNutrition(1,1,1,1));
        Food food1 = Food.createFood( "Food1", user, BaseNutrition.createNutrition(100, 50 ,10, 1));
        Food food2 = Food.createFood( "Food2", user, BaseNutrition.createNutrition(100, 100 ,8, 20));
        Food food3 = Food.createFood( "Food3", user, BaseNutrition.createNutrition(100, 80 ,6, 7));
        Food food4 = Food.createFood( "Food4", user, BaseNutrition.createNutrition(100, 100 ,4, 5));
        Food food5 = Food.createFood( "Food5", user, BaseNutrition.createNutrition(100, 90 ,2, 6));
        user.setId(1L);

        List<Food> foodList = List.of(food1, food2, food3, food4, food5);

        given(foodRepository.findAllByUserIdAndDateBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class))).willReturn(foodList);
        given(userRepository.existsById(user.getId())).willReturn(true);

        // when
        ResponseFoodRankDto response = foodService.getWorstFoodByWeek(user.getId());
        List<ResponseSimpleFoodDto> top3Foods = response.getRankFoodList();

        // then
        assertEquals(3, top3Foods.size());
        assertEquals("Food2", top3Foods.get(0).getName());
        assertEquals("Food4", top3Foods.get(1).getName());
        assertEquals("Food5", top3Foods.get(2).getName());
    }

    @Test
    void getUserRankByWeek() {
        // given
        User user1 = User.createUser("testUser1", "testImage","testPassword", 180, 80, 0, 18, BaseNutrition.createNutrition(1000,100,100,100));
        User user2 = User.createUser("testUser2", "testImage","testPassword", 180, 80, 0, 18, BaseNutrition.createNutrition(1000,100,100,100));
        user1.setId(1L);
        user2.setId(2L);

        Food food1 = Food.createFood( "Food1", user1, BaseNutrition.createNutrition(1000, 100 ,100, 100));
        Food food2 = Food.createFood( "Food2", user2, BaseNutrition.createNutrition(2000, 110 ,50, 90));

        given(userRepository.findById(user1.getId())).willReturn(Optional.of(user1));
        given(followRepository.findAllByFromUser(user1.getId())).willReturn(List.of(user2));
        given(foodRepository.findAllByUserIdAndDateBetween(eq(1L), any(LocalDate.class), any(LocalDate.class))).willReturn(List.of(food1));
        given(foodRepository.findAllByUserIdAndDateBetween(eq(2L), any(LocalDate.class), any(LocalDate.class))).willReturn(List.of(food2));

        // when
        List<ResponseRankUserDto> response = foodService.getUserRankByWeek(user1.getId());

        // then
        assertEquals(2, response.size());
        assertEquals("testUser1", response.get(0).getName());
        assertEquals("testUser2", response.get(1).getName());
        assertEquals(100, response.get(0).getCalorieScore());
        assertEquals(100, response.get(0).getCarbohydrateScore());
        assertEquals(100, response.get(0).getProteinScore());
        assertEquals(100, response.get(0).getFatScore());
        assertEquals(400, response.get(0).getTotalScore());

        assertEquals(0, response.get(1).getCalorieScore());
        assertEquals(100, response.get(1).getCarbohydrateScore());
        assertEquals(50, response.get(1).getProteinScore());
        assertEquals(100, response.get(1).getFatScore());
        assertEquals(250, response.get(1).getTotalScore());
        verify(userRepository, times(1)).findById(user1.getId());
        verify(followRepository, times(1)).findAllByFromUser(user1.getId());
        verify(foodRepository, times(1)).findAllByUserIdAndDateBetween(eq(1L), any(LocalDate.class), any(LocalDate.class));
        verify(foodRepository, times(1)).findAllByUserIdAndDateBetween(eq(2L), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void testGetScoreOfUserWithBestAndWorstFoods(){
        // given
        User user = User.createUser("testUser", "testImage","testPassword", 1, 180, 80, 18, BaseNutrition.createNutrition(2000,400,100,50));
        Food food1 = Food.createFood( "Food1", user, BaseNutrition.createNutrition(100, 100 ,10, 1));
        Food food1_1 = Food.createFood( "Food1_1", user, BaseNutrition.createNutrition(130, 100 ,8, 2));
        Food food2 = Food.createFood( "Food2", user, BaseNutrition.createNutrition(150, 100 ,8, 2));
        Food food3 = Food.createFood( "Food3", user, BaseNutrition.createNutrition(200, 100 ,6, 3));
        user.setId(1L);

        food1.setDate(LocalDate.now());
        food1_1.setDate(LocalDate.now());
        food2.setDate(LocalDate.now().minusDays(2));
        food3.setDate(LocalDate.now().minusDays(3));

        List<Food> foodList = List.of(food1, food1_1, food2, food3);

        given(userRepository.existsById(user.getId())).willReturn(true);
        given(userRepository.getReferenceById(any(Long.class))).willReturn(user);
        given(foodRepository.findAllByUserIdAndDateBetween(any(Long.class), any(LocalDate.class), any(LocalDate.class))).willReturn(foodList);

        // when
        ResponseScoreBestWorstDto response = foodService.getScoreOfUserWithBestAndWorstFoods(user.getId());
        List<ResponseSimpleFoodDto> top3Foods = response.getBest();
        List<ResponseSimpleFoodDto> worst3Foods = response.getWorst();
        double totalScore = response.getTotalScore();
        double calorieScore = response.getCalorieScore();
        double carbohydrateScore = response.getCarbohydrateScore();
        double proteinScore = response.getProteinScore();
        double fatScore = response.getFatScore();

        // then
        assertEquals(3, top3Foods.size());
        assertEquals(3, worst3Foods.size());
        assertEquals(192.95, totalScore);
        assertEquals(32.19, calorieScore);
        assertEquals(111.0, carbohydrateScore);
        assertEquals(32.0, proteinScore);
        assertEquals(17.76, fatScore);
    }

    @Test
    void testGetAnalysisOfUser(){
        // given
        User user = User.createUser("testUser", "testImage","testPassword", 1, 180, 80, 18, BaseNutrition.createNutrition(2000,400,100,50));
        Food food1 = Food.createFood( "Food1", user, BaseNutrition.createNutrition(100, 100 ,10, 1));
        Food food1_1 = Food.createFood( "Food1_1", user, BaseNutrition.createNutrition(130, 100 ,8, 2));
        Food food2 = Food.createFood( "Food2", user, BaseNutrition.createNutrition(150, 100 ,8, 2));
        Food food3 = Food.createFood( "Food3", user, BaseNutrition.createNutrition(200, 100 ,6, 3));
        Food food4 = Food.createFood( "Food4", user, BaseNutrition.createNutrition(250, 100 ,4, 4));
        Food food5 = Food.createFood( "Food5", user, BaseNutrition.createNutrition(300, 100 ,2, 5));
        user.setId(1L);

        food1.setDate(LocalDate.now());
        food1_1.setDate(LocalDate.now());
        food2.setDate(LocalDate.now().minusDays(2));
        food3.setDate(LocalDate.now().minusDays(3));
        food4.setDate(LocalDate.now().minusWeeks(1));
        food5.setDate(LocalDate.now().minusWeeks(2));



        List<Food> foodListOfWeek = List.of(food1,food1_1, food2, food3);
        List<Food> foodListOfMonth = List.of(food1, food1_1,food2, food3, food4, food5);



        given(userRepository.existsById(user.getId())).willReturn(true);
        given(userRepository.getReferenceById(any(Long.class))).willReturn(user);
        given(foodRepository.findAllByUserIdAndDateBetween(user.getId(), LocalDate.now().minusWeeks(1), LocalDate.now())).willReturn(foodListOfWeek);
        given(foodRepository.findAllByUserIdAndDateBetween(user.getId(), LocalDate.now().minusWeeks(3).with(DayOfWeek.MONDAY), LocalDate.now())).willReturn(foodListOfMonth);
        given(foodRepository.findAllByUserIdAndDateBetween(user.getId(), LocalDate.now().with(DayOfWeek.MONDAY), LocalDate.now())).willReturn(foodListOfWeek);


        // when
        ResponseAnalysisDto response = foodService.getAnalysisOfUser(user.getId());

        double totalScore = response.getTotalScore();
        List<Double> calorieLastSevenDays = response.getCalorieLastSevenDays();
        List<Double> proteinLastSevenDays = response.getProteinLastSevenDays();
        List<Double> calorieLastFourWeeks = response.getCalorieLastFourWeek();
        List<Double> proteinLastFourWeeks = response.getProteinLastFourWeek();


        // then
        assertEquals(192.95, totalScore);

        //갯수 확인
        assertEquals(3, calorieLastSevenDays.size()); //일주일동안의 음식 -> 3개
        assertEquals(5, calorieLastFourWeeks.size()); //한달동안의 음식 -> 5개


        //날짜 정렬 확인 (1주)
        assertEquals(230, calorieLastSevenDays.get(0));
        assertEquals(18, proteinLastSevenDays.get(0));

        assertEquals(150, calorieLastSevenDays.get(1));
        assertEquals(8, proteinLastSevenDays.get(1));

        assertEquals(200, calorieLastSevenDays.get(2));
        assertEquals(6, proteinLastSevenDays.get(2));

        //날짜 정렬 확인 (2주)
        assertEquals(230, calorieLastFourWeeks.get(0));
        assertEquals(18, proteinLastFourWeeks.get(0));

        assertEquals(150, calorieLastFourWeeks.get(1));
        assertEquals(8, proteinLastFourWeeks.get(1));

        assertEquals(200, calorieLastFourWeeks.get(2));
        assertEquals(6, proteinLastFourWeeks.get(2));

        assertEquals(250, calorieLastFourWeeks.get(3));
        assertEquals(4, proteinLastFourWeeks.get(3));

        assertEquals(300, calorieLastFourWeeks.get(4));
        assertEquals(2, proteinLastFourWeeks.get(4));


        verify(foodRepository, times(1)).findAllByUserIdAndDateBetween(user.getId(), LocalDate.now().minusWeeks(1), LocalDate.now());
        verify(foodRepository, times(1)).findAllByUserIdAndDateBetween(user.getId(), LocalDate.now().minusWeeks(3).with(DayOfWeek.MONDAY), LocalDate.now());
        verify(foodRepository, times(1)).findAllByUserIdAndDateBetween(user.getId(), LocalDate.now().with(DayOfWeek.MONDAY), LocalDate.now());

    }
}
