package com.bangtalboys.BTS_Backend.oauth.provider;

import com.bangtalboys.BTS_Backend.oauth.client.kakao.KakaoApiClient;
import com.bangtalboys.BTS_Backend.oauth.client.kakao.KakaoAuthClient;
import com.bangtalboys.BTS_Backend.oauth.client.kakao.dto.KakaoTokenResponse;
import com.bangtalboys.BTS_Backend.oauth.client.kakao.dto.KakaoUserInfoResponse;
import com.bangtalboys.BTS_Backend.oauth.client.kakao.dto.KakaoTokenInfoResponse;
import com.bangtalboys.BTS_Backend.oauth.dto.AppSocialLoginRequest;
import com.bangtalboys.BTS_Backend.oauth.dto.KakaoResponse;
import com.bangtalboys.BTS_Backend.oauth.dto.OAuth2Response;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component("kakao")
@RequiredArgsConstructor
public class KakaoLoginProvider implements SocialLoginProvider {

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoApiClient kakaoApiClient;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String grantType;
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;
    @Value("${spring.security.oauth2.client.registration.kakao.app-id}")
    private String AppId;

    @Override
    public OAuth2Response getUserProfile(AppSocialLoginRequest request) {
        // 1. 카카오에 Access Token 유효성 검증
        validateKakaoAccessToken(request.getAccessToken(), request.getId());
        
        
        // // 2. Access Token으로 사용자 정보 (attributes) 요청
        // KakaoUserInfoResponse userInfo = getKakaoUserInfo(request.getAccessToken());

        return new KakaoResponse(request.getId());
    }

    private Boolean validateKakaoAccessToken(String accessToken, Long Id) {
        try {
            String bearerToken = "Bearer " + accessToken;
            KakaoTokenInfoResponse response = kakaoApiClient.validateAccessToken(bearerToken);
            // 앱 아이디 검증
            if (response.getApp_id().toString() != AppId) {
                return false;
            }

            // 사용자 아이디 검증
            if (response.getId() != Id) {
                return false;
            }

            return response.getExpiresIn() > 0;
        } catch (FeignException e) {
            String errorMessage = String.format(
                "카카오 액세스 토큰 유효성 검증 실패 [HTTP %d]: %s",
                e.status(),
                e.contentUTF8() != null && !e.contentUTF8().isEmpty() 
                    ? e.contentUTF8() 
                    : e.getMessage()
            );
            throw new RuntimeException(errorMessage, e);
        }
    }

    private String getKakaoAccessToken(String code) {
        try {
            KakaoTokenResponse response = kakaoAuthClient.getAccessToken(
                    grantType,
                    clientId,
                    redirectUri,
                    code,
                    clientSecret
            );
            
            return response.getAccessToken();

        } catch (FeignException e) {
            String errorMessage = String.format(
                "카카오 액세스 토큰 발급 실패 [HTTP %d]: %s",
                e.status(),
                e.contentUTF8() != null && !e.contentUTF8().isEmpty() 
                    ? e.contentUTF8() 
                    : e.getMessage()
            );
            throw new RuntimeException(errorMessage, e);
        }
    }

    private KakaoUserInfoResponse getKakaoUserInfo(String accessToken) {
        try {
            String bearerToken = "Bearer " + accessToken;

            return kakaoApiClient.getUserInfo(bearerToken);
            
        } catch (FeignException e) {
            String errorMessage = String.format(
                "카카오 사용자 정보 조회 실패 [HTTP %d]: %s",
                e.status(),
                e.contentUTF8() != null && !e.contentUTF8().isEmpty() 
                    ? e.contentUTF8() 
                    : e.getMessage()
            );
            log.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }       
}

