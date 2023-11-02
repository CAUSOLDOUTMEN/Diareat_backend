package com.diareat.diareat.food.dto;

import com.diareat.diareat.user.domain.BaseNutrition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateFoodDto {

    private Long userId;
    private String name;
    private BaseNutrition baseNutrition;
    private LocalDate date;

    public static CreateFoodDto of(Long userId, String name, BaseNutrition baseNutrition, LocalDate date) {
        return new CreateFoodDto(userId, name, baseNutrition, date);
    }
}
