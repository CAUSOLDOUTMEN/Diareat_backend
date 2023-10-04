package com.diareat.diareat.food.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseNutritionSumByDateDto {
    int totalKcal;
    int totalCarbohydrate;
    int totalProtein;
    int totalFat;

    double ratioKcal;
    double ratioCarbohydrate;
    double ratioProtein;
    double ratioFat;

    public static ResponseNutritionSumByDateDto of (int totalKcal, int totalCarbohydrate, int totalProtein, int totalFat, double ratioKcal, double ratioCarbohydrate, double ratioProtein, double ratioFat){
        return new ResponseNutritionSumByDateDto(totalFat, totalCarbohydrate, totalProtein, totalFat, ratioKcal, ratioCarbohydrate, ratioProtein, ratioFat);
    }
}
