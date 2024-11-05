package com.bangtalboys.BTS_Backend.board.service;


import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.dto.BoardRequest;
import com.bangtalboys.BTS_Backend.board.dto.UpdateBoardRequest;
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

    public Board createBoard(BoardRequest boardRequest, Long memberId) {
        Board board = Board.builder()
                .memberId(memberId)
                .themeId(boardRequest.getThemeId())
                .type(boardRequest.getType())
                .title(boardRequest.getTitle())
                .description(boardRequest.getDescription())
                .hit(0L)
                .build();

        return boardRepository.save(board);
    }

    public Board getOneBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundException::new);

        board.setHit(board.getHit()+1);
        return boardRepository.save(board);
    }

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public Board updateBoard(Long boardId, Long memberId, UpdateBoardRequest updateBoardRequest) {
        Board board = getOneBoard(boardId);
        if (!board.getMemberId().equals(memberId)) {
            throw new ForbiddenException();
        }
        board.setTitle(updateBoardRequest.getTitle());
        board.setDescription(updateBoardRequest.getDescription());
        return boardRepository.save(board);
    }

    public String deleteBoard(Long boardId, Long userId) {
        Board board = getOneBoard(boardId);
        if (!board.getMemberId().equals(userId)) {
            throw new ForbiddenException();
        }

        boardRepository.delete(board);
        return "Board deleted";
    }
}