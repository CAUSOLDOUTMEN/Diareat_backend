package com.diareat.diareat.food.dto;

import com.diareat.diareat.user.domain.BaseNutrition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateFavoriteFoodDto {

    private Long foodId;
    private Long userId;
    private String name;
    private BaseNutrition baseNutrition;

    public static CreateFavoriteFoodDto of(Long foodId, Long userId, String name, BaseNutrition baseNutrition) {
        return new CreateFavoriteFoodDto(foodId, userId, name, baseNutrition);
    }
}
