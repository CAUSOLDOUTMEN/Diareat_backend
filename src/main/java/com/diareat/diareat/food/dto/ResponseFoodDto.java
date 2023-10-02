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
    private LocalDate date;
    private LocalTime time;
    private BaseNutrition baseNutrition;

    public static ResponseFoodDto of(Long foodId, Long userId, String name, LocalDate date, LocalTime time, BaseNutrition baseNutrition) {
        return new ResponseFoodDto(foodId, userId, name, date, time, baseNutrition);
    }

    public static ResponseFoodDto from(Food food) {
        return new ResponseFoodDto(food.getId(), food.getUser().getId(), food.getName(), food.getDate(), food.getTime(), food.getBaseNutrition());
    }
}
