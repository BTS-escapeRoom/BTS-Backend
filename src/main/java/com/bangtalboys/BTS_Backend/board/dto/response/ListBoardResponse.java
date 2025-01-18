package com.bangtalboys.BTS_Backend.board.dto.response;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import lombok.Data;

@Data
public class ListBoardResponse {
    private Long id;
    private String type;
    private String title;
    private Long hit;
    private String memberName;
    private String themeName;

    public ListBoardResponse(Board board) {
        this.id = board.getId();
        this.type = board.getType();
        this.title = board.getTitle();
        this.hit = board.getHit();
        this.memberName = board.getMember().getNickname();
        this.themeName = board.getTheme().getTitle();
    }
}
