package com.diareat.diareat.food.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAnalysisDto { // 그래프 + 점수에 사용되는 DTO

    private double totalScore;
    private List<Map<LocalDate, Double>> calorieLastSevenDays; // 최근 7일간의 칼로리 (7개 과거부터 나열)
    private List<Double> calorieLastFourWeek; // 최근 4주간의 칼로리 (4개 과거부터 나열)
    private List<Map<LocalDate, Double>> carbohydrateLastSevenDays; // 최근 7일간의 탄수화물
    private List<Double> carbohydrateLastFourWeek; // 최근 4주간의 탄수화물
    private List<Map<LocalDate, Double>> proteinLastSevenDays; // 최근 7일간의 단백질
    private List<Double> proteinLastFourWeek; // 최근 4주간의 단백질
    private List<Map<LocalDate, Double>> fatLastSevenDays; // 최근 7일간의 지방
    private List<Double> fatLastFourWeek; // 최근 4주간의 지방

    public static ResponseAnalysisDto of(double totalScore, List<Map<LocalDate, Double>> calorieLastSevenDays, List<Double> calorieLastFourWeek,List<Map<LocalDate, Double>> carbohydrateLastSevenDays, List<Double> carbohydrateLastFourWeek,List<Map<LocalDate, Double>> proteinLastSevenDays, List<Double> proteinLastFourWeek, List<Map<LocalDate, Double>> fatLastSevenDays, List<Double> fatLastFourWeek) {
        return new ResponseAnalysisDto(totalScore, calorieLastSevenDays, calorieLastFourWeek, carbohydrateLastSevenDays, carbohydrateLastFourWeek, proteinLastSevenDays, proteinLastFourWeek, fatLastSevenDays, fatLastFourWeek);
    }
}
