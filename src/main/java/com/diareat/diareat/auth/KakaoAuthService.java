package com.diareat.diareat.auth;

import com.diareat.diareat.auth.component.KakaoUserInfo;
import com.diareat.diareat.auth.dto.KakaoUserInfoResponse;
import com.diareat.diareat.user.domain.User;
import com.diareat.diareat.user.repository.UserRepository;
import com.diareat.diareat.util.api.ResponseCode;
import com.diareat.diareat.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class KakaoAuthService { // 카카오 소셜로그인, 세션 관리는 추후 구현 예정

    private final KakaoUserInfo kakaoUserInfo;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Long isSignedUp(String token) { // 클라이언트가 보낸 token을 이용해 카카오 API에 유저 정보를 요청, 유저가 존재하지 않으면 예외 발생, 존재하면 회원번호 반환
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(token);
        User user = userRepository.findByKeyCode(userInfo.getId().toString()).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
        return user.getId();
    }
}
