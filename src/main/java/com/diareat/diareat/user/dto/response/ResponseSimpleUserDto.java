package com.diareat.diareat.user.dto.response;

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
public class ResponseSimpleUserDto implements Serializable {

    private String name;
    private String image;

    public static ResponseSimpleUserDto of(String name, String image) {
        return new ResponseSimpleUserDto(name, image);
    }
}
