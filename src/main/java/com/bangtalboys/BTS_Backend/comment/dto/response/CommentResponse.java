package com.bangtalboys.BTS_Backend.comment.dto.response;

import com.bangtalboys.BTS_Backend.comment.domain.Comment;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentResponse {
    private Long id;
    private String comment;
    private Long memberId;
    private String memberName;
    @JsonProperty("isDeleted")
    private boolean isDeleted;
    @JsonProperty("isReported")
    private boolean isReported;

    public CommentResponse(Comment comment, boolean isReported, boolean isDeleted) {
        this.id = comment.getId();
        this.comment = isDeleted ? "삭제된 댓글입니다." : comment.getComment(); // 🌟 UI friendly
        this.memberId = comment.getMember().getId();
        this.memberName = comment.getMember().getNickname();
        this.isDeleted = isDeleted;
        this.isReported = isReported;
    }
}