package com.bangtalboys.BTS_Backend.comment.dto.request;

import lombok.Data;

@Data
public class CommentReportRequest {
    private Long commentId;
    private String description;
}
