package com.diareat.diareat.food.domain;

import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Food {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate date;

    private LocalTime time;

    private BaseNutrition baseNutrition;

    // 생성 메서드
    public static Food createFood(String name, User user, LocalDate date, LocalTime time, BaseNutrition baseNutrition) {
        Food food = new Food();
        food.name = name;
        food.user = user;
        food.date = date;
        food.time = time;
        food.baseNutrition = baseNutrition;
        return food;
    }

    // 음식 정보 수정
    public void updateFood(String name, BaseNutrition baseNutrition) {
        this.name = name;
        this.baseNutrition = baseNutrition;
    }
}
