package com.bangtalboys.BTS_Backend.board.service;


import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.dto.BoardRequest;
import com.bangtalboys.BTS_Backend.board.repository.BoardRepository;
import com.bangtalboys.BTS_Backend.config.error.exception.ForbiddenException;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Board getOneBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(NotFoundException::new);
    }

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public Board updateBoard(Long boardId, Long userId, BoardRequest boardRequest) {
        Board board = getOneBoard(boardId);
        if (!board.getUserId().equals(userId)) {
            throw new ForbiddenException();
        }
        board.setTitle(boardRequest.getTitle());
        board.setDescription(boardRequest.getDescription());
        return boardRepository.save(board);
    }

    public String deleteBoard(Long boardId, Long userId) {
        Board board = getOneBoard(boardId);
        if (!board.getUserId().equals(userId)) {
            throw new ForbiddenException();
        }

        boardRepository.delete(board);
        return "Board deleted";
    }
}