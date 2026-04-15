package com.bangtalboys.BTS_Backend.comment.repository;

import com.bangtalboys.BTS_Backend.comment.domain.Comment;
import com.bangtalboys.BTS_Backend.comment.domain.CommentReport;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
    Optional<CommentReport> findByMemberAndComment(Member member, Comment comment);
    @Query("select cr.comment.id from CommentReport cr where cr.member.id = :memberId and cr.comment.id in :commentIds")
    List<Long> findReportedCommentIds(@Param("memberId") Long memberId, @Param("commentIds") List<Long> commentIds);
}
