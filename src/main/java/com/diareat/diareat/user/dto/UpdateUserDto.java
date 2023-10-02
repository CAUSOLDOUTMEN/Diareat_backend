package com.diareat.diareat.user.dto;

import com.diareat.diareat.user.domain.BaseNutrition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    private Long id;
    private String name;
    private int height;
    private int weight;
    private int age;
    private BaseNutrition baseNutrition;

    public UpdateUserDto of(Long userId, String userName, int userHeight, int userWeight, int userAge, BaseNutrition userBaseNutrition) {
        return new UpdateUserDto(userId, userName, userHeight, userWeight, userAge, userBaseNutrition);
    }
}
