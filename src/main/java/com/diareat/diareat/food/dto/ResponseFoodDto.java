package com.diareat.diareat.food.dto;

import com.diareat.diareat.food.domain.Food;
import com.diareat.diareat.user.domain.BaseNutrition;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class ResponseFoodDto {

    private Long foodId;
    private Long userId;
    private String name;
    private BaseNutrition baseNutrition;
    private boolean isFavorite;

    public static ResponseFoodDto of(Long foodId, Long userId, String name, BaseNutrition baseNutrition, boolean isFavorite) {
        return new ResponseFoodDto(foodId, userId, name, baseNutrition, isFavorite);
    }

    public static ResponseFoodDto from(Food food) {
        return new ResponseFoodDto(food.getId(), food.getUser().getId(), food.getName(), food.getBaseNutrition(), food.isFavorite());
    }
}
