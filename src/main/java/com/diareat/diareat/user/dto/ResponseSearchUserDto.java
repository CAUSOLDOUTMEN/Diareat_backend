package com.diareat.diareat.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseSearchUserDto {

    private Long userId;
    private String name;
    private String image;
    private boolean isFollow; // 유저가 이미 팔로우한 유저인지 확인

    public static ResponseSearchUserDto of(Long userId, String name, String image, boolean isFollow) {
        return new ResponseSearchUserDto(userId, name, image, isFollow);
    }
}