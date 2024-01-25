package com.diareat.diareat.auth.dto;

import lombok.Getter;

@Getter
public class KakaoUserInfoResponse {

    private Long id;
    private String connected_at;
    private KakaoProperties properties;
    private KakaoAccount kakao_account;
}
