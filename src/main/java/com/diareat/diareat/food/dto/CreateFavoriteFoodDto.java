package com.diareat.diareat.food.dto;

import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateFavoriteFoodDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long foodId;

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String name;

    @NotNull(message = MessageUtil.NOT_NULL)
    private BaseNutrition baseNutrition;

    public static CreateFavoriteFoodDto of(Long foodId, Long userId, String name, BaseNutrition baseNutrition) {
        return new CreateFavoriteFoodDto(foodId, userId, name, baseNutrition);
    }
}
