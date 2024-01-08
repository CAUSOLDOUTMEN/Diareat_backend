package com.diareat.diareat.user.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
@JsonSerialize
@JsonDeserialize
public class ResponseUserNutritionDto implements Serializable {

    private int calorie; // 유저가 설정한(할 수 있는) 현재 영양소 기준섭취량
    private int carbohydrate;
    private int protein;
    private int fat;
}
