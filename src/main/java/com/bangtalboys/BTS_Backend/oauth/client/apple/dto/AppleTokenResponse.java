package com.bangtalboys.BTS_Backend.oauth.client.apple.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppleTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("id_token")
    private String idToken; // <-- 여기에 사용자 정보가 들어있음
    // ...
}