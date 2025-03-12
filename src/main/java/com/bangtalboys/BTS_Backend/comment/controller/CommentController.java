package com.bangtalboys.BTS_Backend.comment.controller;

import com.bangtalboys.BTS_Backend.comment.domain.Comment;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name="댓글 API")
@RequestMapping("/v1/comments")
public class CommentController {
    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @PostMapping("/boards/{boardId}")
    public ResponseEntity<Response<CommentResponse>> createBoardComment(@RequestBody CommentRequest commentRequest, @PathVariable long boardId, @RequestHeader("Authorization") String accessToken) {
Long memberId = jwtUtil.getId(accessToken);
        return ResponseEntity.ok(Response.ok(commentService.createBoardComment(commentRequest, boardId, memberId)));
    }

    @GetMapping("/{id}/boards")
    public ResponseEntity<Response<CommentResponse>> getOneBoardComment(@PathVariable long id, @RequestHeader("Authorization") String accessToken) {
        Long memberId = jwtUtil.getId(accessToken);
        return ResponseEntity.ok(Response.ok(commentService.getOneBoardComment(id, memberId)));
    }

    @PutMapping("/{id}/boards")
    public ResponseEntity<Response<CommentResponse>> updateBoardComment(@PathVariable long id, @RequestBody CommentRequest commentRequest, @AuthenticationPrincipal CustomOAuth2User oauth2User) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(commentService.updateBoardComment(commentRequest, id, memberId)));
    }

    @GetMapping("/boards/{boardId}")
    public ResponseEntity<Response<List<CommentResponse>>> getBoardComment(@PathVariable long boardId, @AuthenticationPrincipal CustomOAuth2User oauth2User) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(commentService.getBoardComments(boardId)));
    }

    @DeleteMapping("/{id}/boards")
    public ResponseEntity<Response<String>> deleteBoardComment(@PathVariable long id, @AuthenticationPrincipal CustomOAuth2User oauth2User) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(commentService.deleteBoardComment(id, memberId)));
    }


    @Operation(summary = "코멘트 신고/취소 (토글)")
    @PostMapping("/report")
    public ResponseEntity<Response<String>> reportComment(@RequestParam(required = false) Long commentId,@AuthenticationPrincipal CustomOAuth2User oauth2User) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(commentService.createCommentReport(memberId, commentId)));
    }
}
