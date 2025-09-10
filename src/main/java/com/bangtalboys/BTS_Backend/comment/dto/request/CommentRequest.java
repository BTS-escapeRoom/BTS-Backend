package com.bangtalboys.BTS_Backend.comment.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequest {
    private Long boardId;
    private String comment;
}
