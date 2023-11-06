package com.diareat.diareat.food.dto;

import com.diareat.diareat.food.domain.FavoriteFood;
import com.diareat.diareat.user.domain.BaseNutrition;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseFavoriteFoodDto {

    private Long userId;
    private Long favoriteFoodId;
    private String name;
    private BaseNutrition baseNutrition;
    private int count;

    public static ResponseFavoriteFoodDto of(Long userId, Long favoriteFoodId, String name, BaseNutrition baseNutrition, int count) {
        return new ResponseFavoriteFoodDto(userId, favoriteFoodId, name, baseNutrition, count);
    }

    public static ResponseFavoriteFoodDto from(FavoriteFood favoriteFood) {
        return new ResponseFavoriteFoodDto(
                favoriteFood.getUser().getId(),
                favoriteFood.getId(),
                favoriteFood.getName(),
                favoriteFood.getBaseNutrition(),
                favoriteFood.getCount()
        );
    }
}
