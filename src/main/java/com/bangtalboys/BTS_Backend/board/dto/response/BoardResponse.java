package com.bangtalboys.BTS_Backend.board.dto.response;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.domain.BoardReport;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeListResponse;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeResponse;
import lombok.Data;

import java.util.Optional;

@Data
public class BoardResponse  {
    private Long id;
    private String type;
    private String title;
    private String description;
    private Long hit;
    private String reportStatus;

    public BoardResponse(Board board, String status) {
        this.id = board.getId();
        this.type = board.getType();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.hit = board.getHit();
        this.reportStatus = status;
    }

}
