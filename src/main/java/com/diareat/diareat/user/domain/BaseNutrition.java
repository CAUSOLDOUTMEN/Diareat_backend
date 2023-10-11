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

    // 생성 메서드 by 개인정보 (성별, 나이, 키, 몸무게로 기준영양소 자동 계산 기능)
    public static BaseNutrition calculateNutrition(int gender, int age, int height, int weight) {
        BaseNutrition baseNutrition = new BaseNutrition();

        // 임의의 식으로 기초대사량 계산하였으며, 추후 식약처 및 관련 기관에서 제공하는 공식으로 변경할 예정
        baseNutrition.kcal = (int) (66.47 + (13.75 * weight) + (5 * height) - (6.76 * age)); // 기초대사량 계산식
        baseNutrition.carbohydrate = (int) (baseNutrition.kcal * 0.65 / 4); // 탄수화물 65%
        baseNutrition.protein = (int) (baseNutrition.kcal * 0.1 / 4); // 단백질 10%
        baseNutrition.fat = (int) (baseNutrition.kcal * 0.25 / 9);

        if(gender == 1) { // 여성일 경우
            baseNutrition.kcal -= 161;
            baseNutrition.carbohydrate -= 40;
            baseNutrition.protein -= 5;
            baseNutrition.fat -= 10;
        }
        return baseNutrition;
    }
}
