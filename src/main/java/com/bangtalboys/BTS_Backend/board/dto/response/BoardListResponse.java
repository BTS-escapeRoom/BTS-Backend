package com.bangtalboys.BTS_Backend.board.dto.response;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import lombok.Data;

@Data
public class BoardListResponse {
    private Long id;
    private String type;
    private String title;
    private Long hit;
    private String memberName;
    private String themeName;
    private String escapeDate;
    private Long recruitPeople;
    private String contactUrl;
    private String contactMethod;
    private int likeCount;

    public BoardListResponse(Board board) {
        this.id = board.getId();
        this.type = board.getType();
        this.title = board.getTitle();
        this.hit = board.getHit();
        this.memberName = board.getMember().getNickname();
        this.themeName = board.getTheme().getTitle();
        this.escapeDate = board.getEscape_date().toString();
        this.recruitPeople = board.getRecruit_people();
        this.contactUrl = board.getContact_url();
        this.contactMethod = board.getContact_method();
        this.likeCount =  board.getLikes().size();
    }
}
