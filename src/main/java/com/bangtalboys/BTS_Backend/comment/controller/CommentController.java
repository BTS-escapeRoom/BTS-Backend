package com.bangtalboys.BTS_Backend.comment.controller;

import com.bangtalboys.BTS_Backend.comment.dto.request.CommentRequest;
import com.bangtalboys.BTS_Backend.comment.dto.response.CommentResponse;
import com.bangtalboys.BTS_Backend.comment.service.CommentService;
import com.bangtalboys.BTS_Backend.oauth.dto.CustomOAuth2User;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import com.bangtalboys.BTS_Backend.utils.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name="댓글 API")
@RequestMapping("/v1/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("")
    public ResponseEntity<Response<CommentResponse>> createComment(@RequestBody CommentRequest commentRequest, @AuthenticationPrincipal CustomOAuth2User oauth2User) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(commentService.createBoardComment(commentRequest, memberId)));
    }

    @PutMapping("{commendId}")
    public ResponseEntity<Response<CommentResponse>> updateComment(@PathVariable Long commendId, @RequestBody CommentRequest commentRequest,@AuthenticationPrincipal CustomOAuth2User oauth2User) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(commentService.updateBoardComment(commentRequest, commendId, memberId)));
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<Response<String>> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal CustomOAuth2User oauth2User) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(commentService.deleteBoardComment(commentId, memberId)));
    }


    @Operation(summary = "코멘트 신고/취소 (토글)")
    @PostMapping("/report")
    public ResponseEntity<Response<String>> reportComment(@RequestParam(required = false) Long commentId,@AuthenticationPrincipal CustomOAuth2User oauth2User) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(commentService.createCommentReport(memberId, commentId)));
    }
}
