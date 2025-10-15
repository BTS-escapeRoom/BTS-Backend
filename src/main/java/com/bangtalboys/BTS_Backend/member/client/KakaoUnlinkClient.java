package com.bangtalboys.BTS_Backend.member.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

//KakaoUnlinkClient
@FeignClient(name = "KakaoUnlinkClient", url = "${kakao.api-url}")
public interface KakaoUnlinkClient {

    @PostMapping(value = "/v1/user/unlink", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void unlinkUser(
            @RequestHeader("Authorization") String authorization,
            @RequestParam(name = "target_id_type", defaultValue = "user_id") String targetIdType,
            @RequestParam("target_id") String socialId
    );
}