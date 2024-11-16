package com.bangtalboys.BTS_Backend.oauth.dto;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

//    @Override
//    public String getNickname() {
//        if (attribute == null) {
//            return "방탈이";
//        }
//
//        String nickname = attribute.get("name").toString();
//        if (Objects.equals(nickname, "null")) {
//            return "방탈이";
//        }
//
//        return nickname;
//    }
//
//    @Override
//    public String getProfileImg() {
//        if (attribute == null) {
//            return null;
//        }
//
//        String profileImg = attribute.get("profile_image").toString();
//        if (Objects.equals(profileImg, "null")) {
//            return null;
//        }
//
//        return profileImg;
//    }

    @Override
    public SocialType getSocialType() {
        return SocialType.NAVER;
    }

    @Override
    public String getSocialId() {
        return attribute.get("id").toString();
    }
}
