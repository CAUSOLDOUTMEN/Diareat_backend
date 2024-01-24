package com.diareat.diareat.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResponseJwtDto {

    private String accessToken;
    private String refreshToken;
}
