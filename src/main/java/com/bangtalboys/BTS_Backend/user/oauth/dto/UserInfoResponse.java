package com.bangtalboys.BTS_Backend.user.oauth.dto;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;

public interface UserInfoResponse {
    String getProfileImg();
    String getNickname();
    SocialType getSocialType();
    Long getSocialId();
}
