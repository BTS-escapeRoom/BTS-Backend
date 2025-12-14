package com.bangtalboys.BTS_Backend.board.dto.request;

import lombok.Data;

@Data
public class BoardReportRequest {
    private Long boardId;
    private String description;
}
