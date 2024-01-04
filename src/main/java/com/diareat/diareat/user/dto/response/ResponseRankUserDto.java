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
public class ResponseRankUserDto implements Serializable {

    private Long userId;
    private String name;
    private String image;
    private double calorieScore;
    private double carbohydrateScore;
    private double proteinScore;
    private double fatScore;
    private double totalScore;
}
