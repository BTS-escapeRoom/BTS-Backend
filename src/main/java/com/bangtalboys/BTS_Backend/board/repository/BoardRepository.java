package com.bangtalboys.BTS_Backend.board.repository;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {

    @Query("SELECT b FROM  Board b " +
            "JOIN BoardLike bl ON b.id = bl.board.id " +
            "WHERE bl.member.id = :memberId")
    List<Board> findLikedBoardByMemberId(
            @Param("memberId") Long memberId);
    List<Board> findAllByMemberId(Long memberId);
    @Query("""
    SELECT b FROM Board b
    LEFT JOIN FETCH b.member
    LEFT JOIN FETCH b.theme t
    LEFT JOIN FETCH t.store
    WHERE b.id = :id
""")
    Optional<Board> findDetailById(@Param("id") Long id);
}