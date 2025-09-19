package com.bangtalboys.BTS_Backend.board.repository;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.utils.enums.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {

    @Query("SELECT b FROM  Board b " +
            "JOIN BoardLike bl ON b.id = bl.board.id " +
            "WHERE bl.member.id = :memberId")
    List<Board> findLikedBoardByMemberId(
            @Param("memberId") Long memberId);
    List<Board> findAllByMemberId(Long memberId);
}