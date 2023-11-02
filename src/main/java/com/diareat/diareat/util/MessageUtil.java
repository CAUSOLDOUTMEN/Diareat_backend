package com.diareat.diareat.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtil { // 반복되는 메시지의 형식을 저장하고 관리

    public static final String NOT_NULL = "값이 존재해야 합니다.";
    public static final String NOT_BLANK = "값이 비어있을 수 없습니다.";

    public static final String HEIGHT_RANGE = "키는 100 이상, 250 이하의 값을 입력해주세요.";
    public static final String WEIGHT_RANGE = "몸무게는 30 이상, 200 이하의 값을 입력해주세요.";
    public static final String AGE_RANGE = "나이는 5 이상, 100 이하의 값을 입력해주세요.";
    public static final String GENDER_RANGE = "성별은 0(남자) 또는 1(여자)의 값을 입력해주세요.";

    public static final String CALORIE_RANGE = "칼로리는 100 이상, 10000 이하의 값을 입력해주세요.";
    public static final String CARBOHYDRATE_RANGE = "탄수화물은 100 이상, 500 이하의 값을 입력해주세요.";
    public static final String PROTEIN_RANGE = "단백질은 25 이상, 500 이하의 값을 입력해주세요.";
    public static final String FAT_RANGE = "지방은 25 이상, 500 이하의 값을 입력해주세요.";
}
