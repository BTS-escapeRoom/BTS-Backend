package com.bangtalboys.BTS_Backend.comment.dto.response;

import com.bangtalboys.BTS_Backend.comment.domain.Comment;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import lombok.Data;

@Data
public class CommentResponse {
    private Long id;
    private String comment;
    private String memberName;

    public CommentResponse(Comment comment, Member member) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.memberName = member.getNickname();
    }
}
