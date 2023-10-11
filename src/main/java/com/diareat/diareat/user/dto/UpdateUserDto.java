package com.diareat.diareat.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    private Long userId;
    private String name;
    private int height;
    private int weight;
    private int age;
    private boolean isAutoUpdateNutrition; // 개인정보를 활용한 기준 영양소 자동계산 여부

    public static UpdateUserDto of(Long userId, String userName, int userHeight, int userWeight, int userAge, boolean isAutoUpdateNutrition) {
        return new UpdateUserDto(userId, userName, userHeight, userWeight, userAge, isAutoUpdateNutrition);
    }
}
