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


public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM  Board b " +
            "JOIN BoardLike bl ON b.id = bl.board.id " +
            "WHERE bl.member.id = :memberId")
    List<Board> findLikedBoardByMemberId(
            @Param("memberId") Long memberId);

    List<Board> findAllByMemberId(Long memberId);


    // 인기순
    @Query(
            value = "SELECT b FROM Board b " +
                    "LEFT JOIN b.likes l " +
                    "WHERE (:keyword IS NULL OR b.title LIKE %:keyword%) " +
                    "  AND (:type IS NULL OR b.type = :type) " +
                    "GROUP BY b " +
                    "ORDER BY COUNT(l) DESC",
            countQuery = "SELECT COUNT(b) FROM Board b " +
                    "WHERE (:keyword IS NULL OR b.title LIKE %:keyword%) " +
                    "  AND (:type IS NULL OR b.type = :type)"
    )
    Page<Board> searchBoardByKeywordOrderByLikes(
            @Param("keyword") String keyword,
            @Param("type") BoardType type,
            Pageable pageable
    );

    // 날짜순·조회순 등 Sort 처리용
    @Query("SELECT b FROM Board b "
            + "WHERE (:keyword IS NULL OR b.title LIKE %:keyword%) "
            + "  AND (:type IS NULL OR b.type = :type)")
    Page<Board> searchBoardByKeyword(
            @Param("keyword") String keyword,
            @Param("type")    BoardType type,
            Pageable pageable
    );


}