package com.diareat.diareat.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseJwtDto {

    private Long id;
    private String jwt;

    public static ResponseJwtDto of(Long id, String jwt) {
        return new ResponseJwtDto(id, jwt);
    }
}
