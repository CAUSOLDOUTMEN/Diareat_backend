package com.diareat.diareat.service;

import com.diareat.diareat.user.dto.*;
import com.diareat.diareat.user.repository.FollowRepository;
import com.diareat.diareat.user.repository.UserRepository;
import com.diareat.diareat.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowRepository followRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        followRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        followRepository.deleteAll();
    }

    @Test
    void saveUser() {
        // given
        Long userId = userService.saveUser(CreateUserDto.of("testUser", "testPassword", 0, 75, 1, 25));

        // when
        ResponseUserDto responseUserDto = userService.getUserInfo(userId);

        // then
        assertNotNull(responseUserDto);
        assertEquals("testUser", responseUserDto.getName());
        assertEquals(25, responseUserDto.getAge());
    }

    @Test
    void getSimpleUserInfo() {
        // given
        Long userId = userService.saveUser(CreateUserDto.of("testUser", "testPassword", 0, 75, 1, 25));

        // when
        ResponseSimpleUserDto responseSimpleUserDto = userService.getSimpleUserInfo(userId);

        // then
        assertEquals("testUser", responseSimpleUserDto.getName());
    }

    @Test
    void getUserInfo() {
        // given
        Long userId = userService.saveUser(CreateUserDto.of("testUser", "testPassword", 0, 75, 1, 25));

        // when
        ResponseUserDto responseUserDto = userService.getUserInfo(userId);

        // then
        assertEquals("testUser", responseUserDto.getName());
    }

    @Test
    void updateUserInfo() {
        // given
        Long userId = userService.saveUser(CreateUserDto.of("testUser", "testPassword", 0, 160, 50, 25));
        UpdateUserDto updateUserDto = UpdateUserDto.of(userId, "updateUser", 180, 75, 25, false);

        // when
        userService.updateUserInfo(updateUserDto);

        // then
        assertEquals("updateUser", userService.getUserInfo(userId).getName());
        assertEquals(180, userService.getUserInfo(userId).getHeight());
    }

    @Test
    void getUserNutrition() { // 임시 코드 사용, 추후 로직 개편 시 테스트코드 수정
        // given
        Long userId = userService.saveUser(CreateUserDto.of("testUser", "testPassword", 0, 160, 50, 25));

        // when
        ResponseUserNutritionDto responseUserNutritionDto = userService.getUserNutrition(userId);

        // then
        assertEquals(0, responseUserNutritionDto.getCalorie());
    }

    @Test
    void updateBaseNutrition() {
        // given
        Long userId = userService.saveUser(CreateUserDto.of("testUser", "testPassword", 0, 160, 50, 25));

        // when
        UpdateUserNutritionDto updateUserNutritionDto = UpdateUserNutritionDto.of(userId, 2000, 300, 80, 80);
        userService.updateBaseNutrition(updateUserNutritionDto);

        // then
        assertEquals(2000, userService.getUserNutrition(userId).getCalorie());
    }

    @Test
    void deleteUser() {
        // given
        Long id = userService.saveUser(CreateUserDto.of("testUser", "testPassword", 0, 75, 1, 25));

        // when
        userService.deleteUser(id);

        // then
        assertFalse(userRepository.existsById(id));
    }

    @Test
    void searchUser() {
        // given
        userService.saveUser(CreateUserDto.of("user1", "testPassword", 0, 175, 80, 25));
        Long id = userService.saveUser(CreateUserDto.of("user2", "testPassword", 0, 175, 80, 25));
        String name = "user";

        // then
        assertEquals(2, userService.searchUser(id, name).size());
    }

    @Test
    void followUser() {
        // given
        Long id1 = userService.saveUser(CreateUserDto.of("testUser", "testPassword", 0, 75, 1, 25));
        Long id2 = userService.saveUser(CreateUserDto.of("followUser", "testPassword", 0, 75, 1, 25));

        // when
        userService.followUser(id2, id1);

        // then
        assertEquals(1, followRepository.findAllByFromUser(id1).size());
    }

    @Test
    void unfollowUser() {
        // given
        Long id1 = userService.saveUser(CreateUserDto.of("testUser", "testPassword", 0, 175, 1, 25));
        Long id2 = userService.saveUser(CreateUserDto.of("followUser", "testPassword", 0, 175, 1, 25));

        // when
        userService.followUser(id1, id2);
        userService.unfollowUser(id1, id2);

        // then
        assertEquals(0, followRepository.findAllByFromUser(id1).size());
    }
}