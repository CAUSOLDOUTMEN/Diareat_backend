package com.diareat.diareat.food.repository;

import com.diareat.diareat.food.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findAllByUserIdAndDate(Long userId, LocalDate date); //유저가 특정 날짜에 먹은 음식 반환
}
