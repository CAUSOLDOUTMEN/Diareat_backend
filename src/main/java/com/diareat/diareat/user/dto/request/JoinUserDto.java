package com.diareat.diareat.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinUserDto {

    @NotNull(message = "token은 null이 될 수 없습니다.")
    private String token;

    @NotBlank(message = "nickName은 비어있을 수 없습니다.")
    private String nickName;

    @DecimalMin(value = "0", message = "gender는 0(남자), 1(여자)만 가능합니다.")
    @DecimalMax(value = "1", message = "gender는 0(남자), 1(여자)만 가능합니다.")
    private int gender;

    private int height;

    private int weight;

    private int age;

}
