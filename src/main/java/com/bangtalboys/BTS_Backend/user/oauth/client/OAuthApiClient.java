package com.bangtalboys.BTS_Backend.user.oauth.client;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import com.bangtalboys.BTS_Backend.user.oauth.dto.LoginRequest;
import com.bangtalboys.BTS_Backend.user.oauth.dto.UserInfoResponse;

public interface OAuthApiClient {
    SocialType socialType();
    String getAccessToken(LoginRequest params);
    UserInfoResponse getUserInfo(String accessToken);
}