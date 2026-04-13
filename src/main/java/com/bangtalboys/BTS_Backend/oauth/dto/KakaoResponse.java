package com.bangtalboys.BTS_Backend.oauth.dto;

import com.bangtalboys.BTS_Backend.oauth.client.kakao.dto.KakaoUserInfoResponse;
import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoResponse implements OAuth2Response {
    
    private final String socialId;
    private final String profileImage;

    @SuppressWarnings("unchecked")
    public KakaoResponse(Map<String, Object> attribute) {
        this.socialId = attribute.get("id").toString();

        Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
        if (kakaoAccount != null) {
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile != null) {
                Object profileImg = profile.get("thumbnail_image_url");
                this.profileImage = profileImg != null ? profileImg.toString() : null;
            } else {
                this.profileImage = null;
            }
        } else {
            this.profileImage = null;
        }
    }

    public KakaoResponse(KakaoUserInfoResponse userInfo) {
        this.socialId = userInfo.getId().toString();
    
        KakaoUserInfoResponse.KakaoAccount kakaoAccount = userInfo.getKakaoAccount();
        if (kakaoAccount != null && kakaoAccount.getProfile() != null) {
            this.profileImage = kakaoAccount.getProfile().getThumbnailImageUrl();
        } else {
            this.profileImage = null;
        }
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }

    @Override
    public String getSocialId() {
        return socialId;
    }

    @Override
    public String getProfileImage() {
        return profileImage;
    }

}