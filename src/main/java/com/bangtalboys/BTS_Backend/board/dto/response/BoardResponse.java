package com.bangtalboys.BTS_Backend.board.dto.response;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import lombok.Data;

@Data
public class BoardResponse  {
    private Long id;
    private String type;
    private String title;
    private String description;
    private Long hit;
    private Member member;
    private Theme theme;

    public BoardResponse(Board board, Member member, Theme theme) {
        this.id = board.getId();
        this.type = board.getType();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.hit = board.getHit();
        this.member = member;
        this.theme = theme;
    }

}
