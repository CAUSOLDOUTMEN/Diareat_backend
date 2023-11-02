package com.diareat.diareat.user.dto.request;

import com.diareat.diareat.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinUserDto {

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String token;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String nickName;

    @DecimalMin(value = "0", message = MessageUtil.GENDER_RANGE)
    @DecimalMax(value = "1", message = MessageUtil.GENDER_RANGE)
    private int gender;

    @DecimalMin(value = "100", message = MessageUtil.HEIGHT_RANGE)
    @DecimalMax(value = "250", message = MessageUtil.HEIGHT_RANGE)
    private int height;

    @DecimalMin(value = "30", message = MessageUtil.WEIGHT_RANGE)
    @DecimalMax(value = "150", message = MessageUtil.WEIGHT_RANGE)
    private int weight;

    @DecimalMin(value = "5", message = MessageUtil.AGE_RANGE)
    @DecimalMax(value = "100", message = MessageUtil.AGE_RANGE)
    private int age;

}
