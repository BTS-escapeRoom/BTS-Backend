package com.bangtalboys.BTS_Backend.member.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "NaverUnlinkClient", url = "${naver.api-url}")
public interface NaverUnlinkClient {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void unlinkUser(@RequestParam("grant_type") String grantType,
                    @RequestParam("client_id") String clientId,
                    @RequestParam("client_secret") String clientSecret,
                    @RequestParam("access_token") String accessToken,
                    @RequestParam("service_provider") String serviceProvider);
}