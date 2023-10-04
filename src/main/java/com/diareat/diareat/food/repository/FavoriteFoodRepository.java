package com.diareat.diareat.food.repository;

import com.diareat.diareat.food.domain.FavoriteFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, Long> {
    List<FavoriteFood> findAllByUserId(Long userId);
}
