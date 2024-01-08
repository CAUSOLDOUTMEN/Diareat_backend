package com.diareat.diareat.user.dto.request;

import com.diareat.diareat.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinUserDto {

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String token;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String nickName;

    @Range(min = 0, max = 1, message = MessageUtil.GENDER_RANGE)
    private int gender;

    @Range(min = 100, max = 250, message = MessageUtil.HEIGHT_RANGE)
    private int height;

    @Range(min = 30, max = 150, message = MessageUtil.WEIGHT_RANGE)
    private int weight;

    @Range(min = 5, max = 100, message = MessageUtil.AGE_RANGE)
    private int age;
}
