package com.bangtalboys.BTS_Backend.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppSocialLoginRequest {
    private String code; // 애플

    private String accessToken; // 네이버 / 카카오

    private String refreshToken; // 네이버

    private Long id; // 카카오 사용자 고유 아이디

    private String nonce; // 애플
}