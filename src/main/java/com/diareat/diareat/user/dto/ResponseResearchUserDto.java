package com.diareat.diareat.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseResearchUserDto {

    private Long userId;
    private String name;

    public static ResponseResearchUserDto of(Long userId, String name) {
        return new ResponseResearchUserDto(userId, name);
    }
}
