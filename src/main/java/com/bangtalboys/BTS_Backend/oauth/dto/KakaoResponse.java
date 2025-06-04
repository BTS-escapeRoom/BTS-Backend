package com.bangtalboys.BTS_Backend.oauth.dto;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoResponse implements OAuth2Response {
    private final String socialId;
//    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {
        this.socialId = attribute.get("id").toString();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }

    @Override
    public String getSocialId() {
        return socialId;
    }

}