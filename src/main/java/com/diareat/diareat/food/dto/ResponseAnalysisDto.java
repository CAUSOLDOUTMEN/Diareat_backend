package com.diareat.diareat.food.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ResponseAnalysisDto { // 그래프 + 점수에 사용되는 DTO

    private double totalScore;
    private List<Double> calorieLastSevenDays; // 최근 7일간의 칼로리 (7개 과거부터 나열)
    private List<Double> calorieLastFourWeek; // 최근 4주간의 칼로리 (4개 과거부터 나열)
    private List<Double> carbohydrateLastSevenDays; // 최근 7일간의 탄수화물
    private List<Double> carbohydrateLastFourWeek; // 최근 4주간의 탄수화물
    private List<Double> proteinLastSevenDays; // 최근 7일간의 단백질
    private List<Double> proteinLastFourWeek; // 최근 4주간의 단백질
    private List<Double> fatLastSevenDays; // 최근 7일간의 지방
    private List<Double> fatLastFourWeek; // 최근 4주간의 지방

    public static ResponseAnalysisDto of(double totalScore, List<Double> calorieLastSevenDays, List<Double> calorieLastFourWeek, List<Double> carbohydrateLastSevenDays, List<Double> carbohydrateLastFourWeek, List<Double> proteinLastSevenDays, List<Double> proteinLastFourWeek, List<Double> fatLastSevenDays, List<Double> fatLastFourWeek) {
        return new ResponseAnalysisDto(totalScore, calorieLastSevenDays, calorieLastFourWeek, carbohydrateLastSevenDays, carbohydrateLastFourWeek, proteinLastSevenDays, proteinLastFourWeek, fatLastSevenDays, fatLastFourWeek);
    }
}
