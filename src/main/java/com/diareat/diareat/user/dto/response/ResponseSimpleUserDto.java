package com.diareat.diareat.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseSimpleUserDto {

    private String name;
    private String image;
    private double nutritionScore;

    public static ResponseSimpleUserDto of(String name, String image, double nutritionScore) {
        return new ResponseSimpleUserDto(name, image, nutritionScore);
    }
}
