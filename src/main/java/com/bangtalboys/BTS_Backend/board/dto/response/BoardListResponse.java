package com.bangtalboys.BTS_Backend.board.dto.response;

import com.bangtalboys.BTS_Backend.board.calculator.PopularityCalculator;
import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.utils.enums.BoardType;
import com.bangtalboys.BTS_Backend.utils.enums.ContactMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Date escapeDate;
    private Long recruitPeople;
    private Date recruitDeadline;
    private String contactUrl;
    private ContactMethod contactMethod;
    private int likeCount;
    private int commentCount;
    private Date createdAt;
    private Date updatedAt;
    @JsonProperty("isPopular")
    private boolean isPopular;

    public BoardListResponse(Board board) {
        this.id = board.getId();
        this.type = board.getType();
        this.title = board.getTitle();
        this.hit = board.getHit();
        this.memberName = board.getMember().getNickname();
        this.themeName = board.getTheme() != null ? board.getTheme().getTitle() : null;
        this.storeName = board.getTheme() != null ?  board.getTheme().getStore().getName() : null;
        this.escapeDate = board.getEscape_date() != null ? board.getEscape_date() : null;
        this.recruitPeople = board.getRecruit_people();
        this.recruitDeadline = board.getRecruit_deadline() != null ? board.getRecruit_deadline() : null;
        this.contactUrl = board.getContact_url();
        this.contactMethod = board.getContact_method();
        this.likeCount =  board.getLikes().size();
        this.commentCount =  board.getComments().size();
        this.createdAt = board.getCreated_at();
        this.updatedAt = board.getUpdated_at();
        this.isPopular = PopularityCalculator.isPopular(board);
    }
}
