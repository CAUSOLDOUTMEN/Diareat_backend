package com.diareat.diareat.controller;

import com.diareat.diareat.food.controller.FoodController;
import com.diareat.diareat.food.domain.FavoriteFood;
import com.diareat.diareat.food.domain.Food;
import com.diareat.diareat.food.dto.*;
import com.diareat.diareat.food.service.FoodService;
import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.domain.User;
import com.diareat.diareat.user.dto.response.ResponseRankUserDto;
import com.diareat.diareat.util.api.ApiResponse;
import com.diareat.diareat.util.api.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = FoodController.class)
class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private FoodService foodService;

    private final Long testUserId = 1L;
    private final Long testFoodId = 2L;
    private final Long testFavoriteFoodId = 3L;
    private final ObjectMapper mapper = new ObjectMapper();

    private final User testUser = User.createUser("test", "test", "test", 180, 70, 0, 20, BaseNutrition.createNutrition(2000, 300, 80, 80));
    private final Food testFood = Food.createFood("testFood", testUser, BaseNutrition.createNutrition(500, 50, 30, 10), 2021, 10, 10);
    private final FavoriteFood testFavoriteFood = FavoriteFood.createFavoriteFood("testFavoriteFood", testUser, testFood, BaseNutrition.createNutrition(500, 50, 30, 10));
    private final BaseNutrition testBaseNutrition = BaseNutrition.createNutrition(500, 50, 30, 10);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        testUser.setId(testUserId);
        testFood.setId(testFoodId);
        testFavoriteFood.setId(testFavoriteFoodId);
        mapper.registerModule(new JavaTimeModule());
    }

    @DisplayName("음식 정보 저장")
    @Test
    @WithMockUser("test")
    void testSaveFood() throws Exception {
        //Given
        CreateFoodDto createFoodDto = CreateFoodDto.of(testUserId, "test", testBaseNutrition, 2021, 10, 10);

        when(foodService.saveFood(any(CreateFoodDto.class))).thenReturn(testFoodId);
        ApiResponse<Long> expectedResponse = ApiResponse.success(foodService.saveFood(createFoodDto), ResponseCode.FOOD_CREATE_SUCCESS.getMessage());


        String json = mapper.writeValueAsString(createFoodDto);

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/food/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(expectedResponse.getData()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));
    }


    @DisplayName("음식 정보 날짜별 조회")
    @Test
    @WithMockUser("test")
    void testGetFoodListByDate() throws Exception {
        //Given
        int yy = 2023;
        int dd = 22;
        int mm = 12;
        LocalDate date = LocalDate.of(yy, mm, dd);

        ResponseFoodDto food1 = ResponseFoodDto.of(testFoodId, testUserId,"test",testBaseNutrition,false);

        when(foodService.getFoodListByDate(any(Long.class), any(LocalDate.class))).thenReturn(List.of(food1));
        ApiResponse<List<ResponseFoodDto>> expectedResponse = ApiResponse.success(List.of(food1), ResponseCode.FOOD_READ_SUCCESS.getMessage());

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/food/{userId}", testUserId)
                        .param("yy", String.valueOf(date.getYear()))
                        .param("mm", String.valueOf(date.getMonthValue()))
                        .param("dd", String.valueOf(date.getDayOfMonth()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(expectedResponse.getData().get(0).getName()))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].date").value(expectedResponse.getData().get(0).getDate().toString()))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].time").value(expectedResponse.getData().get(0).getTime().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].baseNutrition.kcal").value(expectedResponse.getData().get(0).getBaseNutrition().getKcal()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].baseNutrition.carbohydrate").value(expectedResponse.getData().get(0).getBaseNutrition().getCarbohydrate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].baseNutrition.protein").value(expectedResponse.getData().get(0).getBaseNutrition().getProtein()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].baseNutrition.fat").value(expectedResponse.getData().get(0).getBaseNutrition().getFat()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));
    }

    @DisplayName("음식 정보 수정")
    @Test
    @WithMockUser("test")
    void testUpdateFood() throws Exception {
        //Given
        int yy = 2023;
        int dd = 22;
        int mm = 12;
        LocalDate date = LocalDate.of(yy, mm, dd);

        ApiResponse<Void> expectedResponse = ApiResponse.success(null, ResponseCode.FOOD_UPDATE_SUCCESS.getMessage());
        UpdateFoodDto updateFoodDto = UpdateFoodDto.of(testFoodId, testUserId, "testFood", testBaseNutrition);
        String json = mapper.writeValueAsString(updateFoodDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/food/update")
                        .param("yy", String.valueOf(date.getYear()))
                        .param("mm", String.valueOf(date.getMonthValue()))
                        .param("dd", String.valueOf(date.getDayOfMonth()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));

    }

    @DisplayName("음식 정보 삭제")
    @Test
    @WithMockUser("test")
    void testDeleteFood() throws Exception {
        //Given
        int yy = 2023;
        int dd = 22;
        int mm = 12;
        LocalDate date = LocalDate.of(yy, mm, dd);

        ApiResponse<Void> expectedResponse = ApiResponse.success(null, ResponseCode.FOOD_DELETE_SUCCESS.getMessage());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/food/{foodId}/delete", testFoodId)
                        .param("yy", String.valueOf(date.getYear()))
                        .param("mm", String.valueOf(date.getMonthValue()))
                        .param("dd", String.valueOf(date.getDayOfMonth()))
                        .header("userId", testUserId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));
    }

    @DisplayName("즐겨찾기에 음식 저장")
    @Test
    @WithMockUser("test")
    void testSaveFavoriteFood() throws Exception {
        //Given
        ApiResponse<Long> expectedResponse = ApiResponse.success(testFavoriteFoodId, ResponseCode.FOOD_FAVORITE_CREATE_SUCCESS.getMessage());
        CreateFavoriteFoodDto createFavoriteFoodDto = CreateFavoriteFoodDto.of(testFoodId, testUserId, "test", testBaseNutrition);
        String json = mapper.writeValueAsString(createFavoriteFoodDto);
        when(foodService.saveFavoriteFood(any(CreateFavoriteFoodDto.class))).thenReturn(testFavoriteFoodId);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/food/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(expectedResponse.getData()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));
    }

    @DisplayName("즐겨찾기 음식 리스트 반환")
    @Test
    @WithMockUser("test")
    void testGetFavoriteFoodList() throws Exception {
        //Given
        ResponseFavoriteFoodDto food1 = ResponseFavoriteFoodDto.of(testFavoriteFoodId,"test",testBaseNutrition,0);

        ApiResponse<List<ResponseFavoriteFoodDto>> expectedResponse = ApiResponse.success(List.of(food1), ResponseCode.FOOD_FAVORITE_READ_SUCCESS.getMessage());
        when(foodService.getFavoriteFoodList(any(Long.class))).thenReturn(List.of(food1));

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/food/favorite/{userId}", testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(expectedResponse.getData().get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].baseNutrition.kcal").value(expectedResponse.getData().get(0).getBaseNutrition().getKcal()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].baseNutrition.carbohydrate").value(expectedResponse.getData().get(0).getBaseNutrition().getCarbohydrate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].baseNutrition.protein").value(expectedResponse.getData().get(0).getBaseNutrition().getProtein()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].baseNutrition.fat").value(expectedResponse.getData().get(0).getBaseNutrition().getFat()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));
    }

    @DisplayName("즐겨찾기 음식 수정")
    @Test
    @WithMockUser("test")
    void testUpdateFavoriteFood() throws Exception {
        //Given
        ApiResponse<Void> expectedResponse = ApiResponse.success(null, ResponseCode.FOOD_FAVORITE_UPDATE_SUCCESS.getMessage());
        UpdateFavoriteFoodDto updateFavoriteFoodDto = UpdateFavoriteFoodDto.of(testFavoriteFoodId, testUserId, "testFavoriteFood", testBaseNutrition);
        String json = mapper.writeValueAsString(updateFavoriteFoodDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/food/favorite/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));
    }

    @DisplayName("즐겨찾기 음식 삭제")
    @Test
    @WithMockUser("test")
    void testDeleteFavoriteFood() throws Exception {
        //Given
        ApiResponse<Void> expectedResponse = ApiResponse.success(null, ResponseCode.FOOD_FAVORITE_DELETE_SUCCESS.getMessage());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/food/favorite/{favoriteFoodId}", testFavoriteFoodId)
                        .header("userId", testUserId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));
    }

    //특정 날짜에 먹은 음식들의 영양성분별 총합 조회
    @DisplayName("특정 날짜에 먹은 음식들의 영양성분별 총합 조회")
    @Test
    @WithMockUser("test")
    void testGetNutritionSumByDate() throws Exception{
        //Given
        LocalDate date = LocalDate.now();
        ResponseNutritionSumByDateDto responseNutritionSumByDateDto = ResponseNutritionSumByDateDto.of(testUserId,date,1
                ,500,100,50,50,0.2,0.3,0.4,0.5,
                BaseNutrition.createNutrition(500,100,50,50));
        ApiResponse<ResponseNutritionSumByDateDto> expectedResponse = ApiResponse.success(responseNutritionSumByDateDto,ResponseCode.FOOD_READ_SUCCESS.getMessage());
        when(foodService.getNutritionSumByDate(any(Long.class),any(LocalDate.class))).thenReturn(responseNutritionSumByDateDto);


        //When
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/food/{userId}/nutrition", testUserId)
                .param("yy", String.valueOf(date.getYear()))
                .param("mm", String.valueOf(date.getMonthValue()))
                .param("dd", String.valueOf(date.getDayOfMonth())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId").value(expectedResponse.getData().getUserId()))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.data.checkDate").value(expectedResponse.getData().getCheckDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.nutritionSumType").value(expectedResponse.getData().getNutritionSumType()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalKcal").value(expectedResponse.getData().getTotalKcal()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalCarbohydrate").value(expectedResponse.getData().getTotalCarbohydrate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalProtein").value(expectedResponse.getData().getTotalProtein()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalFat").value(expectedResponse.getData().getTotalFat()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.ratioKcal").value(expectedResponse.getData().getRatioKcal()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.ratioCarbohydrate").value(expectedResponse.getData().getRatioCarbohydrate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.ratioProtein").value(expectedResponse.getData().getRatioProtein()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.ratioFat").value(expectedResponse.getData().getRatioFat()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.baseNutrition.carbohydrate").value(expectedResponse.getData().getBaseNutrition().getCarbohydrate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));
    }

    //최근 7일간 먹은 음식들의 영양성분별 총합 조회
    @DisplayName("최근 7일간 먹은 음식들의 영양성분별 총합 조회")
    @Test
    @WithMockUser("test")
    void testGetNutritionSumByWeek() throws Exception {
        //Given
        LocalDate date = LocalDate.now();
        ResponseNutritionSumByDateDto responseNutritionSumByDateDto = ResponseNutritionSumByDateDto.of(testUserId, date, 7
                , 500, 100, 50, 50, 0.2, 0.3, 0.4, 0.5,
                BaseNutrition.createNutrition(500, 100, 50, 50));
        ApiResponse<ResponseNutritionSumByDateDto> expectedResponse = ApiResponse.success(responseNutritionSumByDateDto, ResponseCode.FOOD_READ_SUCCESS.getMessage());
        when(foodService.getNutritionSumByWeek(any(Long.class))).thenReturn(responseNutritionSumByDateDto);


        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/food/{userId}/nutrition/recentWeek", testUserId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId").value(expectedResponse.getData().getUserId()))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.data.checkDate").value(expectedResponse.getData().getCheckDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.nutritionSumType").value(expectedResponse.getData().getNutritionSumType()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalKcal").value(expectedResponse.getData().getTotalKcal()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalCarbohydrate").value(expectedResponse.getData().getTotalCarbohydrate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalProtein").value(expectedResponse.getData().getTotalProtein()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalFat").value(expectedResponse.getData().getTotalFat()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.ratioKcal").value(expectedResponse.getData().getRatioKcal()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.ratioCarbohydrate").value(expectedResponse.getData().getRatioCarbohydrate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.ratioProtein").value(expectedResponse.getData().getRatioProtein()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.ratioFat").value(expectedResponse.getData().getRatioFat()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.baseNutrition.carbohydrate").value(expectedResponse.getData().getBaseNutrition().getCarbohydrate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));
    }

    //최근 한달간 먹은 음식들의 영양성분별 총합 조회
    @DisplayName("최근 한달간 먹은 음식들의 영양성분별 총합 조회")
    @Test
    @WithMockUser("test")
    void testGetNutritionSumByMonth() throws Exception {
        //Given
        LocalDate date = LocalDate.now();
        ResponseNutritionSumByDateDto responseNutritionSumByDateDto = ResponseNutritionSumByDateDto.of(testUserId, date, 30
                , 500, 100, 50, 50, 0.2, 0.3, 0.4, 0.5,
                BaseNutrition.createNutrition(500, 100, 50, 50));
        ApiResponse<ResponseNutritionSumByDateDto> expectedResponse = ApiResponse.success(responseNutritionSumByDateDto, ResponseCode.FOOD_READ_SUCCESS.getMessage());
        when(foodService.getNutritionSumByMonth(any(Long.class))).thenReturn(responseNutritionSumByDateDto);


        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/food/{userId}/nutrition/recentMonth", testUserId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId").value(expectedResponse.getData().getUserId()))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.data.checkDate").value(expectedResponse.getData().getCheckDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.nutritionSumType").value(expectedResponse.getData().getNutritionSumType()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalKcal").value(expectedResponse.getData().getTotalKcal()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalCarbohydrate").value(expectedResponse.getData().getTotalCarbohydrate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalProtein").value(expectedResponse.getData().getTotalProtein()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalFat").value(expectedResponse.getData().getTotalFat()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.ratioKcal").value(expectedResponse.getData().getRatioKcal()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.ratioCarbohydrate").value(expectedResponse.getData().getRatioCarbohydrate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.ratioProtein").value(expectedResponse.getData().getRatioProtein()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.ratioFat").value(expectedResponse.getData().getRatioFat()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.baseNutrition.carbohydrate").value(expectedResponse.getData().getBaseNutrition().getCarbohydrate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));
    }

    //유저의 주간 식습관 점수와 best3, worst3 음식 조회
    @DisplayName("유저의 주간 식습관 점수와 best3, worst3 음식 조회")
    @Test
    @WithMockUser("test")
    void testGetScoreOfUserWithBestAndWorstFoods() throws Exception{
        //Given
        ResponseSimpleFoodDto food1 = ResponseSimpleFoodDto.of("test1", 100, 100, 100, 100, LocalDate.now());
        ResponseSimpleFoodDto food2 = ResponseSimpleFoodDto.of("test", 100, 100, 100, 100, LocalDate.now());
        ResponseSimpleFoodDto food3 = ResponseSimpleFoodDto.of("test", 100, 100, 100, 100, LocalDate.now());
        ResponseSimpleFoodDto food4 = ResponseSimpleFoodDto.of("test4", 100, 100, 100, 100, LocalDate.now());
        ResponseSimpleFoodDto food5 = ResponseSimpleFoodDto.of("test", 100, 100, 100, 100, LocalDate.now());
        ResponseSimpleFoodDto food6 = ResponseSimpleFoodDto.of("test", 100, 100, 100, 100, LocalDate.now());

        ResponseScoreBestWorstDto responseScoreBestWorstDto = ResponseScoreBestWorstDto.of(testUserId, 100, 80
                , 60, 240, List.of(food1, food2,food3), List.of(food4, food5, food6));
        ApiResponse<ResponseScoreBestWorstDto> expectedResponse = ApiResponse.success(responseScoreBestWorstDto, ResponseCode.FOOD_READ_SUCCESS.getMessage());
        when(foodService.getScoreOfUserWithBestAndWorstFoods(any(Long.class))).thenReturn(responseScoreBestWorstDto);


        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/food/{userId}/score", testUserId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalScore").value(expectedResponse.getData().getTotalScore()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.calorieScore").value(expectedResponse.getData().getCalorieScore()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.carbohydrateScore").value(expectedResponse.getData().getCarbohydrateScore()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.proteinScore").value(expectedResponse.getData().getProteinScore()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.fatScore").value(expectedResponse.getData().getFatScore()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.best[0].name").value(expectedResponse.getData().getBest().get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.worst[0].name").value(expectedResponse.getData().getWorst().get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));
    }

    //유저의 일기 분석 그래프 데이터 및 식습관 totalScore 조회
    @DisplayName("유저의 일기 분석 그래프 데이터 및 식습관 totalScore 조회")
    @Test
    @WithMockUser("test")
    void testGetAnalysisOfUser() throws Exception {
        //Given
        ResponseAnalysisDto responseAnalysisDto = ResponseAnalysisDto.of(100, List.of(100.0, 100.0),
                List.of(100.0, 100.0), List.of(100.0, 100.0), List.of(100.0, 100.0), List.of(100.0, 100.0),
                List.of(100.0, 100.0), List.of(100.0, 100.0), List.of(100.0, 100.0));

        ApiResponse<ResponseAnalysisDto> expectedResponse = ApiResponse.success(responseAnalysisDto, ResponseCode.FOOD_READ_SUCCESS.getMessage());
        when(foodService.getAnalysisOfUser(any(Long.class))).thenReturn(responseAnalysisDto);

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/food/{userId}/analysis", testUserId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.calorieLastSevenDays[0]").value(expectedResponse.getData().getCalorieLastSevenDays().get(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.carbohydrateLastSevenDays[0]").value(expectedResponse.getData().getCarbohydrateLastSevenDays().get(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.proteinLastSevenDays[0]").value(expectedResponse.getData().getProteinLastSevenDays().get(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.fatLastSevenDays[0]").value(expectedResponse.getData().getFatLastSevenDays().get(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.calorieLastFourWeek[0]").value(expectedResponse.getData().getCalorieLastFourWeek().get(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.carbohydrateLastFourWeek[0]").value(expectedResponse.getData().getCarbohydrateLastFourWeek().get(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.proteinLastFourWeek[0]").value(expectedResponse.getData().getProteinLastFourWeek().get(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.fatLastFourWeek[0]").value(expectedResponse.getData().getFatLastFourWeek().get(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalScore").value(expectedResponse.getData().getTotalScore()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));

    }

    //식습관 점수 기반 주간 랭킹 조회
    @DisplayName("식습관 점수 기반 주간 랭킹 조회")
    @Test
    @WithMockUser("test")
    void testGetUserRankByWeek() throws Exception{
        //Given
        ResponseRankUserDto responseRankUserDto1 = ResponseRankUserDto.of(1L, "test", "image", 100, 100, 100, 100, 400);
        ResponseRankUserDto responseRankUserDto2 = ResponseRankUserDto.of(2L, "test", "image", 100, 100, 100, 100, 400);
        List<ResponseRankUserDto> responseRankUserDtoList = List.of(responseRankUserDto1, responseRankUserDto2);

        ApiResponse<List<ResponseRankUserDto>> expectedResponse = ApiResponse.success(responseRankUserDtoList, ResponseCode.FOOD_READ_SUCCESS.getMessage());
        when(foodService.getUserRankByWeek(testUserId)).thenReturn(responseRankUserDtoList);

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/food/{userId}/rank", testUserId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].userId").value(expectedResponse.getData().get(0).getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].image").value(expectedResponse.getData().get(0).getImage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].totalScore").value(expectedResponse.getData().get(0).getTotalScore()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].calorieScore").value(expectedResponse.getData().get(0).getCalorieScore()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].carbohydrateScore").value(expectedResponse.getData().get(0).getCarbohydrateScore()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].proteinScore").value(expectedResponse.getData().get(0).getProteinScore()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].fatScore").value(expectedResponse.getData().get(0).getFatScore()));
    }

    @DisplayName("즐겨찾기 음식으로 음식 생성")
    @Test
    @WithMockUser("test")
    void testCreateFoodFromFavoriteFood() throws Exception{
        //Given
        Long testNewFoodId = 1L;
        CreateFoodFromFavoriteFoodDto createFoodFromFavoriteFoodDto = CreateFoodFromFavoriteFoodDto.of(testUserId, testFavoriteFoodId);
        when(foodService.createFoodFromFavoriteFood(any(CreateFoodFromFavoriteFoodDto.class))).thenReturn(testNewFoodId);
        ApiResponse<Long> expectedResponse = ApiResponse.success(foodService.createFoodFromFavoriteFood(createFoodFromFavoriteFoodDto), ResponseCode.FOOD_CREATE_SUCCESS.getMessage());
        String json = mapper.writeValueAsString(createFoodFromFavoriteFoodDto);


        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/food/favorite/createfrom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(expectedResponse.getData()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));
    }
}
