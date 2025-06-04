package com.bangtalboys.BTS_Backend.member.controller;

import com.bangtalboys.BTS_Backend.member.dto.MemberRequest;
import com.bangtalboys.BTS_Backend.member.dto.MemberResponse;
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
            @RequestBody MemberRequest memberRequest
    ) {

        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(memberService.updateMember(memberId, memberRequest)));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("")
    public ResponseEntity<?> DeleteMember(
            @AuthenticationPrincipal CustomOAuth2User oauth2User,
            @RequestHeader("naverAccessToken") String naverAccessToken
    ) {

        Long memberId = oauth2User.getId();
        memberService.deleteMember(memberId, naverAccessToken);

        return ResponseEntity.ok(null);
    }
}