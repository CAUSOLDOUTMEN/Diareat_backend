package com.diareat.diareat.food.repository;

import com.diareat.diareat.food.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    boolean existsByIdAndUserId(Long id, Long userId); // 유저가 먹은 음식인지 확인
    List<Food> findAllByUserIdAndDateOrderByAddedTimeAsc(Long userId, LocalDate date); //유저가 특정 날짜에 먹은 음식 반환
    List<Food> findAllByUserIdAndDateBetweenOrderByAddedTimeAsc(Long userId, LocalDate startDate, LocalDate endDate); // 유저가 특정 기간 내에 먹은 음식 반환

    // 특정 즐겨찾기 음식으로부터 태어난 음식 데이터의, 즐겨찾기 음식과의 연관관계를 해제
    @Query(value = "UPDATE Food f set f.favoriteFood = null where f.favoriteFood.id = :id")
    void updateFoodRelationShipWithFavoriteFood(@Param("id")Long favoriteFoodId);
}
