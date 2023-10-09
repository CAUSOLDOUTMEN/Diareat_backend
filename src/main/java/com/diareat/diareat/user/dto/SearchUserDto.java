package com.diareat.diareat.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserDto {

    private Long userId;
    private String inputName;

    public static SearchUserDto of(Long userId, String inputName) {
        return new SearchUserDto(userId, inputName);
    }
}
