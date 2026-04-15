package com.bangtalboys.BTS_Backend.board.repository;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.domain.BoardReport;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardReportRepository extends JpaRepository<BoardReport, Long> {
    Optional<BoardReport> findByMemberAndBoard(Member memeber, Board board);
    @Query("select br.status from BoardReport br where br.member.id = :memberId and br.board.id = :boardId")
    Optional<String> findStatusByMemberIdAndBoardId(@Param("memberId") Long memberId, @Param("boardId") Long boardId);
    @Query("select br.board.id from BoardReport br where br.member.id = :memberId and br.board.id in :boardIds")
    List<Long> findReportedBoardIds(@Param("memberId") Long memberId, @Param("boardIds") List<Long> boardIds);

}
