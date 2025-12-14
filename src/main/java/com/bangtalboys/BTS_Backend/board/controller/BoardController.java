package com.bangtalboys.BTS_Backend.board.controller;

import com.bangtalboys.BTS_Backend.board.dto.request.BoardListRequest;
import com.bangtalboys.BTS_Backend.board.dto.request.BoardReportRequest;
import com.bangtalboys.BTS_Backend.board.dto.request.BoardRequest;
import com.bangtalboys.BTS_Backend.board.dto.request.UpdateBoardRequest;
import com.bangtalboys.BTS_Backend.board.dto.response.BoardListPageResponse;
import com.bangtalboys.BTS_Backend.board.dto.response.BoardResponse;
import com.bangtalboys.BTS_Backend.board.dto.response.BoardListResponse;
import com.bangtalboys.BTS_Backend.board.service.BoardService;
import com.bangtalboys.BTS_Backend.comment.dto.response.CommentListResponse;
import com.bangtalboys.BTS_Backend.comment.service.CommentService;
import com.bangtalboys.BTS_Backend.oauth.dto.CustomOAuth2User;
import com.bangtalboys.BTS_Backend.utils.enums.BoardType;
import com.bangtalboys.BTS_Backend.utils.enums.SortType;
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
@RequestMapping("/v1/boards")
@Tag(name="게시판 API")
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    @Operation(summary = "게시글 작성")
    @PostMapping("")
    public ResponseEntity<Response<BoardResponse>> createBoard(@RequestBody BoardRequest boardRequest, @AuthenticationPrincipal CustomOAuth2User oauth2User) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(boardService.createBoard(boardRequest, memberId)));
    }

    @Operation(summary = "게시글 단건 조회")
    @GetMapping("/{boardId}")
    public ResponseEntity<Response<BoardResponse>> getBoard(@PathVariable long boardId, @AuthenticationPrincipal CustomOAuth2User oauth2User) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(boardService.getOneBoard(boardId, memberId)));
    }

    @Operation(summary = "게시글 목록 조회")
    @GetMapping("")
    public ResponseEntity<Response<BoardListPageResponse>> getAllBoards(@RequestParam(required = false) String keyword, @RequestParam(required = false) boolean isRecruiting, @RequestParam(required = false) BoardType type, @RequestParam(required = false) SortType sortType, @RequestParam(required = false, defaultValue = "1") Integer page) {
        BoardListRequest boardListRequest = new BoardListRequest(keyword, isRecruiting, type, sortType, page);
        return ResponseEntity.ok(Response.ok(boardService.getAllBoards(boardListRequest)));
    }

    @Operation(summary = "게시글 단건 수정")
    @PatchMapping("/{boardId}")
    public ResponseEntity<Response<BoardResponse>> updateBoard(@PathVariable long boardId, @RequestBody UpdateBoardRequest updateBoardRequest,@AuthenticationPrincipal CustomOAuth2User oauth2User) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(boardService.updateBoard(boardId, memberId, updateBoardRequest)));
    }

    @Operation(summary = "게시글 단건 삭제")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Response<String>> deleteBoard(@PathVariable long boardId,  @AuthenticationPrincipal CustomOAuth2User oauth2User) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(boardService.deleteBoard(boardId, memberId)));
    }

    @Operation(summary = "게시글 찜 설정/취소 (토글)")
    @PostMapping("/{boardId}/like")
    public ResponseEntity<Response<String>> createBoardLike(
            @AuthenticationPrincipal CustomOAuth2User oauth2User,
            @PathVariable Long boardId
    ) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(boardService.createBoardLike(memberId, boardId)));
    }

    @Operation(summary = "게시글 신고/취소 (토글)")
    @PostMapping("/report")
    public ResponseEntity<Response<String>> createBoardReport(
            @AuthenticationPrincipal CustomOAuth2User oauth2User,
            @RequestBody BoardReportRequest boardReportRequest
            ) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(boardService.createBoardReport(memberId, boardReportRequest)));
    }

    @Operation(summary = "내가 찜한 게시글 조회")
    @GetMapping("/like")
    public ResponseEntity<Response<List<BoardListResponse>>> getMemberLikeBoards(
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(boardService.getLikeBoard(memberId)));
    }

    @Operation(summary =  "내가 쓴 게시글 조회")
    @GetMapping("/my")
    public ResponseEntity<Response<List<BoardListResponse>>> getMyBoards(
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(boardService.getMyBoardList(memberId)));
    }

    @Operation(summary = "게시글 댓글 목록 조회")
    @GetMapping("/{boardId}/comments")
    public ResponseEntity<Response<CommentListResponse>> getBoardComments( @PathVariable Long boardId) {
        return ResponseEntity.ok(Response.ok(commentService.getBoardComments(boardId)));
    }

    @PatchMapping("/{boardId}/close-recruit")
    public ResponseEntity<Response<String>> closeRecruit(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        Long memberId = oauth2User.getId();
        String message = boardService.closeRecruit(boardId, memberId);
        return ResponseEntity.ok(Response.ok(message));
    }
}