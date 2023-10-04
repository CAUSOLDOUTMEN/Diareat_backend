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
import com.diareat.diareat.user.dto.ResponseResearchUserDto;
import com.diareat.diareat.user.dto.ResponseUserDto;
import com.diareat.diareat.user.dto.UpdateUserDto;
import com.diareat.diareat.user.repository.UserRepository;
import com.diareat.diareat.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.log4j2.Log4J2LoggingSystem;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

        List<ResponseFavoriteFoodDto> responseFavoriteFoodDtoList = foodService.getFavoriteFoodByUserId(userId);

        assertNotNull(responseFavoriteFoodDtoList);
        assertEquals("testFood",responseFavoriteFoodDtoList.get(0).getName());
    }
}
