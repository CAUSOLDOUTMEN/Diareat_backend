package com.diareat.diareat.food.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFoodRankDto {

    private Long userId;
    private List<ResponseFoodDto> rankFoodList;
    private LocalDate startDate; //해당 날짜로부터 7일전까지
    private boolean isBest; //isBest = true 이면 Best 3, false 이면 Worst 3

    public static ResponseFoodRankDto of(Long userId, List<ResponseFoodDto> rankFoodList, LocalDate startDate, boolean isBest) {
        return new ResponseFoodRankDto(userId, rankFoodList, startDate, isBest);
    }
}
