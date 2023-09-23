package com.diareat.diareat.food.service;

import com.diareat.diareat.food.repository.FavoriteFoodRepository;
import com.diareat.diareat.food.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FoodService {

    private final FoodRepository foodRepository; // 유저:음식은 1:다
    private final FavoriteFoodRepository favoriteFoodRepository; // 유저:즐찾음식은 1:다

    // 촬영 후, 음식 정보 저장
    public Long saveFood() {
        return null;
    }

    // 음식 정보 수정
    public void updateFood() {

    }

    // 음식 삭제
    public void deleteFood() {

    }

    // 즐겨찾기에 음식 저장
    public Long saveFavoriteFood() {
        return null;
    }

    // 즐겨찾기 음식 수정
    public void updateFavoriteFood() {

    }

    // 즐겨찾기 해제
    public void deleteFavoriteFood() {

    }

    /**
     * 메서드 구현 유의사항
     * 1. 메서드명은 동사로 시작
     * 2. 유효성 검사는 private void validateUser의 형태로 분리
     * 3. Repository에서 가져오고 OrElseThrow로 예외처리하는 부분이 반복되는 경우 private ??? get???ById 형태 메서드로 분리
     */
}
