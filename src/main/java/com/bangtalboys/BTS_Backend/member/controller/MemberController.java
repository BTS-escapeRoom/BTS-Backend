package com.bangtalboys.BTS_Backend.member.controller;

import com.bangtalboys.BTS_Backend.config.error.exception.BusinessBaseException;
import com.bangtalboys.BTS_Backend.member.dto.MemberRequest;
import com.bangtalboys.BTS_Backend.member.dto.MemberResponse;
import com.bangtalboys.BTS_Backend.member.service.MemberService;
import com.bangtalboys.BTS_Backend.utils.Response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name="회원 API")
@RequestMapping("/v1/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 단건 조회")
    @GetMapping("/{memberId}")
    public ResponseEntity<Response<MemberResponse>> updateMember(
            @PathVariable Long memberId
    ) {

        return ResponseEntity.ok(Response.ok(memberService.getOneMember(memberId)));
    }

    @Operation(summary = "회원 수정")
    @PutMapping("/{memberId}")
    public ResponseEntity<Response<MemberResponse>> updateMember(
            @PathVariable Long memberId,
            @RequestBody MemberRequest memberRequest
    ) {

        return ResponseEntity.ok(Response.ok(memberService.updateMember(memberId, memberRequest)));
    }
}