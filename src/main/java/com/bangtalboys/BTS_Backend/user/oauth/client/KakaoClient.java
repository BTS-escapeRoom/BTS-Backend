package com.bangtalboys.BTS_Backend.user.oauth.client;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import com.bangtalboys.BTS_Backend.user.oauth.dto.KakaoUserInfoResponse;
import com.bangtalboys.BTS_Backend.user.oauth.dto.KakaoToken;
import com.bangtalboys.BTS_Backend.user.oauth.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoClient implements OAuthApiClient {

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-uri}}")
    private String authorizationUrl;

    @Value("${spring.security.oauth2.client.registration.kakao.token-uri}}")
    private String tokenUrl;

    @Value("${spring.security.oauth2.client.registration.kakao.user-info-uri}}")
    private String userInfoUrl;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    private final RestTemplate restTemplate;

    @Override
    public SocialType socialType() {
        return SocialType.KAKAO;
    }

    public String getAccessToken(LoginRequest params) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        KakaoToken response = restTemplate.postForObject(tokenUrl, request, KakaoToken.class);

        assert response != null;
        return response.getAccessToken();
    }

    public KakaoUserInfoResponse getUserInfo(String accessToken) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        return restTemplate.postForObject(userInfoUrl, request, KakaoUserInfoResponse.class);
    }
}
