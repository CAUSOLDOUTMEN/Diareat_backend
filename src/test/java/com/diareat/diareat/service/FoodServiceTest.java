package com.diareat.diareat.service;

import com.diareat.diareat.food.domain.Food;
import com.diareat.diareat.food.dto.CreateFoodDto;
import com.diareat.diareat.food.dto.ResponseFoodDto;
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
    void testSaveAndGetFood() { // 음식 정보 저장 및 업데이트
        // given
        BaseNutrition testBaseNutrition = BaseNutrition.createNutrition(1,1,1,1);
        Long userId = userService.saveUser(CreateUserDto.of("testUser", "testPassword", 1,180, 80,18));

        //when
        Long foodId = foodService.saveFood(CreateFoodDto.of(userId,"testFood",testBaseNutrition));
        Food testFood = foodRepository.getReferenceById(foodId);

        List<ResponseFoodDto> responseFoodDto = foodService.getFoodListByDate(userId, testFood.getDate());

        assertNotNull(responseFoodDto);
        assertEquals("testFood",responseFoodDto.get(0).getName());
    }


}
