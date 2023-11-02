package com.diareat.diareat.user.dto.request;

import com.diareat.diareat.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
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

    @DecimalMin(value = "100", message = MessageUtil.HEIGHT_RANGE)
    @DecimalMax(value = "250", message = MessageUtil.HEIGHT_RANGE)
    private int height;

    @DecimalMin(value = "30", message = MessageUtil.WEIGHT_RANGE)
    @DecimalMax(value = "200", message = MessageUtil.WEIGHT_RANGE)
    private int weight;

    @DecimalMin(value = "5", message = MessageUtil.AGE_RANGE)
    @DecimalMax(value = "100", message = MessageUtil.AGE_RANGE)
    private int age;

    private boolean isAutoUpdateNutrition; // 개인정보를 활용한 기준 영양소 자동계산 여부

    public static UpdateUserDto of(Long userId, String userName, int userHeight, int userWeight, int userAge, boolean isAutoUpdateNutrition) {
        return new UpdateUserDto(userId, userName, userHeight, userWeight, userAge, isAutoUpdateNutrition);
    }
}
