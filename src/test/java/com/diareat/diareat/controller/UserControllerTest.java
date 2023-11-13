package com.diareat.diareat.controller;

import com.diareat.diareat.auth.service.KakaoAuthService;
import com.diareat.diareat.user.controller.UserController;
import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.domain.User;
import com.diareat.diareat.user.dto.request.SearchUserDto;
import com.diareat.diareat.user.dto.request.UpdateUserDto;
import com.diareat.diareat.user.dto.request.UpdateUserNutritionDto;
import com.diareat.diareat.user.dto.response.ResponseSearchUserDto;
import com.diareat.diareat.user.dto.response.ResponseSimpleUserDto;
import com.diareat.diareat.user.dto.response.ResponseUserDto;
import com.diareat.diareat.user.dto.response.ResponseUserNutritionDto;
import com.diareat.diareat.user.service.UserService;
import com.diareat.diareat.util.MessageUtil;
import com.diareat.diareat.util.api.ApiResponse;
import com.diareat.diareat.util.api.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc  mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;

    @MockBean
    private KakaoAuthService kakaoAuthService;

    private final Long testUserId = 1L;
    private final ObjectMapper mapper = new ObjectMapper();
    private final User testUser = User.createUser("test", "test","test", 180, 70, 0, 20, BaseNutrition.createNutrition(2000, 300, 80, 80));

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        testUser.setId(testUserId);
        when(userService.getSimpleUserInfo(testUserId)).thenReturn(ResponseSimpleUserDto.of(testUser.getName(), testUser.getImage()));
        when(userService.getUserInfo(testUserId)).thenReturn(ResponseUserDto.from(testUser));
    }

    @DisplayName("회원 기본정보 조회")
    @Test
    @WithMockUser("test")
    void testGetSimpleUserInfo() throws Exception {
        // Given
        ApiResponse<ResponseSimpleUserDto> expectedResponse = ApiResponse.success(
                ResponseSimpleUserDto.of(testUser.getName(), testUser.getImage()), ResponseCode.USER_CREATE_SUCCESS.getMessage());

        // When & Then
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/user/1/info/simple", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(expectedResponse.getData().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.image").value(expectedResponse.getData().getImage()));
    }

    @DisplayName("회원정보 조회")
    @Test
    @WithMockUser("test")
    void getUserInfo() throws Exception {
        // Given
        ApiResponse<ResponseUserDto> expectedResponse = ApiResponse.success(
                ResponseUserDto.from(testUser), ResponseCode.USER_CREATE_SUCCESS.getMessage());

        //  When & Then
        mockMvc.perform( MockMvcRequestBuilders
            .get("/api/user/1/info", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(expectedResponse.getData().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.height").value(expectedResponse.getData().getHeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.weight").value(expectedResponse.getData().getWeight()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.age").value(expectedResponse.getData().getAge()));
    }

    @DisplayName("회원정보 수정")
    @Test
    @WithMockUser("test")
    void updateUser() throws Exception {
        // Given
        ApiResponse<Void> expectedResponse = ApiResponse.success(null, ResponseCode.USER_UPDATE_SUCCESS.getMessage());
        UpdateUserDto user = UpdateUserDto.of(testUserId, "test2", 170, 80, 21, true);
        String json = mapper.writeValueAsString(user);

        // When & Then
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/api/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));
    }

    @DisplayName("회원정보 수정 - 유효성 검사 실패")
    @Test
    @WithMockUser("test")
    void updateUserFail() throws Exception {
        // Given
        UpdateUserDto user = UpdateUserDto.of(testUserId, "", 300, 80, 500, true);
        String json = mapper.writeValueAsString(user);

        // When & Then
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/api/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(ResponseCode.BAD_REQUEST.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(ResponseCode.BAD_REQUEST.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(MessageUtil.NOT_BLANK))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.height").value(MessageUtil.HEIGHT_RANGE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.age").value(MessageUtil.AGE_RANGE));
    }

    @DisplayName("회원 기준섭취량 조회")
    @Test
    @WithMockUser("test")
    void getUserNutrition() throws Exception {
        // Given
        when(userService.getUserNutrition(testUserId)).thenReturn(ResponseUserNutritionDto.of(2000, 300, 300, 80, 1000, 200, 200, 60));
        ApiResponse<ResponseUserNutritionDto> expectedResponse = ApiResponse.success(
               ResponseUserNutritionDto.of(2000, 300, 300, 80, 1000, 200, 200, 60), ResponseCode.USER_READ_SUCCESS.getMessage());

        // When & Then
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/user/1/nutrition", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.calorie").value(expectedResponse.getData().getCalorie()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.protein").value(expectedResponse.getData().getProtein()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.fat").value(expectedResponse.getData().getFat()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.carbohydrate").value(expectedResponse.getData().getCarbohydrate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.defaultCalorie").value(expectedResponse.getData().getDefaultCalorie()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.defaultProtein").value(expectedResponse.getData().getDefaultProtein()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.defaultFat").value(expectedResponse.getData().getDefaultFat()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.defaultCarbohydrate").value(expectedResponse.getData().getDefaultCarbohydrate()));
    }

    @DisplayName("회원 기준섭취량 직접 수정")
    @Test
    @WithMockUser("test")
    void updateUserNutrition() throws Exception {
        // Given
        ApiResponse<Void> expectedResponse = ApiResponse.success(null, ResponseCode.USER_UPDATE_SUCCESS.getMessage());
        UpdateUserNutritionDto nutrition = UpdateUserNutritionDto.of(testUserId, 2000, 300, 300, 80);
        String json = mapper.writeValueAsString(nutrition);

        // When & Then
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/api/user/1/nutrition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));
    }

    @DisplayName("회원 기준섭취량 직접 수정 - 유효성 검사 실패")
    @Test
    @WithMockUser("test")
    void updateUserNutritionFail() throws Exception {
        // Given
        UpdateUserNutritionDto nutrition = UpdateUserNutritionDto.of(testUserId, 12000, -1, 5000, -500);
        String json = mapper.writeValueAsString(nutrition);

        // When & Then
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/api/user/1/nutrition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(ResponseCode.BAD_REQUEST.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(ResponseCode.BAD_REQUEST.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.calorie").value(MessageUtil.CALORIE_RANGE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.protein").value(MessageUtil.PROTEIN_RANGE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.fat").value(MessageUtil.FAT_RANGE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.carbohydrate").value(MessageUtil.CARBOHYDRATE_RANGE));
    }
    @DisplayName("회원의 친구 검색 결과 조회")
    @Test
    @WithMockUser("test")
    void searchUser() throws Exception {
        // Given
        when(userService.searchUser(testUserId, "test")).thenReturn(List.of(ResponseSearchUserDto.of(2L, "test2", "test2", true)));
        ApiResponse<List<ResponseSearchUserDto>> expectedResponse = ApiResponse.success(
                List.of(ResponseSearchUserDto.of(2L, "test2", "test2", true)), ResponseCode.USER_SEARCH_SUCCESS.getMessage());
        SearchUserDto searchDto = SearchUserDto.of(testUserId, "test");
        String json = mapper.writeValueAsString(searchDto);

        // When & Then
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/user/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].userId").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(expectedResponse.getData().get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].image").value(expectedResponse.getData().get(0).getImage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].follow").value(expectedResponse.getData().get(0).isFollow()));
    }

    @DisplayName("회원의 친구 검색 결과 조회 - 유효성 검사 실패")
    @Test
    @WithMockUser("test")
    void searchUserFail() throws Exception {
        // Given
        SearchUserDto searchDto = SearchUserDto.of(testUserId, "");
        String json = mapper.writeValueAsString(searchDto);

        // When & Then
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/user/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(ResponseCode.BAD_REQUEST.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(ResponseCode.BAD_REQUEST.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.inputName").value(MessageUtil.NOT_BLANK));
    }

    @DisplayName("회원이 특정 회원 팔로우 및 팔로우 취소")
    @Test
    @WithMockUser("test")
    void followUser() throws Exception {
        // Given
        ApiResponse<Void> expectedResponse = ApiResponse.success(null, ResponseCode.USER_FOLLOW_SUCCESS.getMessage());

        // When & Then
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/api/user/1/follow/2")
                        //.delete("/api/user/1/follow/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.code").value(expectedResponse.getHeader().getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.message").value(expectedResponse.getHeader().getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value(expectedResponse.getMsg()));
    }
}
