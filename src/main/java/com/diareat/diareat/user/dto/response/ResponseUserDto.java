package com.diareat.diareat.user.dto.response;

import com.diareat.diareat.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseUserDto {

    private String name;
    private int height;
    private int weight;
    private int age;

    public static ResponseUserDto from(User user) {
        return new ResponseUserDto(user.getName(), user.getHeight(), user.getWeight(), user.getAge());
    }

    public static ResponseUserDto of(String name, int height, int weight, int age) {
        return new ResponseUserDto(name, height, weight, age);
    }
}
