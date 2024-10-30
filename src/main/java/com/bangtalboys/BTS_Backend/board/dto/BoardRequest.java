package com.bangtalboys.BTS_Backend.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequest {
    private Long themeId;
    private String type;
    private String title;
    private String description;

    public BoardRequest(Long themeId,String type, String title, String description) {
        this.themeId = themeId;
        this.type = type;
        this.title = title;
        this.description = description;
    }
}