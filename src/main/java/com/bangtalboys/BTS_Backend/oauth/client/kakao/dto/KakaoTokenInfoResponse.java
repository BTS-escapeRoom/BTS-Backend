package com.bangtalboys.BTS_Backend.oauth.client.kakao.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoTokenInfoResponse {

    private Long id;
    private Integer expires_in;
    private Integer app_id;
    
}
