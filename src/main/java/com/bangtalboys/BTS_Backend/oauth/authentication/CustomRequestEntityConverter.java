package com.bangtalboys.BTS_Backend.oauth.authentication;

import com.bangtalboys.BTS_Backend.oauth.util.AppleJwtUtil;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.util.MultiValueMap;

public class CustomRequestEntityConverter extends OAuth2AuthorizationCodeGrantRequestEntityConverter {
    private final AppleJwtUtil appleJwtUtil;

    public CustomRequestEntityConverter(AppleJwtUtil appleJwtUtil) {
        this.appleJwtUtil = appleJwtUtil;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
        RequestEntity<?> entity = super.convert(req);
        if ("apple".equals(req.getClientRegistration().getRegistrationId())) {
            MultiValueMap<String, String> params = (MultiValueMap<String, String>) entity.getBody();
            if (params != null) {
                String privateKey = req.getClientRegistration().getClientSecret();
                params.set("client_secret", appleJwtUtil.generateClientSecret(privateKey));
                return new RequestEntity<>(params, entity.getHeaders(), entity.getMethod(), entity.getUrl());
            }
        }
        return entity;
    }
}