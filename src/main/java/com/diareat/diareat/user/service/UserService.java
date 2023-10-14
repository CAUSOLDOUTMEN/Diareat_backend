package com.diareat.diareat.user.service;

import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.domain.Follow;
import com.diareat.diareat.user.domain.User;
import com.diareat.diareat.user.dto.*;
import com.diareat.diareat.user.repository.FollowRepository;
import com.diareat.diareat.user.repository.UserRepository;
import com.diareat.diareat.util.api.ResponseCode;
import com.diareat.diareat.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    // 회원정보 저장
    @Transactional
    public Long saveUser(CreateUserDto createUserDto) {
        BaseNutrition baseNutrition = BaseNutrition.createNutrition(2000, 300, 80, 80);
        // BaseNutrition baseNutrition = BaseNutrition.createNutrition(createUserDto.getGender(), createUserDto.getAge(), createUserDto.getHeight(), createUserDto.getWeight());
        if (userRepository.existsByName(createUserDto.getName()))
            throw new UserException(ResponseCode.USER_ALREADY_EXIST);
        User user = User.createUser(createUserDto.getName(), createUserDto.getImage(), createUserDto.getKeyCode(), createUserDto.getHeight(), createUserDto.getWeight(), createUserDto.getGender(), createUserDto.getAge(), baseNutrition);
        return userRepository.save(user).getId();
    }

    // 회원 기본정보 조회
    @Transactional(readOnly = true)
    public ResponseSimpleUserDto getSimpleUserInfo(Long userId) {
        User user = getUserById(userId);
        double nutritionScore = 100; // 로직 확정 전에는 임시 값으로 대체
        return ResponseSimpleUserDto.of(user.getName(), user.getImage(), nutritionScore);
    }

    // 회원정보 조회
    @Transactional(readOnly = true)
    public ResponseUserDto getUserInfo(Long userId) {
        User user = getUserById(userId);
        return ResponseUserDto.from(user);
    }

    // 회원정보 수정
    @Transactional
    public void updateUserInfo(UpdateUserDto updateUserDto) {
        User user = getUserById(updateUserDto.getUserId());
        user.updateUser(updateUserDto.getName(), updateUserDto.getHeight(), updateUserDto.getWeight(), updateUserDto.getAge());
    }

    // 회원 기준영양소 조회
    @Transactional(readOnly = true)
    public ResponseUserNutritionDto getUserNutrition(Long userId) {
        User user = getUserById(userId);
        return ResponseUserNutritionDto.from(user);
    }

    // 회원 기준영양소 직접 수정
    @Transactional
    public void updateBaseNutrition(UpdateUserNutritionDto updateUserNutritionDto) {
        User user = getUserById(updateUserNutritionDto.getUserId());
        BaseNutrition baseNutrition = BaseNutrition.createNutrition(updateUserNutritionDto.getCalorie(), updateUserNutritionDto.getCarbohydrate(), updateUserNutritionDto.getProtein(), updateUserNutritionDto.getFat());
        user.updateBaseNutrition(baseNutrition);
        userRepository.save(user);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Long userId) {
        validateUser(userId);
        userRepository.deleteById(userId);
    }

    // 회원의 친구 검색 결과 조회
    @Transactional(readOnly = true)
    public List<ResponseSearchUserDto> searchUser(Long hostId, String name) {
        validateUser(hostId);
        List<User> users = new ArrayList<>(userRepository.findAllByNameContaining(name));
        users.removeIf(user -> user.getId().equals(hostId)); // 검색 결과에서 자기 자신은 제외 (removeIf 메서드는 ArrayList에만 존재)
        return users.stream()
                .map(user -> ResponseSearchUserDto.of(user.getId(), user.getName(), user.getImage(), followRepository.existsByFromUserAndToUser(hostId, user.getId()))).collect(Collectors.toList());
    }

    // 회원이 특정 회원 팔로우
    @Transactional
    public void followUser(Long userId, Long followId) {
        validateUser(userId);
        validateUser(followId);
        // 이미 팔로우 중인 경우
        if (followRepository.existsByFromUserAndToUser(userId, followId))
            throw new UserException(ResponseCode.FOLLOWED_ALREADY);
        followRepository.save(Follow.makeFollow(userId, followId));
    }

    // 회원이 특정 회원 팔로우 취소
    @Transactional
    public void unfollowUser(Long userId, Long unfollowId) {
        validateUser(userId);
        validateUser(unfollowId);
        // 이미 팔로우 취소한 경우
        if (!followRepository.existsByFromUserAndToUser(userId, unfollowId))
            throw new UserException(ResponseCode.UNFOLLOWED_ALREADY);
        followRepository.delete(Follow.makeFollow(userId, unfollowId));
    }

    // 회원의 팔로우 목록 조회 (현재 외부 Dto 변환은 Food에서 위임받아 진행할지 협의하지 않았기에 일단 User 리스트로 반환)
    @Transactional(readOnly = true)
    public List<User> getFollowList(Long userId) {
        validateUser(userId);
        List<User> users = followRepository.findAllByFromUser(userId);
        users.add(getUserById(userId)); // 자기 자신도 랭킹에 포함
        return users;
    }

    private void validateUser(Long userId) {
        if (!userRepository.existsById(userId))
            throw new UserException(ResponseCode.USER_NOT_FOUND);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }
}
