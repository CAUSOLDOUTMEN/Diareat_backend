package com.diareat.diareat.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseSearchUserDto {

    private Long userId;
    private String name;
    private String image;

    public static ResponseSearchUserDto of(Long userId, String name, String image) {
        return new ResponseSearchUserDto(userId, name, image);
    }
}
