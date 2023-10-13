package com.diareat.diareat.user.repository;

import com.diareat.diareat.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByNameContaining(String name); // 회원이 팔로우를 위해 검색한 유저 목록 조회
    boolean existsByName(String name); // 회원가입 시 닉네임 중복 확인
}
