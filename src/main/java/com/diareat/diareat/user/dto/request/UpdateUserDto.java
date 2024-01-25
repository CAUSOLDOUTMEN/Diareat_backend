package com.diareat.diareat.user.dto.request;

import com.diareat.diareat.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    @NotNull(message = MessageUtil.NOT_NULL)
    private Long userId;

    @NotBlank(message = MessageUtil.NOT_BLANK)
    private String name;

    @Range(min = 100, max = 250, message = MessageUtil.HEIGHT_RANGE)
    private int height;

    @Range(min = 30, max = 200, message = MessageUtil.WEIGHT_RANGE)
    private int weight;

    @Range(min = 5, max = 100, message = MessageUtil.AGE_RANGE)
    private int age;

    @Range(min = 0, max = 1, message = MessageUtil.ZERO_OR_ONE)
    private int autoUpdateNutrition; // 개인정보를 활용한 기준 영양소 자동계산 여부 (0, 1)

    public static UpdateUserDto of(Long userId, String userName, int userHeight, int userWeight, int userAge, int autoUpdateNutrition) {
        return new UpdateUserDto(userId, userName, userHeight, userWeight, userAge, autoUpdateNutrition);
    }
}
