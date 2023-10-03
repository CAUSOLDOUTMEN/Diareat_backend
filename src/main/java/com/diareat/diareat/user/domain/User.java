package com.diareat.diareat.user.domain;

import com.diareat.diareat.food.domain.FavoriteFood;
import com.diareat.diareat.food.domain.Food;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name; // 닉네임

    @JsonIgnore
    private String keyCode; // 로그인 식별키

    private int height; // 키

    private int weight; // 몸무게

    private int gender; // 성별 (0: 남자, 1: 여자)

    private int age; // 나이

    private BaseNutrition baseNutrition; // 영양소

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}) // 유저가 탈퇴하면 촬영한 음식도 삭제
    private List<Food> foods = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}) // 유저가 탈퇴하면 즐겨찾기 음식도 삭제
    private List<FavoriteFood> favoriteFoods = new ArrayList<>();

    @ManyToMany // 팔로우 목록
    @JoinTable(
            name = "user_follows",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private Set<User> followings = new HashSet<>();

    // 생성 메서드
    public static User createUser(String name, String keyCode, int height, int weight, int gender, int age, BaseNutrition baseNutrition) {
        User user = new User();
        user.name = name;
        user.keyCode = keyCode;
        user.height = height;
        user.weight = weight;
        user.gender = gender;
        user.age = age;
        user.baseNutrition = baseNutrition;
        return user;
    }

    // 회원정보 수정
    public void updateUser(String name, int height, int weight, int age, BaseNutrition baseNutrition) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.baseNutrition = baseNutrition;
    }

    public void followUser(User user) {
        this.followings.add(user);
    }

    public void unfollowUser(User user) {
        this.followings.remove(user);
    }
}
