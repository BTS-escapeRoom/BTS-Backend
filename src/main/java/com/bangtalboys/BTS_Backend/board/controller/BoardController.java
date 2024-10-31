package com.bangtalboys.BTS_Backend.board.controller;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.dto.BoardRequest;
import com.bangtalboys.BTS_Backend.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/board")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("")
    public ResponseEntity<Board> createBoard(@RequestBody BoardRequest boardRequest) {
        return ResponseEntity.ok(boardService.createBoard(boardRequest));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Board> getBoard(@PathVariable long boardId) {
        return ResponseEntity.ok(boardService.getOneBoard(boardId));
    }

    @GetMapping("")
    public ResponseEntity<List<Board>> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }
}
