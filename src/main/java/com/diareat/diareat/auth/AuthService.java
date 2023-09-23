package com.diareat.diareat.auth;

import com.diareat.diareat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService { // 회원가입 및 로그인 기능 전담

    private final UserRepository userRepository;

    // 회원가입
    public void signUp() {

    }

    // 로그인
    public void signIn() {

    }
}
