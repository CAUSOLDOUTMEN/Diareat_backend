package com.diareat.diareat.user.service;

import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.domain.Follow;
import com.diareat.diareat.user.domain.User;
import com.diareat.diareat.user.dto.*;
import com.diareat.diareat.user.repository.FollowRepository;
import com.diareat.diareat.user.repository.UserRepository;
import com.diareat.diareat.util.UserTypeUtil;
import com.diareat.diareat.util.api.ResponseCode;
import com.diareat.diareat.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
        if (userRepository.existsByName(createUserDto.getName()))
            throw new UserException(ResponseCode.USER_NAME_ALREADY_EXIST);
        if(userRepository.existsByKeyCode(createUserDto.getKeyCode()))
            throw new UserException(ResponseCode.USER_ALREADY_EXIST);

        int type = UserTypeUtil.decideUserType(createUserDto.getGender(), createUserDto.getAge());
        List<Integer> standard = UserTypeUtil.getStanardByUserType(type); // 유저 타입에 따른 기본 기준섭취량 조회
        BaseNutrition baseNutrition = BaseNutrition.createNutrition(standard.get(0), standard.get(2), createUserDto.getWeight(), standard.get(1)); // 단백질은 자신 체중 기준으로 계산
        User user = User.createUser(createUserDto.getName(), createUserDto.getImage(), createUserDto.getKeyCode(), createUserDto.getHeight(), createUserDto.getWeight(), createUserDto.getGender(), createUserDto.getAge(), baseNutrition);
        return userRepository.save(user).getId();
    }

    // 회원 기본정보 조회
    @Cacheable(value = "ResponseSimpleUserDto", key = "#userId", cacheManager = "diareatCacheManager")
    @Transactional(readOnly = true)
    public ResponseSimpleUserDto getSimpleUserInfo(Long userId) {
        User user = getUserById(userId);
        double nutritionScore = 100; // 점수 계산 로직 확정 전 기본값 -> 추후 수정 필요
        return ResponseSimpleUserDto.of(user.getName(), user.getImage(), nutritionScore);
    }

    // 회원정보 조회
    @Cacheable(value = "ResponseUserDto", key = "#userId", cacheManager = "diareatCacheManager")
    @Transactional(readOnly = true)
    public ResponseUserDto getUserInfo(Long userId) {
        User user = getUserById(userId);
        return ResponseUserDto.from(user);
    }

    // 회원정보 수정
    @CacheEvict(value = {"ResponseSimpleUserDto", "ResponseUserDto"}, key = "#updateUserDto.userId", cacheManager = "diareatCacheManager")
    @Transactional
    public void updateUserInfo(UpdateUserDto updateUserDto) {
        User user = getUserById(updateUserDto.getUserId());
        user.updateUser(updateUserDto.getName(), updateUserDto.getHeight(), updateUserDto.getWeight(), updateUserDto.getAge(), updateUserDto.isAutoUpdateNutrition());
        userRepository.save(user);
    }

    // 회원 기준섭취량 조회
    @Cacheable(value = "ResponseUserNutritionDto", key = "#userId", cacheManager = "diareatCacheManager")
    @Transactional(readOnly = true)
    public ResponseUserNutritionDto getUserNutrition(Long userId) {
        User user = getUserById(userId);
        List<Integer> standard = UserTypeUtil.getStanardByUserType(user.getType()); // 유저 타입에 따른 기본 기준섭취량 조회
        return ResponseUserNutritionDto.from(user, standard.get(0), standard.get(2), user.getWeight(), standard.get(1)); // 단백질은 자신 체중 기준으로 계산
    }

    // 회원 기준섭취량 직접 수정
    @CacheEvict(value = "ResponseUserNutritionDto", key = "#updateUserNutritionDto.userId", cacheManager = "diareatCacheManager")
    @Transactional
    public void updateBaseNutrition(UpdateUserNutritionDto updateUserNutritionDto) {
        User user = getUserById(updateUserNutritionDto.getUserId());
        BaseNutrition baseNutrition = BaseNutrition.createNutrition(updateUserNutritionDto.getCalorie(), updateUserNutritionDto.getCarbohydrate(), updateUserNutritionDto.getProtein(), updateUserNutritionDto.getFat());
        user.updateBaseNutrition(baseNutrition);
        userRepository.save(user);
    }

    // 회원 탈퇴
    @CacheEvict(value = {"ResponseSimpleUserDto", "ResponseUserDto", "ResponseUserNutritionDto"}, key = "#userId", cacheManager = "diareatCacheManager")
    @Transactional
    public void deleteUser(Long userId) {
        validateUser(userId);
        userRepository.deleteById(userId);
    }

    // 회원의 친구 검색 결과 조회 -> 검색 및 팔로우는 굉장히 돌발적으로 이루어질 가능성이 높아 캐시 적용 X
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

    private void validateUser(Long userId) {
        if (!userRepository.existsById(userId))
            throw new UserException(ResponseCode.USER_NOT_FOUND);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }
}
