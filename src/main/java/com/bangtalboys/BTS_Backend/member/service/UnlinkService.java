package com.bangtalboys.BTS_Backend.member.service;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bangtalboys.BTS_Backend.oauth.client.kakao.KakaoApiClient;
import com.bangtalboys.BTS_Backend.oauth.client.naver.NaverAuthClient;

@Service
@RequiredArgsConstructor
public class UnlinkService {
    private final KakaoApiClient kakaoApiClient;
    private final NaverAuthClient naverAuthClient;

    @Value("${kakao.admin-key}") // application.yml 등에 설정
    private String adminKey;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    public void unlinkKakao(String socialId) {
        String authHeader = "KakaoAK " + adminKey;
        kakaoApiClient.unlinkUser(authHeader, "user_id", socialId);
    }

    public void unlinkNaver(String accessToken) {
        naverAuthClient.unlinkUser(
                "delete",
                clientId,
                clientSecret,
                accessToken,
                SocialType.NAVER.toString()
        );
    }
}