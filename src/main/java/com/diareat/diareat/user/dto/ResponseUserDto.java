package com.diareat.diareat.user.dto;

import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseUserDto {

    private Long id; //누락된 id
    private String name;
    private int height;
    private int weight;
    private int gender;
    private int age;
    private BaseNutrition baseNutrition;


    public static ResponseUserDto of(Long id, String userName, int userHeight, int userWeight, int userGender, int userAge, BaseNutrition userBaseNutrition) {
        return new ResponseUserDto(id, userName, userHeight, userWeight, userGender, userAge, userBaseNutrition);
    }

    public static ResponseUserDto from(User user) {
        return new ResponseUserDto(user.getId(), user.getName(), user.getHeight(), user.getWeight(), user.getGender(), user.getAge(), user.getBaseNutrition());
    }
}
