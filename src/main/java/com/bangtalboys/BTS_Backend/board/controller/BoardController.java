package com.bangtalboys.BTS_Backend.board.controller;

import com.bangtalboys.BTS_Backend.board.dto.request.BoardRequest;
import com.bangtalboys.BTS_Backend.board.dto.request.UpdateBoardRequest;
import com.bangtalboys.BTS_Backend.board.dto.response.BoardResponse;
import com.bangtalboys.BTS_Backend.board.dto.response.ListBoardResponse;
import com.bangtalboys.BTS_Backend.board.service.BoardService;
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
@RequestMapping("/v1/boards")
@Tag(name="게시판 API")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("")
    public ResponseEntity<Response<BoardResponse>> createBoard(@RequestBody BoardRequest boardRequest, @AuthenticationPrincipal CustomOAuth2User oauth2User) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(boardService.createBoard(boardRequest, memberId)));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Response<BoardResponse>> getBoard(@PathVariable long boardId) {
        return ResponseEntity.ok(Response.ok(boardService.getOneBoard(boardId)));
    }

    @GetMapping("")
    public ResponseEntity<Response<List<ListBoardResponse>>> getAllBoards() {
        return ResponseEntity.ok(Response.ok(boardService.getAllBoards()));
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<Response<BoardResponse>> updateBoard(@PathVariable long boardId, @RequestBody UpdateBoardRequest updateBoardRequest,@AuthenticationPrincipal CustomOAuth2User oauth2User) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(boardService.updateBoard(boardId, memberId, updateBoardRequest)));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Response<String>> deleteBoard(@PathVariable long boardId,  @AuthenticationPrincipal CustomOAuth2User oauth2User) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(boardService.deleteBoard(boardId, memberId)));
    }

    @Operation(summary = "게시글 찜 설정/취소 (토글)")
    @PostMapping("/like")
    public ResponseEntity<Response<String>> createBoardLike(
            @AuthenticationPrincipal CustomOAuth2User oauth2User,
            @RequestParam(required = false) Long boardId
    ) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(boardService.createBoardLike(memberId, boardId)));
    }

    @Operation(summary = "내가 찜한 게시글 조회")
    @GetMapping("/like")
    public ResponseEntity<Response<List<ListBoardResponse>>> getMemberLikeBoards(
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {
        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(boardService.getLikeBoard(memberId)));
    }

    @Operation(summary = "게시글 검색 (타이틀, 내용)")
    @GetMapping("/search")
    public ResponseEntity<Response<List<ListBoardResponse>>> searchBoards(
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(Response.ok(boardService.searchBoard(keyword)));
    }
}