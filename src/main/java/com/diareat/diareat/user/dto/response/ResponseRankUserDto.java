package com.diareat.diareat.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRankUserDto {

    private Long userId;
    private String name;
    private String image;
    private double calorieScore;
    private double carbohydrateScore;
    private double proteinScore;
    private double fatScore;
    private double totalScore;

    public static ResponseRankUserDto of(Long userId, String name, String image, double calorieScore, double carbohydrateScore, double proteinScore, double fatScore, double totalScore) {
        return new ResponseRankUserDto(userId, name, image, calorieScore, carbohydrateScore, proteinScore, fatScore, totalScore);
    }
}
