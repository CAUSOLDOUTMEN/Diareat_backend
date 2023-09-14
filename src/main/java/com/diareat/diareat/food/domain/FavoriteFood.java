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
public class FavoriteFood {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_food_id")
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    private LocalDate date;

    private LocalTime time;

    private BaseNutrition baseNutrition;

}
