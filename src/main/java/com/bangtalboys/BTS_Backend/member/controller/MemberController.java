package com.bangtalboys.BTS_Backend.member.controller;

import com.bangtalboys.BTS_Backend.member.dto.*;
import com.bangtalboys.BTS_Backend.member.service.MemberService;
import com.bangtalboys.BTS_Backend.oauth.dto.CustomOAuth2User;
import com.bangtalboys.BTS_Backend.utils.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name="회원 API")
@RequestMapping("/v1/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "내 회원 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<Response<MemberResponse>> getMember(
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {

        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(memberService.getOneMember(memberId)));
    }


    @Operation(summary = "회원 정보 수정")
    @PutMapping("")
    public ResponseEntity<Response<MemberResponse>> updateMember(
            @AuthenticationPrincipal CustomOAuth2User oauth2User,
            @RequestBody MemberUpdateRequest req
    ) {

        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(memberService.updateMember(memberId, req)));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("")
    public ResponseEntity<Response<?>> DeleteMember(
            @AuthenticationPrincipal CustomOAuth2User oauth2User,
            @RequestHeader(value = "naverAccessToken", required = false) String naverAccessToken
    ) {

        Long memberId = oauth2User.getId();
        memberService.deleteMember(memberId, naverAccessToken);

        return ResponseEntity.ok(Response.ok(null));
    }

    /// 앱 로그인 프로세스
    /// 1. 사용자가 카카오 로그인 시도
    /// 2-1. 신규 멤버(회원 가입x, 첫 연동 계정)이면 정보 제공 동의 페이지 이동 및 정보 제공
    /// 2-2. 기존 멤버(회원 가입o)이면 로그인 완료 및 정보 제공
    /// 3. 정보를 이용해 회원가입 여부 조회 API 호출
    /// 4-1. 비회원이면 닉넴 설정 페이지 이동 및 회원 가입 API 호출
    /// 4-2. 회원이면 홈 이동
    @Operation(summary = "회원가입 여부 조회")
    @PostMapping("/check-signup")
    public ResponseEntity<Response<MemberCheckSignupResponse>> CheckSignupMember(
        @RequestBody MemberCheckSignupRequest req
    ) {

        return ResponseEntity.ok(Response.ok(memberService.checkSignupMember(req)));
    }
}