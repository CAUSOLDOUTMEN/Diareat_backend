package com.diareat.diareat.food.dto;

import com.diareat.diareat.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateFoodFromFavoriteFoodDto {
    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long favoriteFoodId;

    public static CreateFoodFromFavoriteFoodDto of(Long userId, Long favoriteFoodId) {
        return new CreateFoodFromFavoriteFoodDto(userId, favoriteFoodId);
    }
}
