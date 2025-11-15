package com.bangtalboys.BTS_Backend.oauth.dto;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import com.bangtalboys.BTS_Backend.oauth.client.naver.dto.NaverUserInfoResponse;

import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverResponse implements OAuth2Response{

    private final String socialId;

    @SuppressWarnings("unchecked")
    public NaverResponse(Map<String, Object> attribute) {
        Map<String, Object> response = (Map<String, Object>) attribute.get("response");
        this.socialId = response.get("id").toString();
    }

    public NaverResponse(NaverUserInfoResponse userInfo) {
        this.socialId = userInfo.getResponse().getId();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.NAVER;
    }

    @Override
    public String getSocialId() {
        return socialId;
    }
}
