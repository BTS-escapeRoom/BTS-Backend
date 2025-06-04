package com.bangtalboys.BTS_Backend.oauth.authentication;

import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.oauth.dto.*;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.utils.enums.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("apple")) {
            oAuth2Response = new AppleResponse(userRequest.getAdditionalParameters());
        } else {
            return null;
        }

        System.out.println("-----------------------------");
        System.out.println(oAuth2Response.getSocialId());
        System.out.println(oAuth2Response.getSocialType());
        Member existData = memberRepository.findBySocialTypeAndSocialId(oAuth2Response.getSocialType(), oAuth2Response.getSocialId());

        if (existData == null) {
            Member member = Member.builder()
                    .socialType(oAuth2Response.getSocialType())
                    .socialId(oAuth2Response.getSocialId())
                    .role(Role.ROLE_USER)
                    .build();

            memberRepository.save(member);
            UserDto userDto = UserDto.builder()
                    .id(member.getId())
                    .socialType(oAuth2Response.getSocialType().toString())
                    .socialId(oAuth2Response.getSocialId())
                    .role(Role.ROLE_USER)
                    .build();

            return new CustomOAuth2User(userDto);
        } else {
            UserDto userDto = UserDto.builder()
                    .id(existData.getId())
                    .profileImg(existData.getProfileImg())
                    .nickname(existData.getNickname())
                    .socialType(existData.getSocialType().toString())
                    .socialId(oAuth2Response.getSocialId())
                    .role(existData.getRole())
                    .build();

            return new CustomOAuth2User(userDto);
        }
    }
}