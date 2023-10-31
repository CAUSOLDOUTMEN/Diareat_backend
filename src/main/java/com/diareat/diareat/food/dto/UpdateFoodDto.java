package com.diareat.diareat.food.dto;

import com.diareat.diareat.user.domain.BaseNutrition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFoodDto {

    private Long foodId;
    private Long userId;
    private String name;
    private BaseNutrition baseNutrition;

    public static UpdateFoodDto of(Long foodId, Long userId, String name, BaseNutrition baseNutrition) {
        return new UpdateFoodDto(foodId, userId, name, baseNutrition);
    }
}
