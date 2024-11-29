package com.bangtalboys.BTS_Backend.board.controller;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.dto.request.BoardRequest;
import com.bangtalboys.BTS_Backend.board.dto.request.UpdateBoardRequest;
import com.bangtalboys.BTS_Backend.board.dto.response.BoardResponse;
import com.bangtalboys.BTS_Backend.board.dto.response.ListBoardResponse;
import com.bangtalboys.BTS_Backend.board.service.BoardService;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import com.bangtalboys.BTS_Backend.utils.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/boards")
public class BoardController {
    private final BoardService boardService;
    private final JwtUtil jwtUtil;

    @PostMapping("")
    public ResponseEntity<Response<BoardResponse>> createBoard(@RequestBody BoardRequest boardRequest, @RequestHeader("access_token") String accessToken) {
        Long memberId = jwtUtil.getId(accessToken);
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
    public ResponseEntity<Response<BoardResponse>> updateBoard(@PathVariable long boardId, @RequestBody UpdateBoardRequest updateBoardRequest,  @RequestHeader("access_token") String accessToken) {
        Long memberId = jwtUtil.getId(accessToken);
        return ResponseEntity.ok(Response.ok(boardService.updateBoard(boardId, memberId, updateBoardRequest)));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Response<String>> deleteBoard(@PathVariable long boardId, @RequestHeader("access_token") String accessToken) {
        Long memberId = jwtUtil.getId(accessToken);
        return ResponseEntity.ok(Response.ok(boardService.deleteBoard(boardId, memberId)));
    }
}
