package com.bangtalboys.BTS_Backend.user.oauth.dto;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@NoArgsConstructor
public class KakaoLoginRequest implements LoginRequest {
    private String authorizationCode;

    @Override
    public SocialType socialType() {
        return SocialType.KAKAO;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        return body;
    }
}
