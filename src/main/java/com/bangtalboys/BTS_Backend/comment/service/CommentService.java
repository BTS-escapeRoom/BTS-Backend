package com.bangtalboys.BTS_Backend.comment.service;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.repository.BoardRepository;
import com.bangtalboys.BTS_Backend.comment.domain.Comment;
import com.bangtalboys.BTS_Backend.comment.domain.CommentReport;
import com.bangtalboys.BTS_Backend.comment.dto.request.CommentRequest;
import com.bangtalboys.BTS_Backend.comment.dto.response.CommentResponse;
import com.bangtalboys.BTS_Backend.comment.repository.CommentReportRepository;
import com.bangtalboys.BTS_Backend.comment.repository.CommentRepository;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentReportRepository commentReportRepository;

    public CommentResponse createBoardComment(CommentRequest commentRequest, Long boardId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundException::new);
        Comment comment = new Comment(member, board, commentRequest.getComment());
        commentRepository.save(comment);
        return new CommentResponse(comment, member);
    }

    public CommentResponse getOneBoardComment(Long id,Long memberId) {
        Comment comment = commentRepository.findByIdAndMemberId(id, memberId);
        if (comment == null) {
            throw new NotFoundException();
        }
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        return new CommentResponse(comment, member);
    }

    public CommentResponse updateBoardComment(CommentRequest commentRequest, Long id,Long memberId) {
        Comment comment = commentRepository.findByIdAndMemberId(id,memberId);
        if (comment == null) {
            throw new NotFoundException();
        }
        comment.setComment(commentRequest.getComment());
        commentRepository.save(comment);

        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        return new CommentResponse(comment, member);
    }

    public List<CommentResponse> getBoardComments(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardId(boardId);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment : comments) {
            commentResponses.add(new CommentResponse(comment, memberRepository.findById(comment.getMember().getId()).orElseThrow(NotFoundException::new)));
        }
        return commentResponses;
    }

    public String deleteBoardComment(Long id, Long memberId) {
        Comment comment = commentRepository.findByIdAndMemberId(id, memberId);
        if (comment == null) {
            throw new NotFoundException();
        }
        commentRepository.delete(comment);
        return "Comment deleted";
    }

    public String createCommentReport(Long memberId, Long CommentId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        Comment comment = commentRepository.findByIdAndMemberId(CommentId, memberId);

        Optional<CommentReport> existReport = commentReportRepository.findByMemberAndComment(member, comment);

        if (existReport.isPresent()) {
           commentReportRepository.delete(existReport.get());
           return "코멘트 신고 취소 완료";
        }
        CommentReport commentReport = new CommentReport(member, comment, "active");
        commentReportRepository.save(commentReport);
        return "코멘트 신고 완료";
    }
}
