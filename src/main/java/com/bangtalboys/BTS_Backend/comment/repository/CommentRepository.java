package com.bangtalboys.BTS_Backend.comment.repository;

import com.bangtalboys.BTS_Backend.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByIdAndMemberId(Long id, Long memberId);
    List<Comment> findByBoardId(Long boardId);
}
