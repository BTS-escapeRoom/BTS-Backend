package com.bangtalboys.BTS_Backend.oauth.dto;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppleResponse implements OAuth2Response {
    private final String socialId;
    public AppleResponse(Map<String, Object> attribute) {
        this.socialId = attribute.get("id").toString();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.APPLE;
    }

    @Override
    public String getSocialId() {
        return socialId;
    }
}
