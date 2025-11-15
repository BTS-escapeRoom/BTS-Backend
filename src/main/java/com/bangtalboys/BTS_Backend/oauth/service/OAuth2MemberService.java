package com.bangtalboys.BTS_Backend.oauth.service;

import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.oauth.dto.OAuth2Response;
import com.bangtalboys.BTS_Backend.oauth.dto.UserDto;
import com.bangtalboys.BTS_Backend.utils.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuth2MemberService {

    private final MemberRepository memberRepository;

    /**
     * 소셜 로그인 정보를 받아 회원을 조회하거나 신규 가입시킵니다.
     * @param oAuth2Response (KakaoResponse, NaverResponse, AppleResponse 등)
     * @return UserDto (로그인/가입된 회원 정보)
     */
    @Transactional
    public UserDto processOAuth2User(OAuth2Response oAuth2Response) {
        
        Optional<Member> memberOpt = memberRepository.findBySocialTypeAndSocialId(
                oAuth2Response.getSocialType(), oAuth2Response.getSocialId()
        );

        if (memberOpt.isPresent()) {
            // 1. 기존 회원일 경우
            Member member = memberOpt.get();
            return UserDto.builder()
                    .id(member.getId())
                    .profileImg(member.getProfileImg())
                    .nickname(member.getNickname())
                    .socialType(member.getSocialType().toString())
                    .socialId(member.getSocialId())
                    .role(member.getRole().toString())
                    .isNewUser(false) // 기존 회원이므로 false
                    .build();
        } else {
            // 2. 신규 회원일 경우
            Member newMember = Member.builder()
                    .socialType(oAuth2Response.getSocialType())
                    .socialId(oAuth2Response.getSocialId())
                    .role(Role.ROLE_USER)
                    .build();

            Member saved = memberRepository.save(newMember);

            return UserDto.builder()
                    .id(saved.getId())
                    .socialType(saved.getSocialType().toString())
                    .socialId(saved.getSocialId())
                    .role(saved.getRole().toString())
                    .isNewUser(true) // 신규 회원이므로 true
                    .build();
        }
    }
}