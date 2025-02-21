package com.bangtalboys.BTS_Backend.board.repository;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM  Board b " +
            "JOIN BoardLike bl ON b.id = bl.board.id " +
            "WHERE bl.member.id = :memberId")
    List<Board> findBoardByMemberId(
            @Param("memberId") Long memberId);

    @Query("SELECT b FROM Board b " +
            "WHERE (:keyword IS NULL OR b.title LIKE %:keyword% OR b.description LIKE %:keyword%)" +
            "AND (:type IS NULL OR b.type = :type)")
    List<Board> searchBoardByKeyword(@Param("keyword") String keyword, @Param("type") String type);
}