package com.bangtalboys.BTS_Backend.oauth.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OidcUser {

    private final UserDto userDTO;

    public CustomOAuth2User(UserDto userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                return userDTO.getRole().toString();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return userDTO.getSocialId();
    }

    public String getNickname() {
        return userDTO.getNickname();
    }

    public String getSocialType() {
        return userDTO.getSocialType();
    }

    public String getSocialId() {
        return userDTO.getSocialId();
    }

    public boolean getIsNewUser() {
        return userDTO.getIsNewUser();
    }

    public Long getId() {
        return userDTO.getId();
    }

    @Override
    public Map<String, Object> getClaims() {
        return null; // 필요하다면 attributes를 리턴
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null; // 우리는 UserInfo 엔드포인트를 안 쓰므로 null
    }

    @Override
    public OidcIdToken getIdToken() {
        return null; // 토큰 자체를 저장할 게 아니므로 null
    }
    
}
