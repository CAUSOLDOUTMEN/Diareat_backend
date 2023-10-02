package com.diareat.diareat.service;

import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.dto.CreateUserDto;
import com.diareat.diareat.user.dto.ResponseResearchUserDto;
import com.diareat.diareat.user.dto.ResponseUserDto;
import com.diareat.diareat.user.dto.UpdateUserDto;
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
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testSaveAndGetUserInfo() { // 회원정보 저장 및 조회
        // given
        Long userId = 1L;

        // when
        userService.saveUser(CreateUserDto.of("testUser", "testPassword", 180, 75, 1, 25));
        ResponseUserDto responseUserDto = userService.getUserInfo(userId);

        // 검증: 올바른 결과를 반환하는지 확인
        assertNotNull(responseUserDto);
        assertEquals("testUser", responseUserDto.getName());
    }

    @Test
    void testUpdateUserInfo() { // 회원정보 수정

        // given
        Long id = userService.saveUser(CreateUserDto.of("1", "testPassword", 180, 75, 1, 25));
        BaseNutrition baseNutrition = BaseNutrition.createNutrition(2000, 300, 80, 80);

        // when
        UpdateUserDto updateUserDto = UpdateUserDto.of(id, "2", 185, 70, 75, baseNutrition);
        userService.updateUserInfo(updateUserDto);
        ResponseUserDto responseUserDto = userService.getUserInfo(id);

        // then
        assertNotNull(responseUserDto);
        assertEquals("2", responseUserDto.getName());
        assertEquals(185, responseUserDto.getHeight());
        assertEquals(70, responseUserDto.getWeight());
    }

    @Test
    void testDeleteUser() { // 회원 탈퇴
        // given
        Long id = userService.saveUser(CreateUserDto.of("testUser", "testPassword", 180, 75, 1, 25));

        // when
        userService.deleteUser(id);

        // then
        assertNull(userRepository.findById(id).orElse(null));
    }

    @Test
    void testSearchUserName() {
        // given
        userService.saveUser(CreateUserDto.of("testUser", "testPassword", 180, 75, 1, 25));
        userService.saveUser(CreateUserDto.of("hello", "testPassword", 180, 75, 1, 25));

        // when
        String name = "testUser";
        List<ResponseResearchUserDto> users = userService.searchUser(name);

        // then
        assertEquals(1, users.size());
    }

    @Test
    void testFollowUser() { // 회원이 특정 회원 팔로우
        // given
        Long id1 = userService.saveUser(CreateUserDto.of("testUser", "testPassword", 180, 75, 1, 25));
        Long id2 = userService.saveUser(CreateUserDto.of("followUser", "testPassword", 180, 75, 1, 25));

        // when
        userService.followUser(id1, id2);

        // then
        assertEquals(1, userRepository.findById(id1).get().getFollowings().size());
    }

    @Test
    void testUnfollowUser() { // 회원이 특정 회원 팔로우 취소
        // given
        Long id1 = userService.saveUser(CreateUserDto.of("testUser", "testPassword", 180, 75, 1, 25));
        Long id2 = userService.saveUser(CreateUserDto.of("followUser", "testPassword", 180, 75, 1, 25));

        // when
        userService.followUser(id1, id2);
        userService.unfollowUser(id1, id2);

        // then
        assertEquals(0, userRepository.findById(id1).get().getFollowings().size());
    }
}
