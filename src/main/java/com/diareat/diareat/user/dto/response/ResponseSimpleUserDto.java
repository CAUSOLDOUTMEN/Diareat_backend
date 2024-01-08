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
public class ResponseSimpleUserDto implements Serializable {

    private String name;
    private String image;
}
