package com.bangtalboys.BTS_Backend.board.dto.response;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.utils.enums.BoardType;
import lombok.Data;

import java.util.Date;

@Data
public class BoardListResponse {
    private Long id;
    private BoardType type;
    private String title;
    private Long hit;
    private String memberName;
    private String themeName;
    private String storeName;
    private String escapeDate;
    private Long recruitPeople;
    private String contactUrl;
    private String contactMethod;
    private int likeCount;
    private int commentCount;
    private String createdAt;
    private String updatedAt;

    public BoardListResponse(Board board) {
        this.id = board.getId();
        this.type = board.getType();
        this.title = board.getTitle();
        this.hit = board.getHit();
        this.memberName = board.getMember().getNickname();
        this.themeName = board.getTheme().getTitle();
        this.storeName = board.getTheme().getStore().getName();
        this.escapeDate = board.getEscape_date().toString();
        this.recruitPeople = board.getRecruit_people();
        this.contactUrl = board.getContact_url();
        this.contactMethod = board.getContact_method();
        this.likeCount =  board.getLikes().size();
        this.commentCount =  board.getComments().size();
        this.createdAt = board.getCreated_at();
        this.updatedAt = board.getUpdated_at();
    }
}
