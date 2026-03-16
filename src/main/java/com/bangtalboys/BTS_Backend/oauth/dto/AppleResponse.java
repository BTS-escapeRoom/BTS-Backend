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
        // Apple의 경우 ID 토큰에서 sub 클레임을 사용
        this.socialId = attribute.get("sub").toString();
    }
    
    // Apple의 경우 추가 파라미터에서 정보를 가져오는 생성자도 제공
    public AppleResponse(String socialId) {
        this.socialId = socialId;
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.APPLE;
    }

    @Override
    public String getSocialId() {
        return socialId;
    }

    @Override
    public String getProfileImage() {
        return null;
    }
}
