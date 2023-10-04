package com.diareat.diareat.food.service;

import com.diareat.diareat.food.domain.Food;
import com.diareat.diareat.food.dto.CreateFoodDto;
import com.diareat.diareat.food.dto.ResponseFoodDto;
import com.diareat.diareat.food.repository.FavoriteFoodRepository;
import com.diareat.diareat.food.repository.FoodRepository;
import com.diareat.diareat.user.domain.User;
import com.diareat.diareat.user.dto.ResponseResearchUserDto;
import com.diareat.diareat.user.repository.UserRepository;
import com.diareat.diareat.util.api.ResponseCode;
import com.diareat.diareat.util.exception.FoodException;
import com.diareat.diareat.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FoodService {

    private final FoodRepository foodRepository; // 유저:음식은 1:다
    private final FavoriteFoodRepository favoriteFoodRepository; // 유저:즐찾음식은 1:다

    private final UserRepository userRepository;

    // 촬영 후, 음식 정보 저장
    @Transactional
    public Long saveFood(CreateFoodDto createFoodDto) {
        User user = userRepository.findById(createFoodDto.getUserId())
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
        Food food = Food.createFood(createFoodDto.getName(), user, createFoodDto.getBaseNutrition());
        return foodRepository.save(food).getId();
    }

    // 회원이 특정 날짜에 먹은 음식 반환
    @Transactional
    public List<ResponseFoodDto> getFoodListByDate(Long userId, LocalDate date){
        List<Food> foodList = foodRepository.findAllByUserIdAndDate(userId, date);
        return foodList.stream()
                .map(food -> ResponseFoodDto.of(food.getId(), food.getUser().getId(), food.getName(), food.getDate(), food.getTime(), food.getBaseNutrition(), food.isFavorite())).collect(Collectors.toList());
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
