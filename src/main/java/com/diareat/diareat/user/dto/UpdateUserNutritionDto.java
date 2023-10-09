package com.diareat.diareat.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserNutritionDto {

    private Long userId;
    private int calorie;
    private int carbohydrate;
    private int protein;
    private int fat;

    public static UpdateUserNutritionDto of(Long userId, int calorie, int carbohydrate, int protein, int fat) {
        return new UpdateUserNutritionDto(userId, calorie, carbohydrate, protein, fat);
    }
}
