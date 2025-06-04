package com.bangtalboys.BTS_Backend.oauth.dto;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;

public interface OAuth2Response {
    SocialType getSocialType();
    String getSocialId();
}
