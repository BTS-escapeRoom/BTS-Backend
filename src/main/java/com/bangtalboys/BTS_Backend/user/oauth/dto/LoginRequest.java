package com.bangtalboys.BTS_Backend.user.oauth.dto;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import org.springframework.util.MultiValueMap;

public interface LoginRequest {
    SocialType socialType();
    MultiValueMap<String, String> makeBody();
}
