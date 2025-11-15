package com.bangtalboys.BTS_Backend.oauth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppSocialLoginRequest {
    @NotEmpty
    private String code; // 카카오/네이버에서 받은 Authorization Code

    private String state; // 네이버 로그인을 위해 state 전달 필요 (임의의 문자열)

    private String nonce; // 애플 로그인을 위해 nonce 전달 필요 (임의의 문자열)
}