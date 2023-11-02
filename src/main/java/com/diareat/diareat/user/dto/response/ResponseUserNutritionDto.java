package com.diareat.diareat.user.dto.response;

import com.diareat.diareat.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseUserNutritionDto {

    private int calorie; // 유저가 설정한(할 수 있는) 현재 영양소 기준섭취량
    private int carbohydrate;
    private int protein;
    private int fat;

    private int defaultCalorie; // 개인정보에 따라 기본적으로 설정되는 영양소 기준섭취량
    private int defaultCarbohydrate;
    private int defaultProtein;
    private int defaultFat;

    public static ResponseUserNutritionDto of(int calorie, int carbohydrate, int protein, int fat, int defaultCalorie, int defaultCarbohydrate, int defaultProtein, int defaultFat) {
        return new ResponseUserNutritionDto(calorie, carbohydrate, protein, fat, defaultCalorie, defaultCarbohydrate, defaultProtein, defaultFat);
    }

    public static ResponseUserNutritionDto from(User user, int defaultCalorie, int defaultCarbohydrate, int defaultProtein, int defaultFat) {
        return new ResponseUserNutritionDto(user.getBaseNutrition().getKcal(), user.getBaseNutrition().getCarbohydrate(),
                user.getBaseNutrition().getProtein(), user.getBaseNutrition().getFat(), defaultCalorie, defaultCarbohydrate, defaultProtein, defaultFat);
    }
}
