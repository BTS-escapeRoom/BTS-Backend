package com.bangtalboys.BTS_Backend.oauth.provider;

import com.bangtalboys.BTS_Backend.oauth.dto.AppSocialLoginRequest;
import com.bangtalboys.BTS_Backend.oauth.dto.OAuth2Response;

public interface SocialLoginProvider {
    OAuth2Response getUserProfile(AppSocialLoginRequest request);
}

