package com.diareat.diareat.food.repository;

import com.diareat.diareat.food.domain.FavoriteFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, Long> {
    List<FavoriteFood> findAllByUserId(Long userId);
    boolean existsByFoodId(Long foodId); // 이미 즐겨찾기에 추가된 음식인지 확인하기 위함
}
