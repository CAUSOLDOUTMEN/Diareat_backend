package com.diareat.diareat.user.dto.request;

import com.diareat.diareat.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserNutritionDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    @DecimalMin(value = "100", message = MessageUtil.CALORIE_RANGE)
    @DecimalMax(value = "10000", message = MessageUtil.CALORIE_RANGE)
    private int calorie;

    @DecimalMin(value = "100", message = MessageUtil.CARBOHYDRATE_RANGE)
    @DecimalMax(value = "500", message = MessageUtil.CARBOHYDRATE_RANGE)
    private int carbohydrate;

    @DecimalMin(value = "25", message = MessageUtil.PROTEIN_RANGE)
    @DecimalMax(value = "500", message = MessageUtil.PROTEIN_RANGE)
    private int protein;

    @DecimalMin(value = "25", message = MessageUtil.FAT_RANGE)
    @DecimalMax(value = "500", message = MessageUtil.FAT_RANGE)
    private int fat;

    public static UpdateUserNutritionDto of(Long userId, int calorie, int carbohydrate, int protein, int fat) {
        return new UpdateUserNutritionDto(userId, calorie, carbohydrate, protein, fat);
    }
}
