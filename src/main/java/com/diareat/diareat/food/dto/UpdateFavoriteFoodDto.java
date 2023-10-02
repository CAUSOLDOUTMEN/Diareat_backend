package com.diareat.diareat.food.dto;

import com.diareat.diareat.user.domain.BaseNutrition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFavoriteFoodDto {

    private Long favoriteFoodId;
    private String name;
    private BaseNutrition baseNutrition;

    public static UpdateFavoriteFoodDto of(Long favoriteFoodId, String name, BaseNutrition baseNutrition) {
        return new UpdateFavoriteFoodDto(favoriteFoodId, name, baseNutrition);
    }
}
