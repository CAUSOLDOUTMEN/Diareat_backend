package com.diareat.diareat.user.service;

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
import com.diareat.diareat.util.UserTypeUtil;
import com.diareat.diareat.util.api.ResponseCode;
import com.diareat.diareat.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    // 회원정보 저장
    @Transactional
    public Long saveUser(CreateUserDto createUserDto) {
        if (userRepository.existsByName(createUserDto.getName())) {
            log.info("이미 존재하는 닉네임입니다 by {}", createUserDto.getName());
            throw new UserException(ResponseCode.USER_NAME_ALREADY_EXIST);
        }
        if (userRepository.existsByKeyCode(createUserDto.getKeyCode())) {
            log.info("이미 존재하는 " +
                    "con키코드입니다 by {}", createUserDto.getKeyCode());
            throw new UserException(ResponseCode.USER_ALREADY_EXIST);
        }

        int type = UserTypeUtil.decideUserType(createUserDto.getGender(), createUserDto.getAge());
        log.info("회원 타입: {}", type);
        List<Integer> standard = UserTypeUtil.getStanardByUserType(type); // 유저 타입에 따른 기본 기준섭취량 조회
        BaseNutrition baseNutrition = BaseNutrition.createNutrition(standard.get(0), standard.get(2), createUserDto.getWeight(), standard.get(1)); // 단백질은 자신 체중 기준으로 계산
        User user = User.createUser(createUserDto.getName(), createUserDto.getImage(), createUserDto.getKeyCode(), createUserDto.getHeight(), createUserDto.getWeight(), createUserDto.getGender(), createUserDto.getAge(), baseNutrition);
        return userRepository.save(user).getId();
    }

    // 회원 기본정보 조회
    // @Cacheable(value = "ResponseSimpleUserDto", key = "#userId", cacheManager = "diareatCacheManager")
    @Transactional(readOnly = true)
    public ResponseSimpleUserDto getSimpleUserInfo(Long userId) {
        User user = getUserById(userId);
        log.info("{} 회원 기본정보 조회 완료: ", user.getName());
        return ResponseSimpleUserDto.builder()
                .name(user.getName())
                .image(user.getImage())
                .build();
    }

    // 회원정보 조회
    // @Cacheable(value = "ResponseUserDto", key = "#userId", cacheManager = "diareatCacheManager")
    @Transactional(readOnly = true)
    public ResponseUserDto getUserInfo(Long userId) {
        User user = getUserById(userId);
        log.info("{} 회원정보 조회 완료: ", user.getName());
        return ResponseUserDto.builder()
                .name(user.getName())
                .height(user.getHeight())
                .weight(user.getWeight())
                .age(user.getAge())
                .build();
    }

    // 회원정보 수정
    // @CacheEvict(value = {"ResponseSimpleUserDto", "ResponseUserDto"}, key = "#updateUserDto.getUserId()", cacheManager = "diareatCacheManager")
    @Transactional
    public void updateUserInfo(UpdateUserDto updateUserDto) {
        User user = getUserById(updateUserDto.getUserId());
        log.info("{} 회원정보 조회 완료: ", user.getName());
        user.updateUser(updateUserDto.getName(), updateUserDto.getHeight(), updateUserDto.getWeight(), updateUserDto.getAge(), updateUserDto.getAutoUpdateNutrition());
        userRepository.save(user);
        log.info("{} 회원정보 수정 완료: ", user.getName());
    }

    // 회원 기준섭취량 조회
    // @Cacheable(value = "ResponseUserNutritionDto", key = "#userId", cacheManager = "diareatCacheManager")
    @Transactional(readOnly = true)
    public ResponseUserNutritionDto getUserNutrition(Long userId) {
        User user = getUserById(userId);
        log.info("{} user 객체 조회 완료: ", user.getName());
        return ResponseUserNutritionDto.builder()
                .calorie(user.getBaseNutrition().getKcal())
                .carbohydrate(user.getBaseNutrition().getCarbohydrate())
                .protein(user.getBaseNutrition().getProtein())
                .fat(user.getBaseNutrition().getFat())
                .build();
    }

    // 회원 기준섭취량 직접 수정
    // @CacheEvict(value = "ResponseUserNutritionDto", key = "#updateUserNutritionDto.getUserId()", cacheManager = "diareatCacheManager")
    @Transactional
    public void updateBaseNutrition(UpdateUserNutritionDto updateUserNutritionDto) {
        User user = getUserById(updateUserNutritionDto.getUserId());
        log.info("{} user 객체 조회 완료: ", user.getName());
        BaseNutrition baseNutrition = BaseNutrition.createNutrition(updateUserNutritionDto.getCalorie(), updateUserNutritionDto.getCarbohydrate(), updateUserNutritionDto.getProtein(), updateUserNutritionDto.getFat());
        user.updateBaseNutrition(baseNutrition);
        userRepository.save(user);
        log.info("{} 회원 기준섭취량 수정 완료: ", user.getName());
    }

    // 회원 탈퇴
    // @CacheEvict(value = {"ResponseSimpleUserDto", "ResponseUserDto", "ResponseUserNutritionDto"}, key = "#userId", cacheManager = "diareatCacheManager")
    @Transactional
    public void deleteUser(Long userId) {
        validateUser(userId);
        userRepository.deleteById(userId);
        log.info("PK {} 회원 탈퇴 완료: ", userId);
    }

    // 회원의 친구 검색 결과 조회 -> 검색 및 팔로우는 굉장히 돌발적으로 이루어질 가능성이 높아 캐시 적용 X
    @Transactional(readOnly = true)
    public List<ResponseSearchUserDto> searchUser(Long hostId, String name) {
        validateUser(hostId);
        log.info("{} 회원 검증 완료", hostId);
        List<User> users = new ArrayList<>(userRepository.findAllByNameContaining(name));
        log.info("{} 검색 결과 조회 완료", name);
        users.removeIf(user -> user.getId().equals(hostId)); // 검색 결과에서 자기 자신은 제외 (removeIf 메서드는 ArrayList에만 존재)
        return users.stream()
                .map(user -> ResponseSearchUserDto.builder()
                        .userId(user.getId())
                        .name(user.getName())
                        .image(user.getImage())
                        .follow(followRepository.existsByFromUserAndToUser(hostId, user.getId()))
                        .build()).collect(Collectors.toList());
    }

    // 회원이 특정 회원 팔로우
    @Transactional
    public void followUser(Long fromId, Long toId) {
        validateUser(fromId);
        validateUser(toId);
        log.info("팔로우 대상 검증 완료");
        // 이미 팔로우 중인 경우
        if (followRepository.existsByFromUserAndToUser(fromId, toId)) {
            log.info("{}는 이미 {}를 팔로우한 상태입니다.", fromId, toId);
            throw new UserException(ResponseCode.FOLLOWED_ALREADY);
        }
        followRepository.save(Follow.makeFollow(toId, fromId));
        log.info("이제 {}가 {}를 팔로우합니다.", fromId, toId);
    }

    // 회원이 특정 회원 팔로우 취소
    @Transactional
    public void unfollowUser(Long fromId, Long toId) {
        validateUser(fromId);
        validateUser(toId);
        log.info("팔로우 대상 검증 완료");
        // 이미 팔로우 취소한 경우
        if (!followRepository.existsByFromUserAndToUser(fromId, toId)) {
            log.info("{}는 이미 {}를 팔로우 취소한 상태입니다.", fromId, toId);
            throw new UserException(ResponseCode.UNFOLLOWED_ALREADY);
        }
        followRepository.deleteByFromUserAndToUser(fromId, toId);
        log.info("이제 {}가 {}를 언팔로우합니다.", fromId, toId);
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
