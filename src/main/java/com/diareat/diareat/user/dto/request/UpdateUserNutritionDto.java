package com.diareat.diareat.user.dto.request;

import com.diareat.diareat.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserNutritionDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    @Range(min = 100, max = 10000, message = MessageUtil.CALORIE_RANGE)
    private int calorie;

    @Range(min = 100, max = 500, message = MessageUtil.CARBOHYDRATE_RANGE)
    private int carbohydrate;

    @Range(min = 25, max = 500, message = MessageUtil.PROTEIN_RANGE)
    private int protein;

    @Range(min = 25, max = 500, message = MessageUtil.FAT_RANGE)
    private int fat;

    public static UpdateUserNutritionDto of(Long userId, int calorie, int carbohydrate, int protein, int fat) {
        return new UpdateUserNutritionDto(userId, calorie, carbohydrate, protein, fat);
    }
}
