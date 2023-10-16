package com.diareat.diareat.user.domain;

import com.diareat.diareat.food.domain.FavoriteFood;
import com.diareat.diareat.food.domain.Food;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name; // 닉네임

    @JsonIgnore
    @Column(length = 100, nullable = false, unique = true)
    private String keyCode; // 로그인 식별키

    private String image; // 프로필 사진 경로

    private int height; // 키

    private int weight; // 몸무게

    private int gender; // 성별 (0: 남자, 1: 여자)

    private int age; // 나이

    private BaseNutrition baseNutrition; // 기준영양소

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}) // 유저가 탈퇴하면 촬영한 음식도 삭제
    private List<Food> foods = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}) // 유저가 탈퇴하면 즐겨찾기 음식도 삭제
    private List<FavoriteFood> favoriteFoods = new ArrayList<>();

    // Jwt 전용 설정 (UserDetails 인터페이스 구현)

    @ElementCollection(fetch = FetchType.EAGER) //roles 컬렉션
    private List<String> roles = new ArrayList<>();

    @Override   //사용자의 권한 목록 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return keyCode;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Jwt 전용 설정 종료

    // 생성 메서드
    public static User createUser(String name, String image, String keyCode, int height, int weight, int gender, int age, BaseNutrition baseNutrition) {
        User user = new User();
        user.name = name;
        user.image = image;
        user.keyCode = keyCode;
        user.height = height;
        user.weight = weight;
        user.gender = gender;
        user.age = age;
        user.baseNutrition = baseNutrition;
        return user;
    }

    // 회원정보 수정
    public void updateUser(String name, int height, int weight, int age) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.age = age;
    }

    // 회원 기준영양소 직접 수정
    public void updateBaseNutrition(BaseNutrition baseNutrition) {
        this.baseNutrition = baseNutrition;
    }

    public void setId(Long id) { // 테스트코드용 메서드
        this.id = id;
    }
}
