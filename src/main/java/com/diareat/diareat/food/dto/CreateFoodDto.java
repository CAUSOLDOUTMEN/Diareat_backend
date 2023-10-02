package com.diareat.diareat.food.dto;

import com.diareat.diareat.user.domain.BaseNutrition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateFoodDto {

    private Long userId;
    private String name;
    private BaseNutrition baseNutrition;

    public static CreateFoodDto of(Long userId, String name, BaseNutrition baseNutrition) {
        return new CreateFoodDto(userId, name, baseNutrition);
    }
}
