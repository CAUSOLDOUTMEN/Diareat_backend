package com.diareat.diareat.food.repository;

import com.diareat.diareat.food.domain.FavoriteFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, Long> {
    List<FavoriteFood> findAllByUserId(Long userId);
    boolean existsByIdAndUserId(Long id, Long userId); // 유저가 즐겨찾기에 추가한 음식인지 확인
}
