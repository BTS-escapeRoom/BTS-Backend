package com.bangtalboys.BTS_Backend.comment.service;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.repository.BoardRepository;
import com.bangtalboys.BTS_Backend.comment.domain.Comment;
import com.bangtalboys.BTS_Backend.comment.domain.CommentReport;
import com.bangtalboys.BTS_Backend.comment.dto.request.CommentReportRequest;
import com.bangtalboys.BTS_Backend.comment.dto.request.CommentRequest;
import com.bangtalboys.BTS_Backend.comment.dto.response.CommentListResponse;
import com.bangtalboys.BTS_Backend.comment.dto.response.CommentResponse;
import com.bangtalboys.BTS_Backend.comment.repository.CommentReportRepository;
import com.bangtalboys.BTS_Backend.comment.repository.CommentRepository;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.utils.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentReportRepository commentReportRepository;

    /** 댓글 생성 */
    public CommentResponse createBoardComment(CommentRequest commentRequest, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundException::new);

        Board board = boardRepository.findById(commentRequest.getBoardId())
                .orElseThrow(NotFoundException::new);

        Comment comment = new Comment(member, board, commentRequest.getComment());
        commentRepository.save(comment);

        return new CommentResponse(comment, false, false);  // 신고 X, 삭제 X
    }

    /** 댓글 수정 */
    public CommentResponse updateBoardComment(CommentRequest commentRequest, Long id, Long memberId) {
        Comment comment = commentRepository.findByIdAndMemberId(id, memberId);
        if (comment == null || comment.isDeleted()) {
            throw new NotFoundException();
        }

        comment.setComment(commentRequest.getComment());
        commentRepository.save(comment);

        return new CommentResponse(comment, false, comment.isDeleted());
    }

    /** 댓글 목록 조회 */
    public CommentListResponse getBoardComments(Long boardId, Long memberId) {
        List<Comment> comments = commentRepository.findByBoard_Id(boardId);

        List<Long> commentIds = comments.stream()
                .map(Comment::getId)
                .toList();

        Set<Long> reportedCommentIdSet = commentIds.isEmpty()
                ? Collections.emptySet()
                : new HashSet<>(commentReportRepository.findReportedCommentIds(memberId, commentIds));

        List<CommentResponse> responses = comments.stream()
                .map(comment -> new CommentResponse(
                        comment,
                        reportedCommentIdSet.contains(comment.getId()),
                        comment.isDeleted()
                ))
                .toList();

        return new CommentListResponse(responses);
    }

    /** 댓글 삭제 → Soft Delete */
    public String deleteBoardComment(Long id, Long memberId) {
        Comment comment = commentRepository.findByIdAndMemberId(id, memberId);
        if (comment == null) {
            throw new NotFoundException();
        }

        comment.setDeleted(true);
        commentRepository.save(comment);

        return "댓글 삭제 완료";
    }

    /** 댓글 신고/취소 */
    public String createCommentReport(Long memberId, CommentReportRequest commentReportRequest) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundException::new);

        // 🔥 댓글 찾기 버그 수정: 내 댓글이든 남의 댓글이든 신고 가능해야 함
        Comment comment = commentRepository.findById(commentReportRequest.getCommentId())
                .orElseThrow(NotFoundException::new);

        Optional<CommentReport> existReport =
                commentReportRepository.findByMemberAndComment(member, comment);

        if (existReport.isPresent()) {
            commentReportRepository.delete(existReport.get());
            return "댓글 신고 취소 완료";
        }

        CommentReport commentReport = new CommentReport(
                member,
                comment,
                Status.ACTIVE.name(),
                commentReportRequest.getDescription()
        );

        commentReportRepository.save(commentReport);
        return "댓글 신고 완료";
    }
}
