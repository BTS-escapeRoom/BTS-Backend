package com.bangtalboys.BTS_Backend.comment.service;

import com.bangtalboys.BTS_Backend.comment.domain.Comment;
import com.bangtalboys.BTS_Backend.comment.dto.CommentRequest;
import com.bangtalboys.BTS_Backend.comment.repository.CommentRepository;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment createBoardComment(CommentRequest commentRequest, Long boardId, Long memberId) {
        Comment comment = Comment.builder()
                .userId(memberId)
                .boardId(boardId)
                .comment(commentRequest.getComment())
                .build();
        return commentRepository.save(comment);
    }

    public Comment getOneBoardComment(Long id,Long memberId) {
        Comment comment = commentRepository.findByIdAndMemberId(id, memberId);
        if (comment == null) {
            throw new NotFoundException();
        }
        return comment;
    }

    public Comment updateBoardComment(CommentRequest commentRequest, Long id,Long memberId) {
        Comment comment = commentRepository.findByIdAndMemberId(id,memberId);
        if (comment == null) {
            throw new NotFoundException();
        }
        comment.setComment(commentRequest.getComment());
        return commentRepository.save(comment);
    }

    public List<Comment> getBoardComments(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

    public String deleteBoardComment(Long id, Long memberId) {
        Comment comment = getOneBoardComment(id ,memberId);
        if (comment == null) {
            throw new NotFoundException();
        }
        commentRepository.delete(comment);
        return "Comment deleted";
    }
}
