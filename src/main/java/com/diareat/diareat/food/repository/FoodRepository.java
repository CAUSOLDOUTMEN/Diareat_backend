package com.diareat.diareat.food.repository;

import com.diareat.diareat.food.domain.Food;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    boolean existsByIdAndUserId(Long id, Long userId); // 유저가 먹은 음식인지 확인
    boolean existsByName(String name);
    List<Food> findAllByUserIdAndDate(Long userId, LocalDate date, Sort sort); //유저가 특정 날짜에 먹은 음식 반환
    List<Food> findAllByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate, Sort sort); // 유저가 특정 기간 내에 먹은 음식 반환
}
