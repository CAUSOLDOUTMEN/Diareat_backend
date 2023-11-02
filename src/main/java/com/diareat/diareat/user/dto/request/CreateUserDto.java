package com.diareat.diareat.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {

    private String name;
    private String image;
    private String keyCode;
    private int gender;
    private int height;
    private int weight;
    private int age;

    public static CreateUserDto of(String name, String image, String keyCode, int gender, int height, int weight, int age){
        return new CreateUserDto(name, image, keyCode, gender, height, weight, age);
    }
}