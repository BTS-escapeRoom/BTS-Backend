package com.bangtalboys.BTS_Backend.member.service;

import com.bangtalboys.BTS_Backend.config.error.ErrorCode;
import com.bangtalboys.BTS_Backend.config.error.exception.BusinessBaseException;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.member.dto.*;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.utils.enums.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final UnlinkService unlinkService;

    public MemberResponse createMember(MemberCreateRequest req) {
        Member member = Member.builder()
                .profileImg(req.getProfileImg())
                .nickname(req.getNickname())
                .socialType(req.getSocialType())
                .socialId(req.getSocialId())
                .role(Role.ROLE_USER)
                .build();

        memberRepository.save(member);
        return new MemberResponse(member);
    }

    public MemberResponse getOneMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return member.map(MemberResponse::new).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public MemberResponse updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);

        member.setProfileImg(memberUpdateRequest.getProfileImg());
        member.setNickname(memberUpdateRequest.getNickname());
        member.setDescription(memberUpdateRequest.getDescription());

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

    @Transactional
    public MemberCheckSignupResponse checkSignupMember(MemberCheckSignupRequest req) {
        Optional<Member> memberOpt = memberRepository.findBySocialTypeAndSocialId(req.getSocialType(), req.getSocialId());

        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            return new MemberCheckSignupResponse(true, member.getId());
        }

        return new MemberCheckSignupResponse(false, null);
    }
}
