package com.diareat.diareat.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable // 값 타입
public class BaseNutrition { // 기본 영양소 4종만 포함

    private int kcal = 0; // 칼로리
    private int carbohydrate = 0; // 탄수화물
    private int protein = 0; // 단백질
    private int fat = 0; // 지방

    // 생성 메서드
    public static BaseNutrition createNutrition(int kcal, int carbohydrate, int protein, int fat) {
        BaseNutrition baseNutrition = new BaseNutrition();
        baseNutrition.kcal = kcal;
        baseNutrition.carbohydrate = carbohydrate;
        baseNutrition.protein = protein;
        baseNutrition.fat = fat;
        return baseNutrition;
    }

    // 영양소 수정
    public void updateNutrition(int kcal, int carbohydrate, int protein, int fat) {
        this.kcal = kcal;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
    }
}
