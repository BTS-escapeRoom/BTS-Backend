package com.bangtalboys.BTS_Backend.member.service;

import com.bangtalboys.BTS_Backend.config.error.ErrorCode;
import com.bangtalboys.BTS_Backend.config.error.exception.BusinessBaseException;
import com.bangtalboys.BTS_Backend.member.client.KakaoUnlinkClient;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.member.dto.MemberRequest;
import com.bangtalboys.BTS_Backend.member.dto.MemberResponse;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.utils.enums.SocialType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final UnlinkService unlinkService;

    public MemberResponse getOneMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return member.map(MemberResponse::new).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public MemberResponse updateMember(Long memberId, MemberRequest memberRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);

        member.setProfileImg(memberRequest.getProfileImg());
        member.setNickname(memberRequest.getNickname());
        member.setDescription(memberRequest.getDescription());

        memberRepository.save(member);
        return new MemberResponse(member);
    }

    @Transactional
    public void deleteMember(Long memberId, String accessToken) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);

        switch (member.getSocialType()) {
            case KAKAO -> {
                unlinkService.unlinkKakao(member.getSocialId());
            }
            case NAVER -> {
                if (accessToken == null || accessToken.isBlank()) {
                    throw new BusinessBaseException(ErrorCode.INVALID_ACCESS_TOKEN);
                }
                unlinkService.unlinkNaver(accessToken);
            }
            case APPLE -> {
                // 탈퇴 처리 없이 내부 데이터만 삭제 + 사용자에게 안내
            }
        }

        // 내부 DB 유저 데이터 삭제
        memberRepository.delete(member);
    }
}
