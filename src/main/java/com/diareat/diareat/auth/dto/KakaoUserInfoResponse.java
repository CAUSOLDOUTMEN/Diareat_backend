package com.diareat.diareat.auth.dto;

import lombok.Getter;

@Getter
public class KakaoUserInfoResponse {

    private Long id;
    private boolean hasSignedUp;
    private KakaoAccount kakaoAccount;
}
