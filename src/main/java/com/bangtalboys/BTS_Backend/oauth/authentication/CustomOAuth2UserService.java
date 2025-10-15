package com.bangtalboys.BTS_Backend.oauth.authentication;

import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.oauth.dto.*;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.utils.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User oAuth2User;

        // Apple 로그인일 경우, id_token을 직접 파싱하여 oAuth2User를 생성
        String idToken = null;
        if ("apple".equalsIgnoreCase(registrationId)) {
            // 1. id_token 가져오기
            if ("apple".equalsIgnoreCase(registrationId)) {
                Object idTokenObj = userRequest.getAdditionalParameters().get("id_token");
                if (idTokenObj == null) {
                    throw new OAuth2AuthenticationException("Apple ID token not found");
                }
                idToken = idTokenObj.toString();
            }
            
            // 2. id_token의 payload를 디코딩하여 attributes 맵 생성
            Map<String, Object> attributes = decodeIdTokenPayload(idToken);
            
            // 3. attributes 맵과 nameAttributeKey("sub")를 이용하여 DefaultOAuth2User 생성
            oAuth2User = new DefaultOAuth2User(Collections.emptySet(), attributes, "sub");
        } else {
            oAuth2User = super.loadUser(userRequest);
        }
        

        OAuth2Response oAuth2Response = getOAuth2Response(userRequest, oAuth2User);

        try {
            Optional<Member> memberOpt = memberRepository.findBySocialTypeAndSocialId(
                    oAuth2Response.getSocialType(), oAuth2Response.getSocialId()
            );

            if (memberOpt.isPresent()) {
                Member member = memberOpt.get();
                UserDto userDto = UserDto.builder()
                        .id(member.getId())
                        .profileImg(member.getProfileImg())
                        .nickname(member.getNickname())
                        .socialType(member.getSocialType().toString())
                        .socialId(member.getSocialId())
                        .role(member.getRole().toString())
                        .isNewUser(false)
                        .build();

                return new CustomOAuth2User(userDto);

            } else {
                Member newMember = Member.builder()
                        .socialType(oAuth2Response.getSocialType())
                        .socialId(oAuth2Response.getSocialId())
                        .role(Role.ROLE_USER)
                        .build();

                Member saved = memberRepository.save(newMember);

                UserDto userDto = UserDto.builder()
                        .id(saved.getId())
                        .socialType(saved.getSocialType().toString())
                        .socialId(saved.getSocialId())
                        .role(saved.getRole().toString())
                        .isNewUser(true)
                        .build();

                return new CustomOAuth2User(userDto);
            }

        } catch (Exception e) {
            throw new OAuth2AuthenticationException("서버 내부 오류로 인해 로그인에 실패했습니다.");
        }
    }

    private Map<String, Object> decodeIdTokenPayload(String idToken) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String payload = idToken.split("\\.")[1];
            byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
            String decodedPayload = new String(decodedBytes);
            return objectMapper.readValue(decodedPayload, new TypeReference<>() {});
        } catch (Exception e) {
            throw new OAuth2AuthenticationException("id_token을 파싱하는데 실패했습니다.");
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