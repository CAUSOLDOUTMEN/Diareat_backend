package com.diareat.diareat.food.dto;

import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateFoodDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String name;

    @NotNull(message = MessageUtil.NOT_NULL)
    private BaseNutrition baseNutrition;

    @PastOrPresent(message = MessageUtil.PAST_OR_PRESENT)
    private LocalDate date;

    public static CreateFoodDto of(Long userId, String name, BaseNutrition baseNutrition, LocalDate date) {
        return new CreateFoodDto(userId, name, baseNutrition, date);
    }
}
