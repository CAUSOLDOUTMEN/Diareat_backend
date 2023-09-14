package com.diareat.diareat.user.repository;

import com.diareat.diareat.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
