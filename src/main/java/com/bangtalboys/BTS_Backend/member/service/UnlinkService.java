package com.bangtalboys.BTS_Backend.member.service;

import com.bangtalboys.BTS_Backend.oauth.client.naver.NaverApiClient;
import com.bangtalboys.BTS_Backend.oauth.client.naver.dto.NaverTokenResponse;
import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bangtalboys.BTS_Backend.oauth.client.kakao.KakaoApiClient;
import com.bangtalboys.BTS_Backend.oauth.client.naver.NaverAuthClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnlinkService {
    private final KakaoApiClient kakaoApiClient;
    private final NaverAuthClient naverAuthClient;
    private final NaverApiClient naverApiClient;

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

    public void unlinkNaver(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            log.warn("네이버 리프레시 토큰 없음 - 네이버 연동 해제 생략, 회원 탈퇴는 진행");
            return;
        }

        try {
            NaverTokenResponse tokenResponse = naverAuthClient.getAccessToken(
                    clientId,
                    clientSecret,
                    "refresh_token",
                    refreshToken
            );
            String accessToken = tokenResponse.getAccessToken();

            naverAuthClient.unlinkUser(
                    "delete",
                    clientId,
                    clientSecret,
                    accessToken,
                    SocialType.NAVER.toString()
            );
        } catch (FeignException e) {
            String errorBody = e.contentUTF8() != null && !e.contentUTF8().isEmpty()
                    ? e.contentUTF8()
                    : e.getMessage();
            String errorMessage = String.format(
                    "네이버 연동 해제 실패 [HTTP %d]: %s - 네이버 연동 해제 생략, 회원 탈퇴는 진행",
                    e.status(),
                    errorBody
            );
            log.warn(errorMessage);
        }
    }
}