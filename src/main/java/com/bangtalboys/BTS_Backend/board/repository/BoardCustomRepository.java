package com.bangtalboys.BTS_Backend.board.repository;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.dto.request.BoardListRequest;

import java.util.List;

public interface BoardCustomRepository {
    List<Board> findBoards(BoardListRequest boardListRequest);
    long countBoards(BoardListRequest boardListRequest);
}
