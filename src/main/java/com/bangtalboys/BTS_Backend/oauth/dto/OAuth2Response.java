package com.bangtalboys.BTS_Backend.oauth.dto;

import com.bangtalboys.BTS_Backend.utils.enums.Role;
import com.bangtalboys.BTS_Backend.utils.enums.SocialType;

public interface OAuth2Response {
    String getProfileImg();
    String getNickname();
    SocialType getSocialType();
    String getSocialId();
}
