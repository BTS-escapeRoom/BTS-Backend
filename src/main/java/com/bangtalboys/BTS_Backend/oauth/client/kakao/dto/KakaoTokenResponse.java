package com.bangtalboys.BTS_Backend.oauth.client.kakao.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class KakaoTokenResponse {
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private Long expiresIn;
    private Long refreshTokenExpiresIn;
}
