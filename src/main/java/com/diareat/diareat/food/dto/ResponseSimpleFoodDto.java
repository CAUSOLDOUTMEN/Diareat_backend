package com.diareat.diareat.food.dto;

import com.diareat.diareat.food.domain.Food;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSimpleFoodDto { // Best 3 and Worst 3에 사용될 객체

    private String name;
    private double calorie;
    private double carbohydrate;
    private double protein;
    private double fat;
    private LocalDate date;

    public static ResponseSimpleFoodDto of(String name, double calorie, double carbohydrate, double protein, double fat, LocalDate date) {
        return new ResponseSimpleFoodDto(name, calorie, carbohydrate, protein, fat, date);
    }

    public static ResponseSimpleFoodDto from(Food food) {
        return new ResponseSimpleFoodDto(food.getName(), food.getBaseNutrition().getKcal(), food.getBaseNutrition().getCarbohydrate(),
                food.getBaseNutrition().getProtein(), food.getBaseNutrition().getFat(), food.getDate());
    }
}
