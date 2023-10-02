package com.diareat.diareat.food.domain;

import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

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

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = false) // 음식이 삭제되어도 즐찾음식은 삭제되지 않음
    @JoinColumn(name = "favorite_food_id")
    private FavoriteFood favoriteFood;

    @CreatedDate
    private LocalDate date;

    private LocalTime time;

    private BaseNutrition baseNutrition;

    // 생성 메서드
    public static Food createFood(String name, User user, BaseNutrition baseNutrition) {
        Food food = new Food();
        food.name = name;
        food.user = user;
        food.time = LocalTime.now();
        food.baseNutrition = baseNutrition;
        return food;
    }

    // 음식 정보 수정
    public void updateFood(String name, BaseNutrition baseNutrition) {
        this.name = name;
        this.baseNutrition = baseNutrition;
    }
}
