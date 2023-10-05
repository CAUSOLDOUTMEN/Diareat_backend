package com.diareat.diareat.service;

import com.diareat.diareat.food.domain.FavoriteFood;
import com.diareat.diareat.food.domain.Food;
import com.diareat.diareat.food.dto.*;
import com.diareat.diareat.food.repository.FavoriteFoodRepository;
import com.diareat.diareat.food.repository.FoodRepository;
import com.diareat.diareat.food.service.FoodService;
import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.dto.CreateUserDto;
import com.diareat.diareat.user.repository.UserRepository;
import com.diareat.diareat.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class FoodServiceTest {

    @Autowired
    FoodService foodService;

    @Autowired
    UserService userService;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    FavoriteFoodRepository favoriteFoodRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        foodRepository.deleteAll();
        favoriteFoodRepository.deleteAll();
    }

    @Test
    void testSaveAndGetFood() { // 음식 정보 저장 및 해당 날짜 음식 리스트 불러오기
        // given
        BaseNutrition testBaseNutrition = BaseNutrition.createNutrition(1,1,1,1);
        Long userId = userService.saveUser(CreateUserDto.of("testUser", "testPassword", 1,180, 80,18));

        //when
        Long foodId = foodService.saveFood(CreateFoodDto.of(userId,"testFood",testBaseNutrition));
        Food testFood = foodRepository.getReferenceById(foodId);

        List<ResponseFoodDto> responseFoodDtoList = foodService.getFoodListByDate(userId, testFood.getDate());

        assertNotNull(responseFoodDtoList);
        assertEquals("testFood",responseFoodDtoList.get(0).getName());
    }

    @Test
    void testUpdateFood() {
        //given
        BaseNutrition testBaseNutrition = BaseNutrition.createNutrition(1,1,1,1);
        Long userId = userService.saveUser(CreateUserDto.of("testUser", "tessPassword", 1, 180, 80, 18));
        Long foodId = foodService.saveFood(CreateFoodDto.of(userId, "testFood", testBaseNutrition));

        //when
        BaseNutrition testChangedBaseNutrition = BaseNutrition.createNutrition(2,3,4,5);
        foodService.updateFood(UpdateFoodDto.of(foodId, "testChangedFood", testChangedBaseNutrition));

        Food changedFood = foodRepository.getReferenceById(foodId);

        assertNotNull(changedFood);
        assertEquals("testChangedFood", changedFood.getName());
        assertEquals(2,changedFood.getBaseNutrition().getKcal());
        assertEquals(3,changedFood.getBaseNutrition().getCarbohydrate());
        assertEquals(4,changedFood.getBaseNutrition().getProtein());
        assertEquals(5,changedFood.getBaseNutrition().getFat());
    }

    @Test
    void testDeleteFood() {
        //given
        BaseNutrition testBaseNutrition = BaseNutrition.createNutrition(1,1,1,1);
        Long userId = userService.saveUser(CreateUserDto.of("testUser", "tessPassword", 1, 180, 80, 18));
        Long foodId = foodService.saveFood(CreateFoodDto.of(userId, "testFood", testBaseNutrition));

        //when
        foodService.deleteFood(foodId);

        assertNull(foodRepository.findById(foodId).orElse(null));
    }

    @Test
    void testSaveAndGetFavoriteFood() {
        //given
        BaseNutrition testBaseNutrition = BaseNutrition.createNutrition(1,1,1,1);
        Long userId = userService.saveUser(CreateUserDto.of("testUser", "tessPassword", 1, 180, 80, 18));
        Long foodId = foodService.saveFood(CreateFoodDto.of(userId, "testFood", testBaseNutrition));

        //when
        Long favoriteFoodId = foodService.saveFavoriteFood(CreateFavoriteFoodDto.of(foodId, userId, "testFood", testBaseNutrition));

        List<ResponseFavoriteFoodDto> responseFavoriteFoodDtoList = foodService.getFavoriteFoodList(userId);

        assertNotNull(responseFavoriteFoodDtoList);
        assertEquals("testFood",responseFavoriteFoodDtoList.get(0).getName());
    }

    @Test
    void testUpdateFavoriteFood() {
        //given
        BaseNutrition testBaseNutrition = BaseNutrition.createNutrition(1,1,1,1);
        Long userId = userService.saveUser(CreateUserDto.of("testUser", "tessPassword", 1, 180, 80, 18));
        Long foodId = foodService.saveFood(CreateFoodDto.of(userId, "testFood", testBaseNutrition));
        Long favoriteFoodId = foodService.saveFavoriteFood(CreateFavoriteFoodDto.of(foodId, userId, "testFood", testBaseNutrition));


        //when
        BaseNutrition testChangedBaseNutrition = BaseNutrition.createNutrition(2,3,4,5);
        foodService.updateFavoriteFood(UpdateFavoriteFoodDto.of(favoriteFoodId, "testChangedFood", testChangedBaseNutrition));

        FavoriteFood changedFood = favoriteFoodRepository.getReferenceById(favoriteFoodId);

        assertNotNull(changedFood);
        assertEquals("testChangedFood", changedFood.getName());
        assertEquals(2,changedFood.getBaseNutrition().getKcal());
        assertEquals(3,changedFood.getBaseNutrition().getCarbohydrate());
        assertEquals(4,changedFood.getBaseNutrition().getProtein());
        assertEquals(5,changedFood.getBaseNutrition().getFat());
    }

    @Test
    void testDeleteFavoriteFood() {
        //given
        BaseNutrition testBaseNutrition = BaseNutrition.createNutrition(1,1,1,1);
        Long userId = userService.saveUser(CreateUserDto.of("testUser", "tessPassword", 1, 180, 80, 18));
        Long foodId = foodService.saveFood(CreateFoodDto.of(userId, "testFood", testBaseNutrition));
        Long favoriteFoodId = foodService.saveFavoriteFood(CreateFavoriteFoodDto.of(foodId, userId, "testFood", testBaseNutrition));

        //when
        foodService.deleteFavoriteFood(favoriteFoodId);

        assertNull(favoriteFoodRepository.findById(favoriteFoodId).orElse(null));
    }

    @Test
    void testNutritionSumByDate(){
        //given
        BaseNutrition testFoodNutrition = BaseNutrition.createNutrition(100,150,200,250);
        Long userId = userService.saveUser(CreateUserDto.of("testUser", "testPassword",1, 180, 80, 18));
        Long foodId = foodService.saveFood(CreateFoodDto.of(userId,"testFood", testFoodNutrition));
        Food food = foodRepository.getReferenceById(foodId);

        //when
        ResponseNutritionSumByDateDto responseNutritionSumByDateDto = foodService.getNutritionSumByDate(userId,food.getDate());

        assertNotNull(responseNutritionSumByDateDto);
        assertEquals(100, responseNutritionSumByDateDto.getTotalKcal());
        assertEquals(150, responseNutritionSumByDateDto.getTotalCarbohydrate());
        assertEquals(200, responseNutritionSumByDateDto.getTotalProtein());
        assertEquals(250, responseNutritionSumByDateDto.getTotalFat());

        assertEquals(Math.round((100*1.0)/(2000*1.0))*10.0, responseNutritionSumByDateDto.getRatioKcal());
        assertEquals(Math.round((150*1.0)/(300*1.0))*10.0, responseNutritionSumByDateDto.getRatioCarbohydrate());
        assertEquals(Math.round((200*1.0)/(80*1.0))*10.0, responseNutritionSumByDateDto.getRatioProtein());
        assertEquals(Math.round((250*1.0)/(80*1.0))*10.0, responseNutritionSumByDateDto.getRatioFat());

    }
}
