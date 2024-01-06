package com.diareat.diareat.food.domain;

import com.diareat.diareat.user.domain.BaseNutrition;
import com.diareat.diareat.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Food {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //@OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST}, orphanRemoval = false) // 음식이 삭제되어도 즐찾음식은 삭제되지 않음
    //@JoinColumn(name = "favorite_food_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST}) // 다대일로 매핑하여 음식의 즐찾음식을 찾을 수 있도록 함
    @JoinColumn(name = "favorite_food_id")
    private FavoriteFood favoriteFood;

    private LocalDate date;

    @CreatedDate
    @Column(name = "added_time") //테이블과 매핑
    private LocalDateTime addedTime; //클라이언트에서 추가하도록 요청 보낸 timestamp

    private BaseNutrition baseNutrition;

    // 생성 메서드
    public static Food createFood(String name, User user, BaseNutrition baseNutrition, int year, int month, int day) {
        Food food = new Food();
        food.name = name;
        food.user = user;
        food.date = LocalDate.of(year, month, day);
        food.baseNutrition = baseNutrition;

        return food;
    }

    // 음식 정보 수정
    public void updateFood(String name, BaseNutrition baseNutrition) {
        this.name = name;
        this.baseNutrition = baseNutrition;
    }

    public boolean isFavorite() {
        return this.favoriteFood != null;
    }

    public void setId(long id) {this.id = id;}

    public void setDate(LocalDate date) {this.date = date;} //food test를 위한 date

    public void setFavoriteFood(FavoriteFood favoriteFood){
        this.favoriteFood = favoriteFood;
    }
}
