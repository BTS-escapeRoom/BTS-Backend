package com.bangtalboys.BTS_Backend.comment.repository;

import com.bangtalboys.BTS_Backend.comment.domain.Comment;
import com.bangtalboys.BTS_Backend.comment.domain.CommentReport;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
    Optional<CommentReport> findByMemberAndComment(Member member, Comment comment);
}
