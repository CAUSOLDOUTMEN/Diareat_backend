package com.diareat.diareat.user.service;

import com.diareat.diareat.food.domain.Food;
import com.diareat.diareat.food.repository.FavoriteFoodRepository;
import com.diareat.diareat.food.repository.FoodRepository;
import com.diareat.diareat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final FavoriteFoodRepository favoriteFoodRepository;

    // 회원정보 조회
    @Transactional(readOnly = true)
    public void getUserInfo(Long userId) {

    }

    // 회원정보 수정
    @Transactional
    public void updateUserInfo(Long userId) {

    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Long userId) {

    }

    // 유저가 특정 날짜에 섭취한 음식 조회
    @Transactional(readOnly = true)
    public List<Food> getFoodByDate(Long userId, String date) { // 클라이언트가 넘길 날짜의 자료형 협의 필요

        return null;
    }

    // 유저가 특정 날짜에 섭취한 음식의 칼탄단지 총합 및 유저의 권장섭취량, 탄/단/지 구성 비율 조회
    @Transactional(readOnly = true)
    public void getFoodNutrientByDate(Long userId, String date) {

    }

    // 유저의 주간 영양소 섭취량 및 탄/단/지 구성 비율 조회
    @Transactional(readOnly = true)
    public void getNutrientInfoByWeek(Long userId, String week) {

    }

    // 유저의 월간 영양소 섭취량 및 탄/단/지 구성 비율 조회
    @Transactional(readOnly = true)
    public void getNutrientInfoByMonth(Long userId, String month) {

    }

    // 최근 7일간 Best 3 음식 조회
    @Transactional(readOnly = true)
    public void getBestThreeFoodByDate(Long userId, String date) {

    }

    // 최근 7일간 Worst 3 음식 조회
    @Transactional(readOnly = true)
    public void getWorstThreeFoodByDate(Long userId, String date) {

    }

    /**
     * 위 메서드 외 누락된 메서드가 존재할 수 있으며, 아래와 같은 추가적인 부가 기능을 구현할 가능성 있음
     * 1. 친구 추가 기능
     * 2. 친구 목록에 나를 포함하여 각 사람의 칼탄단지 섭취량 기준으로 정렬하는 랭킹 기능
     * 3. 주간 미션 기능
     * void라고 적혀있어도 실제 구현시 반환값이 필요하면 Dto를 생성해야 한다.
     */
}
