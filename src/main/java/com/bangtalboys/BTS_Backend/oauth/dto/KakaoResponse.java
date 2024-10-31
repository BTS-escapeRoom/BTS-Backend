package com.bangtalboys.BTS_Backend.oauth.dto;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoResponse implements OAuth2Response {
    public static String socialId;
    public static Map<String, Object> account;
    public static Map<String, Object> profile;

    public KakaoResponse(Map<String, Object> attribute) {
        socialId = String.valueOf(attribute.get("id"));
        account = (Map<String, Object>) attribute.get("kakao_account");
        profile = (Map<String, Object>) account.get("profile");
    }

    @Override
    public String getNickname() {
        if (profile == null) {
            return "방탈이";
        }

        String nickname = String.valueOf(profile.get("nickname"));
        if (Objects.equals(nickname, "null")) {
            return "방탈이";
        }

        return nickname;
    }

    @Override
    public String getProfileImg() {
        if (profile == null) {
            return null;
        }

        String profileImg = String.valueOf(profile.get("profile_img_url"));
        if (Objects.equals(profileImg, "null")) {
            return null;
        }

        return profileImg;
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