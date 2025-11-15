package com.bangtalboys.BTS_Backend.oauth.provider;

import com.bangtalboys.BTS_Backend.oauth.client.naver.NaverApiClient;
import com.bangtalboys.BTS_Backend.oauth.client.naver.NaverAuthClient;
import com.bangtalboys.BTS_Backend.oauth.client.naver.dto.NaverTokenResponse;
import com.bangtalboys.BTS_Backend.oauth.client.naver.dto.NaverUserInfoResponse;
import com.bangtalboys.BTS_Backend.oauth.dto.AppSocialLoginRequest;
import com.bangtalboys.BTS_Backend.oauth.dto.NaverResponse;
import com.bangtalboys.BTS_Backend.oauth.dto.OAuth2Response;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component("naver")
@RequiredArgsConstructor
public class NaverLoginProvider implements SocialLoginProvider {

    private final NaverAuthClient naverAuthClient;
    private final NaverApiClient naverApiClient;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.naver.authorization-grant-type}")
    private String grantType;

    @Override
    public OAuth2Response getUserProfile(AppSocialLoginRequest request) {
        // 1. code로 네이버에 Access Token 요청
        String naverAccessToken = getNaverAccessToken(request.getCode(), request.getState());
        
        // 2. Access Token으로 사용자 정보 (attributes) 요청
        NaverUserInfoResponse userInfo = getNaverUserInfo(naverAccessToken);

        return new NaverResponse(userInfo);
    }

    private String getNaverAccessToken(String code, String state) {
        try {
            NaverTokenResponse response = naverAuthClient.getAccessToken(
                    grantType,
                    clientId,
                    clientSecret,
                    code,
                    state
            );
            return response.getAccessToken();

        } catch (FeignException e) {
            throw new RuntimeException("네이버 액세스 토큰 발급에 실패했습니다.", e);
        }
    }

    private NaverUserInfoResponse getNaverUserInfo(String accessToken) {
        try {
            String bearerToken = "Bearer " + accessToken;

            return naverApiClient.getUserInfo(bearerToken);

        } catch (FeignException e) {
            throw new RuntimeException("네이버 사용자 정보 조회에 실패했습니다.", e);
        }
    }
}

