package com.bangtalboys.BTS_Backend.oauth.client.apple;

import com.bangtalboys.BTS_Backend.oauth.client.apple.dto.AppleTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "appleAuthClient", url = "https://appleid.apple.com")
public interface AppleAuthClient {

    @PostMapping(value = "/auth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    AppleTokenResponse getAccessToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecretJwt, // 동적으로 생성된 JWT
            @RequestParam("grant_type") String grantType,
            @RequestParam("code") String code,
            @RequestParam("redirect_uri") String redirectUri
            // (redirect_uri는 최초 요청 시에만 필요할 수 있음)
    );
}
