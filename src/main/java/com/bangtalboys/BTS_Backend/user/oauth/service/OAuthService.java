package com.bangtalboys.BTS_Backend.user.oauth.service;

import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import com.bangtalboys.BTS_Backend.user.oauth.client.OAuthApiClient;
import com.bangtalboys.BTS_Backend.user.oauth.dto.LoginRequest;
import com.bangtalboys.BTS_Backend.user.oauth.dto.UserInfoResponse;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OAuthService {
    private final Map<SocialType, OAuthApiClient> clients;

    public OAuthService(List<OAuthApiClient> clients) {
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OAuthApiClient::socialType, Function.identity())
        );
    }

    public UserInfoResponse request(LoginRequest params) {
        OAuthApiClient client = clients.get(params.socialType());
        String accessToken = client.getAccessToken(params);
        return client.getUserInfo(accessToken);
    }
}