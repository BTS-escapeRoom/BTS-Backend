package com.bangtalboys.BTS_Backend.board.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateBoardRequest {
    private String title;
    private String description;

    public UpdateBoardRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
