package com.bangtalboys.BTS_Backend.board.service;


import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.dto.BoardRequest;
import com.bangtalboys.BTS_Backend.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Board createBoard(BoardRequest boardRequest) {
        Long dummyUserId = 1L;
        Board board = Board.builder()
                .userId(dummyUserId)
                .themeId(boardRequest.getThemeId())
                .type(boardRequest.getType())
                .title(boardRequest.getTitle())
                .description(boardRequest.getDescription())
                .build();

        return boardRepository.save(board);
    }

    public Board getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) {

        }

    }
}