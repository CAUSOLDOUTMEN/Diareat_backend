package com.diareat.diareat.service;

import com.diareat.diareat.food.domain.FavoriteFood;
import com.diareat.diareat.food.domain.Food;
import com.diareat.diareat.food.dto.*;
import com.diareat.diareat.food.repository.FavoriteFoodRepository;
import com.diareat.diareat.food.repository.FoodRepository;
import com.diareat.diareat.food.service.FoodService;
import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.domain.User;
import com.diareat.diareat.user.dto.CreateUserDto;
import com.diareat.diareat.user.repository.UserRepository;
import com.diareat.diareat.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class FoodServiceTest {

    @InjectMocks
    FoodService foodService;

    @InjectMocks
    UserService userService;

    @Mock
    FoodRepository foodRepository;

    @Mock
    FavoriteFoodRepository favoriteFoodRepository;

    @Mock
    UserRepository userRepository;

    @DisplayName("음식 정보 저장")
    @Test
    void testSaveAndGetFood() { // 음식 정보 저장 및 해당 날짜 음식 리스트 불러오기
        // given
        BaseNutrition testBaseNutrition = BaseNutrition.createNutrition(1,1,1,1);
        User user = User.createUser("testUser", "testImage","testPassword", 1,180, 80,18, testBaseNutrition);
        user.setId(1L);

        CreateFoodDto createFoodDto = CreateFoodDto.of(user.getId(), "testFood", testBaseNutrition);
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
        foodService.updateFood(UpdateFoodDto.of(food.getId(), "testChangedFood", testChangedBaseNutrition));


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

        //when
        foodService.deleteFood(food.getId());

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
        foodService.updateFavoriteFood(UpdateFavoriteFoodDto.of(favoriteFood.getId(), "testChangedFood", testChangedBaseNutrition));

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

        //when
        foodService.deleteFavoriteFood(favoriteFood.getId());

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
//    @Test
//    void testNutritionSumByWeekAndMonth(){
//        //given
//        BaseNutrition testFoodNutrition = BaseNutrition.createNutrition(100,150,200,250);
//        Long userId = userService.saveUser(CreateUserDto.of("testUser", "testImage","testPassword",1, 180, 80, 18));
//        Long foodId = foodService.saveFood(CreateFoodDto.of(userId,"testFood", testFoodNutrition));
//
//    }
//
//    @Test
//    void getBest3FoodTest() {
//        // given
//        Long userId = userService.saveUser(CreateUserDto.of("testUser", "testImage","testPassword", 1, 180, 80, 18));
//        foodService.saveFood(CreateFoodDto.of(userId, "Food1", BaseNutrition.createNutrition(100, 100 ,10, 1)));
//        foodService.saveFood(CreateFoodDto.of(userId, "Food2", BaseNutrition.createNutrition(100, 100 ,8, 2)));
//        foodService.saveFood(CreateFoodDto.of(userId, "Food3", BaseNutrition.createNutrition(100, 100 ,6, 3)));
//        foodService.saveFood(CreateFoodDto.of(userId, "Food4", BaseNutrition.createNutrition(100, 100 ,4, 4)));
//        foodService.saveFood(CreateFoodDto.of(userId, "Food5", BaseNutrition.createNutrition(100, 100 ,2, 5)));
//
//
//        // when
//        ResponseFoodRankDto response = foodService.getBestFoodByWeek(userId);
//        List<ResponseFoodDto> top3Foods = response.getRankFoodList();
//
//        // then
//        assertEquals(3, top3Foods.size());
//        assertEquals("Food1", top3Foods.get(0).getName());
//        assertEquals("Food2", top3Foods.get(1).getName());
//        assertEquals("Food3", top3Foods.get(2).getName());
//    }
//
//    @Test
//    void getWorst3FoodsTest() {
//        // given
//        Long userId = userService.saveUser(CreateUserDto.of("testUser", "testImage","testPassword", 1, 180, 80, 18));
//        foodService.saveFood(CreateFoodDto.of(userId, "Food1", BaseNutrition.createNutrition(100, 50 ,10, 1)));
//        foodService.saveFood(CreateFoodDto.of(userId, "Food2", BaseNutrition.createNutrition(100, 100 ,8, 20)));
//        foodService.saveFood(CreateFoodDto.of(userId, "Food3", BaseNutrition.createNutrition(100, 80 ,6, 7)));
//        foodService.saveFood(CreateFoodDto.of(userId, "Food4", BaseNutrition.createNutrition(100, 100 ,4, 5)));
//        foodService.saveFood(CreateFoodDto.of(userId, "Food5", BaseNutrition.createNutrition(100, 90 ,2, 6)));
//
//
//        // when
//        ResponseFoodRankDto response = foodService.getWorstFoodByWeek(userId);
//        List<ResponseFoodDto> top3Foods = response.getRankFoodList();
//
//        // then
//        assertEquals(3, top3Foods.size());
//        assertEquals("Food2", top3Foods.get(0).getName());
//        assertEquals("Food4", top3Foods.get(1).getName());
//        assertEquals("Food5", top3Foods.get(2).getName());
//    }
}
