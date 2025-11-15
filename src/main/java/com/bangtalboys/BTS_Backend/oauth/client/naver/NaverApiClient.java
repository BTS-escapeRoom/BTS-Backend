package com.bangtalboys.BTS_Backend.oauth.client.naver;

import com.bangtalboys.BTS_Backend.oauth.client.naver.dto.NaverUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "naverApiClient", url = "https://openapi.naver.com")
public interface NaverApiClient {

    @GetMapping(value = "/v1/nid/me")
    NaverUserInfoResponse getUserInfo(
            @RequestHeader("Authorization") String bearerToken
    );
}