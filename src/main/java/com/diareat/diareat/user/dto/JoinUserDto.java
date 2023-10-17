package com.diareat.diareat.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinUserDto {

    private String token;
    private String nickName;
    private int gender;
    private int height;
    private int weight;
    private int age;
}
