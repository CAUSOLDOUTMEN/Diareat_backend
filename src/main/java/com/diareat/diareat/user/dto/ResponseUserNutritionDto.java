package com.diareat.diareat.user.dto;

import com.diareat.diareat.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseUserNutritionDto {

    private int calorie;
    private int carbohydrate;
    private int protein;
    private int fat;

    public static ResponseUserNutritionDto of(int calorie, int carbohydrate, int protein, int fat) {
        return new ResponseUserNutritionDto(calorie, carbohydrate, protein, fat);
    }

    public static ResponseUserNutritionDto from(User user) {
        return new ResponseUserNutritionDto(user.getBaseNutrition().getKcal(), user.getBaseNutrition().getCarbohydrate(), user.getBaseNutrition().getProtein(), user.getBaseNutrition().getFat());
    }
}
