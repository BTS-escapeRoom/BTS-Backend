package com.bangtalboys.BTS_Backend.board.repository;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BoardRepository extends JpaRepository<Board, Long> {}