package com.diareat.diareat.food.repository;

import com.diareat.diareat.food.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
}
