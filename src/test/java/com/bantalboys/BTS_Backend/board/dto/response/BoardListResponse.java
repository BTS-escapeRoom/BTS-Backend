package com.bantalboys.BTS_Backend.board.dto.response;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.utils.enums.BoardType;
import lombok.Data;

@Data
public class BoardListResponse {
    private Long id;
    private BoardType type;
    private String title;
    private Long hit;
    private String memberName;
    private String themeName;

    public BoardListResponse(Board board) {
        this.id = board.getId();
        this.type = board.getType();
        this.title = board.getTitle();
        this.hit = board.getHit();
        this.memberName = board.getMember().getNickname();
        this.themeName = board.getTheme().getTitle();
    }
}
