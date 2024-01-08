package com.diareat.diareat.food.dto;

import com.diareat.diareat.food.domain.Food;
import com.diareat.diareat.user.domain.BaseNutrition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFoodDto {

    private Long foodId;
    private Long userId;
    private String name;
    private BaseNutrition baseNutrition;
    private boolean favoriteChecked;
    private int hour;
    private int minute;

    public static ResponseFoodDto of(Long foodId, Long userId, String name, BaseNutrition baseNutrition, boolean favoriteChecked, int hour, int minute) {
        return ResponseFoodDto.builder()
                .foodId(foodId)
                .userId(userId)
                .name(name)
                .baseNutrition(baseNutrition)
                .favoriteChecked(favoriteChecked)
                .hour(hour)
                .minute(minute)
                .build();
    }

    public static ResponseFoodDto from(Food food) {
        return ResponseFoodDto.builder()
                .foodId(food.getId())
                .userId(food.getUser().getId())
                .name(food.getName())
                .baseNutrition(food.getBaseNutrition())
                .favoriteChecked(food.isFavorite())
                .hour(food.getAddedTime().getHour())
                .minute(food.getAddedTime().getMinute())
                .build();
    }
}
