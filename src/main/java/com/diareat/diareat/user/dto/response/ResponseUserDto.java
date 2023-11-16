package com.diareat.diareat.user.dto.response;

import com.diareat.diareat.user.domain.User;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@JsonDeserialize
public class ResponseUserDto implements Serializable {

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
