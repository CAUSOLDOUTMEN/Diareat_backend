package com.diareat.diareat.auth.dto;

import lombok.Getter;

@Getter
public class KakaoProfile {

    private String nickname;
    private String thumbnail_image_url;
    private String profile_image_url;
    private Boolean is_default_image;
}
