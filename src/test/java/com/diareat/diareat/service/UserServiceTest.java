package com.diareat.diareat.service;

import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.domain.Follow;
import com.diareat.diareat.user.domain.User;
import com.diareat.diareat.user.dto.request.CreateUserDto;
import com.diareat.diareat.user.dto.request.UpdateUserDto;
import com.diareat.diareat.user.dto.request.UpdateUserNutritionDto;
import com.diareat.diareat.user.dto.response.ResponseSearchUserDto;
import com.diareat.diareat.user.dto.response.ResponseSimpleUserDto;
import com.diareat.diareat.user.dto.response.ResponseUserDto;
import com.diareat.diareat.user.dto.response.ResponseUserNutritionDto;
import com.diareat.diareat.user.repository.FollowRepository;
import com.diareat.diareat.user.repository.UserRepository;
import com.diareat.diareat.user.service.UserService;
import com.diareat.diareat.util.exception.UserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowRepository followRepository;

    @DisplayName("회원정보 저장")
    @Test
    void saveUser() { // setId() 메서드로 테스트 진행함
        // Given
        CreateUserDto createUserDto = CreateUserDto.of("test", "profile.jpg", "testPassword", 0, 75, 1, 25);
        BaseNutrition baseNutrition = BaseNutrition.createNutrition(2000, 300, 80, 80);
        User user = User.createUser(createUserDto.getName(), createUserDto.getImage(), createUserDto.getKeyCode(), createUserDto.getHeight(), createUserDto.getWeight(), createUserDto.getGender(), createUserDto.getAge(), baseNutrition);
        user.setId(1L); // 테스트 커밋 중 User에 setId() 메서드 임시적으로 삽입하여 테스트 진행함

        given(userRepository.existsByName("test")).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(user);

        // When
        Long id = userService.saveUser(createUserDto); // id null로 반환됨 (Mock은 실제 DB에 객체를 생성하지 않기 때문)

        // Then
        assertEquals(1L, id);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @DisplayName("회원정보 저장 닉네임 중복")
    @Test
    void saveUserDupliacedName() {
        // Given
        CreateUserDto createUserDto = CreateUserDto.of("test", "profile.jpg", "testPassword", 0, 75, 1, 25);
        BaseNutrition baseNutrition = BaseNutrition.createNutrition(2000, 300, 80, 80);
        User user = User.createUser(createUserDto.getName(), createUserDto.getImage(), createUserDto.getKeyCode(), createUserDto.getHeight(), createUserDto.getWeight(), createUserDto.getGender(), createUserDto.getAge(), baseNutrition);
        user.setId(1L); // 테스트 커밋 중 User에 setId() 메서드 임시적으로 삽입하여 테스트 진행함

        given(userRepository.existsByName("test")).willReturn(true);

        // When -> 예외처리
        assertThrows(UserException.class, () -> userService.saveUser(createUserDto));
    }

    @DisplayName("회원 기본정보 조회")
    @Test
    void getSimpleUserInfo() {
        // Given
        Long userId = 1L;
        User user = User.createUser("test", "profile.jpg", "keycode123", 175, 70, 0, 30, BaseNutrition.createNutrition(2000, 300, 80, 80));
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // When
        ResponseSimpleUserDto result = userService.getSimpleUserInfo(userId);

        // Then
        assertEquals("test", result.getName());
        assertEquals("profile.jpg", result.getImage());
    }

    @DisplayName("회원정보 조회")
    @Test
    void getUserInfo() {
        // Given
        Long userId = 1L;
        User user = User.createUser("test", "profile.jpg", "keycode123", 175, 70, 0, 30, BaseNutrition.createNutrition(2000, 300, 80, 80));
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // When
        ResponseUserDto result = userService.getUserInfo(userId);

        // Then
        assertEquals("test", result.getName());
        assertEquals(30, result.getAge());
    }

    @DisplayName("회원정보 수정")
    @Test
    void updateUserInfo() {
        // given
        UpdateUserDto updateUserDto = UpdateUserDto.of(1L, "update", 180, 75, 25, 0);
        User user = User.createUser("test", "profile.jpg", "keycode123", 175, 70, 0, 30, BaseNutrition.createNutrition(2000, 300, 80, 80));
        given(userRepository.findById(updateUserDto.getUserId())).willReturn(Optional.of(user));

        // when
        userService.updateUserInfo(updateUserDto);


        // Then
        assertEquals("update", user.getName());
        assertEquals(180, user.getHeight());
        assertEquals(75, user.getWeight());
        assertEquals(25, user.getAge());
    }

    @DisplayName("회원 기준섭취량 조회")
    @Test
    void getUserNutrition() { // 임시 코드 사용, 추후 로직 개편 시 테스트코드 수정
        // Given
        Long userId = 1L;
        User user = User.createUser("test", "profile.jpg", "keycode123", 175, 70, 0, 30, BaseNutrition.createNutrition(2000, 300, 80, 80));
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // When
        ResponseUserNutritionDto result = userService.getUserNutrition(userId);

        // Then
        assertEquals(2000, result.getCalorie());
        assertEquals(300, result.getCarbohydrate());
        assertEquals(80, result.getProtein());
        assertEquals(80, result.getFat());
    }

    @DisplayName("회원 기준섭취량 직접 수정")
    @Test
    void updateBaseNutrition() {
        // Given
        UpdateUserNutritionDto updateUserNutritionDto = UpdateUserNutritionDto.of(1L, 2000, 300, 80, 80);
        // 필드 초기화
        User user = User.createUser("test", "profile.jpg", "keycode123", 175, 70, 0, 30, BaseNutrition.createNutrition(1000, 100, 40, 40));
        given(userRepository.findById(updateUserNutritionDto.getUserId())).willReturn(Optional.of(user));

        // When
        userService.updateBaseNutrition(updateUserNutritionDto);

        // Then
        assertEquals(2000, user.getBaseNutrition().getKcal());
        assertEquals(300, user.getBaseNutrition().getCarbohydrate());
        assertEquals(80, user.getBaseNutrition().getProtein());
        assertEquals(80, user.getBaseNutrition().getFat());
    }

    @DisplayName("회원 탈퇴")
    @Test
    void deleteUser() {
        // Given
        Long userId = 1L;
        given(userRepository.existsById(userId)).willReturn(true);

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository, times(1)).deleteById(userId);
    }

    @DisplayName("회원의 친구 검색 결과 조회")
    @Test
    void searchUser() { // setId() 메서드로 테스트 진행함
        // Given
        Long userId1 = 1L;
        Long userId2 = 2L;
        Long userId3 = 3L;
        String name = "John";

        // 사용자 목록 생성
        User user1 = User.createUser("John", "profile1.jpg", "keycode123", 175, 70, 0, 30, BaseNutrition.createNutrition(2000, 300, 80, 80));
        User user2 = User.createUser("John Doe", "profile2.jpg", "keycode456", 170, 65, 1, 35, BaseNutrition.createNutrition(2000, 300, 80, 80));
        User user3 = User.createUser("John Doo", "profile3.jpg", "keycode789", 160, 55, 1, 25, BaseNutrition.createNutrition(2000, 300, 80, 80));
        user1.setId(userId1);
        user2.setId(userId2);
        user3.setId(userId3);

        given(userRepository.existsById(userId1)).willReturn(true);
        given(userRepository.findAllByNameContaining(name)).willReturn(List.of(user1, user2, user3));
        given(followRepository.existsByFromUserAndToUser(userId1, userId2)).willReturn(true);
        given(followRepository.existsByFromUserAndToUser(userId1, userId3)).willReturn(false);

        // When
        List<ResponseSearchUserDto> result = userService.searchUser(userId1, name);

        // Then
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertTrue(result.get(0).isFollow());
        assertEquals("John Doo", result.get(1).getName());
        assertFalse(result.get(1).isFollow());
        verify(userRepository, times(1)).findAllByNameContaining(name);
    }

    @DisplayName("회원이 특정 회원 팔로우")
    @Test
    void followUser() {
        // Given
        Long userId = 1L; // 팔로우 요청을 보낸 사용자의 ID
        Long followId = 2L; // 팔로우할 사용자의 ID

        given(userRepository.existsById(userId)).willReturn(true); // userId에 해당하는 사용자가 존재함
        given(userRepository.existsById(followId)).willReturn(true); // followId에 해당하는 사용자가 존재함
        given(followRepository.existsByFromUserAndToUser(userId, followId)).willReturn(false); // 아직 팔로우 중이 아님

        // When
        userService.followUser(userId, followId);

        // Then
        verify(userRepository, times(1)).existsById(userId); // 사용자 존재 여부 확인
        verify(userRepository, times(1)).existsById(followId); // 팔로우할 사용자 존재 여부 확인
        verify(followRepository, times(1)).existsByFromUserAndToUser(userId, followId); // 이미 팔로우 중인지 확인
        verify(followRepository, times(1)).save(any(Follow.class));
    }

    @DisplayName("회원이 특정 회원 팔로우 중복 요청")
    @Test
    void followerUserDuplicate() {
        // Given
        Long userId = 1L; // 팔로우 요청을 보낸 사용자의 ID
        Long followId = 2L; // 팔로우할 사용자의 ID

        given(userRepository.existsById(userId)).willReturn(true); // userId에 해당하는 사용자가 존재함
        given(userRepository.existsById(followId)).willReturn(true); // followId에 해당하는 사용자가 존재함
        given(followRepository.existsByFromUserAndToUser(userId, followId)).willReturn(true); // 아직 팔로우 중이 아님

        // When -> 예외처리
        assertThrows(UserException.class, () -> userService.followUser(userId, followId));
    }

    @DisplayName("회원이 특정 회원 팔로우 취소")
    @Test
    void unfollowUser() {
        // Given
        Long userId = 1L; // 팔로우 취소를 요청한 사용자의 ID
        Long unfollowId = 2L; // 팔로우를 취소할 사용자의 ID

        given(userRepository.existsById(userId)).willReturn(true); // userId에 해당하는 사용자가 존재함
        given(userRepository.existsById(unfollowId)).willReturn(true); // unfollowId에 해당하는 사용자가 존재함
        given(followRepository.existsByFromUserAndToUser(userId, unfollowId)).willReturn(true);

        // When
        userService.unfollowUser(userId, unfollowId);

        // Then
        verify(followRepository, times(1)).deleteByFromUserAndToUser(userId, unfollowId);
    }

    @DisplayName("회원이 특정 회원 팔로우 취소 중복 요청")
    @Test
    void unfollowUserDuplicate() {
        // Given
        Long userId = 1L; // 팔로우 취소를 요청한 사용자의 ID
        Long unfollowId = 2L; // 팔로우를 취소할 사용자의 ID

        given(userRepository.existsById(userId)).willReturn(true); // userId에 해당하는 사용자가 존재함
        given(userRepository.existsById(unfollowId)).willReturn(true); // unfollowId에 해당하는 사용자가 존재함
        given(followRepository.existsByFromUserAndToUser(userId, unfollowId)).willReturn(false);

        // When -> 예외처리
        assertThrows(UserException.class, () -> userService.unfollowUser(userId, unfollowId));
    }
}
