package com.diareat.diareat.food.dto;

import com.diareat.diareat.food.domain.FavoriteFood;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseFavoriteFoodDto {

    private Long favoriteFoodId;
    private String name;
    private String baseNutrition;
    private int count;

    public static ResponseFavoriteFoodDto of(Long favoriteFoodId, String name, String baseNutrition, int count) {
        return new ResponseFavoriteFoodDto(favoriteFoodId, name, baseNutrition, count);
    }

    public static ResponseFavoriteFoodDto from(FavoriteFood favoriteFood) {
        return new ResponseFavoriteFoodDto(
                favoriteFood.getId(),
                favoriteFood.getName(),
                favoriteFood.getBaseNutrition().toString(),
                favoriteFood.getCount()
        );
    }
}
