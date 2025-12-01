package com.bangtalboys.BTS_Backend.oauth.provider;

import com.bangtalboys.BTS_Backend.oauth.dto.AppSocialLoginRequest;
import com.bangtalboys.BTS_Backend.oauth.dto.AppleResponse;
import com.bangtalboys.BTS_Backend.oauth.dto.OAuth2Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.bangtalboys.BTS_Backend.oauth.client.apple.AppleAuthClient;
import com.bangtalboys.BTS_Backend.oauth.client.apple.dto.AppleTokenResponse;
import com.bangtalboys.BTS_Backend.oauth.util.AppleJwtUtils;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Component("apple")
@RequiredArgsConstructor
public class AppleLoginProvider implements SocialLoginProvider{

    private final AppleAuthClient appleAuthClient;
    private final AppleJwtUtils appleJwtUtils;

    @Value("${spring.security.oauth2.client.registration.apple.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.apple.authorization-grant-type}")
    private String grantType;
    @Value("${spring.security.oauth2.client.registration.apple.redirect-uri}")
    private String redirectUri;

    @Override
    public OAuth2Response getUserProfile(AppSocialLoginRequest request) {

        String appNonce = request.getNonce();
        if (appNonce == null || appNonce.isBlank()) {
            throw new IllegalArgumentException("Apple 로그인 요청에 'nonce' 값이 없습니다.");
        }

        // 1. 토큰 받기 (이 과정에서 id_token도 함께 받음)
        AppleTokenResponse tokenResponse = getAppleToken(request.getCode());
        
        // 2. id_token 디코딩 (별도 API 호출 없음)
        Map<String, Object> attributes = appleJwtUtils.decodeIdToken(
            tokenResponse.getIdToken(),
            appNonce
        );

        // 3. DTO를 AppleResponse에 바로 전달
        return new AppleResponse(attributes);
    }

    private AppleTokenResponse getAppleToken(String code) {
        try {
            // 1. Feign 호출 전, client_secret JWT 동적 생성
            String clientSecret = appleJwtUtils.createClientSecret();

            // 2. Feign Client 호출
            return appleAuthClient.getAccessToken(
                    clientId,
                    clientSecret,
                    grantType,
                    code,
                    redirectUri
            );
        } catch (FeignException e) {
            String errorMessage = String.format(
                "애플 액세스 토큰 발급 실패 [HTTP %d]: %s",
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

