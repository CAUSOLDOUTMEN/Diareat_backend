package com.diareat.diareat.auth.service;

import com.diareat.diareat.auth.component.KakaoUserInfo;
import com.diareat.diareat.auth.dto.KakaoUserInfoResponse;
import com.diareat.diareat.user.domain.User;
import com.diareat.diareat.user.dto.request.CreateUserDto;
import com.diareat.diareat.user.dto.request.JoinUserDto;
import com.diareat.diareat.user.repository.UserRepository;
import com.diareat.diareat.util.api.ResponseCode;
import com.diareat.diareat.util.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class KakaoAuthService {

    private final KakaoUserInfo kakaoUserInfo;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Long isSignedUp(String token) { // 클라이언트가 보낸 token을 이용해 카카오 API에 유저 정보를 요청, 유저가 존재하지 않으면 예외 발생, 존재하면 회원번호 반환
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(token);
        User user = userRepository.findByKeyCode(userInfo.getId().toString()).orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
        return user.getId();
    }

    @Transactional(readOnly = true)
    public CreateUserDto createUserDto(JoinUserDto joinUserDto) { // 카카오로부터 프사 URL, 유저 고유ID를 얻어온 후, 이를 유저가 입력한 정보와 함께 CreateUserDto로 반환
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(joinUserDto.getToken());
        return CreateUserDto.of(joinUserDto.getNickName(), userInfo.getKakao_account().getProfile().getProfile_image_url(),
                userInfo.getId().toString(), joinUserDto.getGender(), joinUserDto.getHeight(), joinUserDto.getWeight(), joinUserDto.getAge());
    }
}
