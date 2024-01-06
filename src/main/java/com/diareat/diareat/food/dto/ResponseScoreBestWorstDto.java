package com.diareat.diareat.food.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseScoreBestWorstDto { // 일기 분석 자세히보기에 사용되는 DTO

    private double calorieScore;
    private double carbohydrateScore;
    private double proteinScore;
    private double fatScore;
    private double totalScore;
    private List<ResponseSimpleFoodDto> best;
    private List<ResponseSimpleFoodDto> worst;

    public static ResponseScoreBestWorstDto of(double calorieScore, double carbohydrateScore, double proteinScore, double fatScore, double totalScore, List<ResponseSimpleFoodDto> best, List<ResponseSimpleFoodDto> worst) {
        return new ResponseScoreBestWorstDto(calorieScore, carbohydrateScore, proteinScore, fatScore, totalScore, best, worst);
    }
}
