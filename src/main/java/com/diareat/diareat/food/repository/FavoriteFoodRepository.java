package com.diareat.diareat.food.repository;

import com.diareat.diareat.food.domain.FavoriteFood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, Long> {
}
