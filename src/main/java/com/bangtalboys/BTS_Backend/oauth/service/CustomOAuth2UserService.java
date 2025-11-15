package com.bangtalboys.BTS_Backend.oauth.service;

import com.bangtalboys.BTS_Backend.oauth.dto.AppleResponse;
import com.bangtalboys.BTS_Backend.oauth.dto.KakaoResponse;
import com.bangtalboys.BTS_Backend.oauth.dto.NaverResponse;
import com.bangtalboys.BTS_Backend.oauth.dto.OAuth2Response;
import com.bangtalboys.BTS_Backend.oauth.dto.UserDto;
import com.bangtalboys.BTS_Backend.oauth.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final OAuth2MemberService oAuth2MemberService;

    private final OidcUserService oidcUserService = new OidcUserService();

    private final DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User oAuth2User;

        if (registrationId.equals("apple")) {
            if (!(userRequest instanceof OidcUserRequest)) {
                throw new OAuth2AuthenticationException("Apple OAuth 요청이 OIDC 요청이 아닙니다.");
            }
            OidcUser oidcUser = oidcUserService.loadUser((OidcUserRequest) userRequest);
            oAuth2User = oidcUser;
        } else {
            oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
        }
        

        OAuth2Response oAuth2Response = getOAuth2Response(userRequest, oAuth2User);

        try {
            UserDto userDto = oAuth2MemberService.processOAuth2User(oAuth2Response);
            return new CustomOAuth2User(userDto);
            

        } catch (Exception e) {
            throw new OAuth2AuthenticationException("서버 내부 오류로 인해 로그인에 실패했습니다.");
        }
    }


    private static OAuth2Response getOAuth2Response(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("apple")) {
            // Apple의 경우 ID 토큰에서 사용자 정보를 가져옴
            oAuth2Response = new AppleResponse(oAuth2User.getAttributes());
        } else {
            throw new OAuth2AuthenticationException("Unsupported OAuth provider: " + registrationId);
        }

        return oAuth2Response;
    }
}