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
            String accessToken = response.getAccessToken();
            return accessToken;

        } catch (FeignException e) {
            String errorMessage = String.format(
                "네이버 액세스 토큰 발급 실패 [HTTP %d]: %s",
                e.status(),
                e.contentUTF8() != null && !e.contentUTF8().isEmpty() 
                    ? e.contentUTF8() 
                    : e.getMessage()
            );
            log.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    private NaverUserInfoResponse getNaverUserInfo(String accessToken) {
        try {
            if (accessToken == null || accessToken.trim().isEmpty()) {
                log.error("네이버 Access Token이 null이거나 비어있습니다.");
                throw new IllegalArgumentException("Access Token이 유효하지 않습니다.");
            }
            
            String bearerToken = "Bearer " + accessToken;

            NaverUserInfoResponse response = naverApiClient.getUserInfo(bearerToken);
            return response;

        } catch (FeignException e) {
            String errorBody = e.contentUTF8() != null && !e.contentUTF8().isEmpty() 
                    ? e.contentUTF8() 
                    : e.getMessage();
            String errorMessage = String.format(
                "네이버 사용자 정보 조회 실패 [HTTP %d]: %s",
                e.status(),
                errorBody
            );
            log.error("네이버 사용자 정보 조회 실패 - Access Token 앞 10자: {}, 에러 응답: {}", 
                    accessToken != null && accessToken.length() > 10 ? accessToken.substring(0, 10) + "..." : "null",
                    errorBody);
            log.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }
}

