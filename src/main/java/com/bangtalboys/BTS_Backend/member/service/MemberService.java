package com.bangtalboys.BTS_Backend.member.service;

import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.member.dto.MemberRequest;
import com.bangtalboys.BTS_Backend.member.dto.MemberResponse;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

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
}
