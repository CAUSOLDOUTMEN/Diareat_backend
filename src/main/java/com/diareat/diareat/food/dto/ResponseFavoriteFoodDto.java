package com.diareat.diareat.food.dto;

import com.diareat.diareat.food.domain.FavoriteFood;
import com.diareat.diareat.user.domain.BaseNutrition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFavoriteFoodDto {

    private Long favoriteFoodId;
    private String name;
    private BaseNutrition baseNutrition;
    private int count;

    public static ResponseFavoriteFoodDto of(Long favoriteFoodId, String name, BaseNutrition baseNutrition, int count) {
        return new ResponseFavoriteFoodDto(favoriteFoodId, name, baseNutrition, count);
    }

    public static ResponseFavoriteFoodDto from(FavoriteFood favoriteFood) {
        return new ResponseFavoriteFoodDto(
                favoriteFood.getId(),
                favoriteFood.getName(),
                favoriteFood.getBaseNutrition(),
                favoriteFood.getCount()
        );
    }
}
