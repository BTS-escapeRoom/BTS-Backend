package com.bangtalboys.BTS_Backend.board.repository;

import com.bangtalboys.BTS_Backend.board.domain.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
}
