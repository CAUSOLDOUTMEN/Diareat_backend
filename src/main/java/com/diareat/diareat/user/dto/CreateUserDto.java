package com.diareat.diareat.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {

    private String name;
    private String keyCode;
    private int gender;
    private int height;
    private int weight;
    private int age;

    public static CreateUserDto of(String name, String keyCode, int gender, int height, int weight, int age){
        return new CreateUserDto(name, keyCode, gender, height, weight, age);
    }
}
