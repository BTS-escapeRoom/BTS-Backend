package com.bangtalboys.BTS_Backend.board.controller;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.dto.BoardRequest;
import com.bangtalboys.BTS_Backend.board.dto.UpdateBoardRequest;
import com.bangtalboys.BTS_Backend.board.service.BoardService;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.oauth.dto.CustomOAuth2User;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import com.bangtalboys.BTS_Backend.utils.Response.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/boards")
public class BoardController {
    private final BoardService boardService;
    private final JwtUtil jwtUtil;

    @PostMapping("")
    public ResponseEntity<Response<Board>> createBoard(@RequestBody BoardRequest boardRequest, @RequestHeader("access_token") String accessToken) {
        Long memberId = jwtUtil.getId(accessToken);
        return ResponseEntity.ok(Response.ok(boardService.createBoard(boardRequest, memberId)));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Response<Board>> getBoard(@PathVariable long boardId) {
        return ResponseEntity.ok(Response.ok(boardService.getOneBoard(boardId)));
    }

    @GetMapping("")
    public ResponseEntity<Response<List<Board>>> getAllBoards() {
        return ResponseEntity.ok(Response.ok(boardService.getAllBoards()));
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<Response<Board>> updateBoard(@PathVariable long boardId, @RequestBody UpdateBoardRequest updateBoardRequest,  @RequestHeader("access_token") String accessToken) {
        Long memberId = jwtUtil.getId(accessToken);
        return ResponseEntity.ok(Response.ok(boardService.updateBoard(boardId, memberId, updateBoardRequest)));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Response<String>> deleteBoard(@PathVariable long boardId, @RequestHeader("access_token") String accessToken) {
        Long memberId = jwtUtil.getId(accessToken);
        return ResponseEntity.ok(Response.ok(boardService.deleteBoard(boardId, memberId)));
    }
}
