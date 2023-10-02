package com.diareat.diareat.user.service;

import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.domain.User;
import com.diareat.diareat.user.dto.CreateUserDto;
import com.diareat.diareat.user.dto.ResponseResearchUserDto;
import com.diareat.diareat.user.dto.ResponseUserDto;
import com.diareat.diareat.user.dto.UpdateUserDto;
import com.diareat.diareat.user.repository.UserRepository;
import com.diareat.diareat.util.api.ResponseCode;
import com.diareat.diareat.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // 회원정보 저장
    @Transactional
    public Long saveUser(CreateUserDto createUserDto) {
        // 나이, 성별, 키, 몸무게로 기준 영양소 계산 (일단 임의의 값으로 설정했고 추후 로직 구성)
        BaseNutrition baseNutrition = BaseNutrition.createNutrition(2000, 300, 80, 80);
        User user = User.createUser(createUserDto.getName(), createUserDto.getKeyCode(), createUserDto.getHeight(), createUserDto.getWeight(), createUserDto.getAge(), createUserDto.getGender(), baseNutrition);
        return userRepository.save(user).getId();
    }

    // 회원정보 조회
    @Transactional(readOnly = true)
    public ResponseUserDto getUserInfo(Long userId) {
        return ResponseUserDto.from(getUserById(userId));
    }

    // 회원정보 수정
    @Transactional
    public void updateUserInfo(UpdateUserDto updateUserDto) {
        User user = getUserById(updateUserDto.getUserId());
        BaseNutrition baseNutrition = BaseNutrition.createNutrition(2000, 300, 80, 80);
        user.updateUser(updateUserDto.getName(), updateUserDto.getHeight(), updateUserDto.getWeight(), updateUserDto.getAge(), baseNutrition);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    // 회원이 팔로우를 위해 검색한 유저 목록 조회
    @Transactional(readOnly = true)
    public List<ResponseResearchUserDto> searchUser(String name) {
        List<User> users = userRepository.findAllByNameContaining(name);
        return users.stream()
                .map(user -> ResponseResearchUserDto.of(user.getId(), user.getName())).collect(Collectors.toList());
    }

    // 회원이 특정 회원 팔로우
    @Transactional
    public void followUser(Long userId, Long followId) {
        User user = getUserById(userId);
        User followUser = getUserById(followId);
        user.followUser(followUser);
    }

    // 회원이 특정 회원 팔로우 취소
    @Transactional
    public void unfollowUser(Long userId, Long unfollowId) {
        User user = getUserById(userId);
        User followUser = getUserById(unfollowId);
        user.unfollowUser(followUser);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }

    /*
     아래 메서드들은 원래 두 패키지 모두에 존재할 수 있는 혼합적인 의미를 가지고 있으며, 일단 FoodService에서 구현을 담당할 예정이다.
     현재 UserService가 음식 관련 Repository에 의존하지 않고 있기에, FoodService에서 구현하는 것이 더 적절하다고 판단했다.

     // 유저의 즐겨찾기 음식 목록 조회
     public void getFavoriteFoodList(Long userId) {

     }

     // 유저의 특정 날짜에 먹은 음식 목록 조회
     public void getFoodListByDate(Long userId, LocalDate date) {

     }

     // 유저의 특정 날짜에 먹은 음식들의 영양성분별 총합 조회 (섭취영양소/기준영양소 및 비율까지 계산해서 반환, dto 구체적 협의 필요)
     public void getNutritionSumByDate(Long userId, LocalDate date) {

     }

     // 유저의 최근 7일간의 영양성분별 총합 조회 (섭취영양소/기준영양소 및 비율까지 계산해서 반환, dto 구체적 협의 필요)
     public void getNutritionSumByWeek(Long userId) {

     }

     // 유저의 최근 1개월간의 영양성분별 총합 조회 (섭취영양소/기준영양소 및 비율까지 계산해서 반환, dto 구체적 협의 필요)
     public void getNutritionSumByMonth(Long userId) {

     }

     // 유저의 최근 7일간의 Best 3 음식 조회 (dto 구체적 협의 필요)
     public void getBestFoodByWeek(Long userId) {

     }

     // 유저의 최근 7일간의 Worst 3 음식 조회 (dto 구체적 협의 필요)
     public void getWorstFoodByWeek(Long userId) {

     }
     */

    /*
     * 위 메서드 외 누락된 메서드가 존재할 수 있으며, UserService에는 아래와 같은 추가적인 부가 기능을 구현할 가능성이 있다.
     * 1. 팔로우 목록에 나를 포함하여 칼탄단지 섭취량 기준으로 건강한 섭취 현황을 분석(?)하여 점수별로 정렬하는 랭킹 기능
     * 2. 주간 과제 기능 (예: 주간에 목표한 섭취량을 달성하면 보상이 주어지는 등)
     */
}
