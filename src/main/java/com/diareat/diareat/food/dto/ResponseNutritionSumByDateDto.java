package com.diareat.diareat.food.dto;

import com.diareat.diareat.user.domain.BaseNutrition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseNutritionSumByDateDto implements Serializable {

    Long userId;
    // LocalDate checkDate; //조회한 날짜
    int nutritionSumType; //조회할 기간을 나타내는 코드. {1: 특정 날짜, 7: 최근 7일간, 30: 최근 한달간}

    int totalKcal;
    int totalCarbohydrate;
    int totalProtein;
    int totalFat;

    double ratioKcal;
    double ratioCarbohydrate;
    double ratioProtein;
    double ratioFat;

    BaseNutrition baseNutrition;

    boolean isEmpty;

    public static ResponseNutritionSumByDateDto of (Long userId, LocalDate checkDate, int nutritionSumType, int totalKcal,
                                                    int totalCarbohydrate, int totalProtein, int totalFat, double ratioKcal,
                                                    double ratioCarbohydrate, double ratioProtein, double ratioFat,
                                                    BaseNutrition baseNutrition, boolean isEmpty){
        return new ResponseNutritionSumByDateDto(userId, nutritionSumType, totalKcal,
                                                totalCarbohydrate, totalProtein, totalFat, ratioKcal,
                                                ratioCarbohydrate, ratioProtein, ratioFat, baseNutrition, isEmpty);
    }
}
