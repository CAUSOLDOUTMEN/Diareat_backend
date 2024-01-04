package com.diareat.diareat.user.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
@JsonSerialize
@JsonDeserialize
public class ResponseSearchUserDto implements Serializable {

    private Long userId;
    private String name;
    private String image;
    private boolean follow; // 유저가 이미 팔로우한 유저인지 확인
}
