package com.bangtalboys.BTS_Backend.comment.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CommentListResponse {
    private List<CommentResponse> comments;
    private int totalCount;

    public CommentListResponse(List<CommentResponse> comments) {
        this.comments = comments;
        this.totalCount = comments.size();
    }
}
