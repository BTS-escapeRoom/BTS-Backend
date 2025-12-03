package com.bangtalboys.BTS_Backend.oauth.client.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.bangtalboys.BTS_Backend.oauth.client.kakao.dto.KakaoUserInfoResponse;
import com.bangtalboys.BTS_Backend.oauth.client.kakao.dto.KakaoTokenInfoResponse;


/**
 * 카카오 API 서버(kapi.kakao.com)와 통신하는 Feign Client
 * (application.yml에 'kakao.api-url' 프로퍼티로 URL 관리 추천)
 */
@FeignClient(name = "kakaoApiClient", url = "https://kapi.kakao.com")
public interface KakaoApiClient {

    @GetMapping(value = "/v2/user/me", headers = "Content-Type=application/x-www-form-urlencoded;charset=utf-8")
    KakaoUserInfoResponse getUserInfo(
            @RequestHeader("Authorization") String bearerToken
    );

    @PostMapping(value = "/v1/user/unlink", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void unlinkUser(
            @RequestHeader("Authorization") String authorization,
            @RequestParam(name = "target_id_type", defaultValue = "user_id") String targetIdType,
            @RequestParam("target_id") String socialId
    );

    @GetMapping(value = "/v1/user/access_token_info")
    KakaoTokenInfoResponse validateAccessToken(
            @RequestHeader("Authorization") String bearerToken
    );
}