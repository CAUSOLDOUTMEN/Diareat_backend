package com.diareat.diareat.user.repository;

import com.diareat.diareat.user.domain.Follow;
import com.diareat.diareat.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Follow.PK> {
    @Query(value = "select u from Follow f INNER JOIN User u ON f.toUser = u.id where f.fromUser = :userId") // 팔로우 목록 조회
    List<User> findAllByFromUser(@Param("userId") Long userId);
}
