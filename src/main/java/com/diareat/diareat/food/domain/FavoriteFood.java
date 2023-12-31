package com.diareat.diareat.food.domain;

import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FavoriteFood {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_food_id")
    private Long id;

    private String name;

    //@OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST}, orphanRemoval = false) // 즐찾음식이 삭제되어도 음식은 삭제되지 않음
    //@JoinColumn(name = "food_id")
    @OneToMany(mappedBy = "favoriteFood", cascade = CascadeType.PERSIST, orphanRemoval = false) // 즐찾음식이 삭제되어도 음식은 삭제되지 않음
    private List<Food> foods = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int count = 0; // 즐겨찾기에서 선택되어 바로 음식으로 추가된 횟수

    private BaseNutrition baseNutrition;

    public void addCount(){
        this.count++;
    }

    // 생성 메서드
    public static FavoriteFood createFavoriteFood(String name, User user, Food food, BaseNutrition baseNutrition) {
        FavoriteFood favoriteFood = new FavoriteFood();
        favoriteFood.name = name;
        favoriteFood.foods.add(food);
        favoriteFood.user = user;
        favoriteFood.baseNutrition = baseNutrition;
        //food.setFavoriteFood(favoriteFood); 관계의 주인이 즐겨찾기로 명확하게 정해졌기에 주석처리
        return favoriteFood;
    }

    // 즐겨찾기 음식 정보 수정
    public void updateFavoriteFood(String name, BaseNutrition baseNutrition) {
        this.name = name;
        this.baseNutrition = baseNutrition;
    }

    // 즐겨찾기 음식 정보로 새 음식 정보 생성
    public static Food createFoodFromFavoriteFood(FavoriteFood favoriteFood) {
        favoriteFood.addCount();
        LocalDate createdDate = LocalDate.now();
        return Food.createFood(favoriteFood.getName(), favoriteFood.getUser(), favoriteFood.getBaseNutrition(), createdDate.getYear(), createdDate.getMonthValue(), createdDate.getDayOfMonth());
    }
}
