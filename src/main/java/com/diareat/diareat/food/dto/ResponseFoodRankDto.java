package com.diareat.diareat.food.dto;

import com.diareat.diareat.food.domain.Food;
import com.diareat.diareat.user.domain.BaseNutrition;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ResponseFoodRankDto {

    private Long userId;
    private List<Food> rankFoodList;
    private LocalDate startDate; //해당 날짜로부터 7일전까지
    private boolean isBest; //isBest = true 이면 Best 3, false 이면 Worst 3

    public static ResponseFoodRankDto of(Long userId, List<Food> rankFoodList, LocalDate startDate, boolean isBest) {
        return new ResponseFoodRankDto(userId, rankFoodList, startDate, isBest);
    }
}
