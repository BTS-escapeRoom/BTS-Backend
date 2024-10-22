package com.bangtalboys.BTS_Backend.oauth.service;

import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.oauth.dto.CustomOAuth2User;
import com.bangtalboys.BTS_Backend.oauth.dto.KakaoResponse;
import com.bangtalboys.BTS_Backend.oauth.dto.OAuth2Response;
import com.bangtalboys.BTS_Backend.oauth.dto.UserDto;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.utils.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

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
        } else {
            return null;
        }

        String username = oAuth2Response.getSocialType() + oAuth2Response.getSocialId();
        Member existData = memberRepository.findByUsername(username);

        if (existData == null) {
            Member member = Member.builder()
                    .username(username)
                    .profileImg(oAuth2Response.getProfileImg())
                    .nickname(oAuth2Response.getNickname())
                    .socialType(oAuth2Response.getSocialType())
                    .role(Role.ROLE_USER)
                    .build();

            memberRepository.save(member);
            UserDto userDto = UserDto.builder()
                    .username(username)
                    .profileImg(oAuth2Response.getProfileImg())
                    .nickname(oAuth2Response.getNickname())
                    .socialType(oAuth2Response.getSocialType())
                    .role(Role.ROLE_USER)
                    .build();

            return new CustomOAuth2User(userDto);
        } else {
            UserDto userDto = UserDto.builder()
                    .username(username)
                    .profileImg(existData.getProfileImg())
                    .nickname(existData.getNickname())
                    .socialType(existData.getSocialType())
                    .role(existData.getRole())
                    .build();

            return new CustomOAuth2User(userDto);
        }
    }
}