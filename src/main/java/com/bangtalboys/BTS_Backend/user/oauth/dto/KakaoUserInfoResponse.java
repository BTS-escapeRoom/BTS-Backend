package com.bangtalboys.BTS_Backend.user.oauth.dto;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResponse implements UserInfoResponse {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        private KakaoProfile kakaoProfile;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoProfile {
        private String nickname;
        private String profileImageUrl;
    }

    @Override
    public String getNickname() {
        return kakaoAccount.kakaoProfile.nickname;
    }

    @Override
    public String getProfileImg() {
        return kakaoAccount.kakaoProfile.profileImageUrl;
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }

    @Override
    public Long getSocialId() {
        return id;
    }
}