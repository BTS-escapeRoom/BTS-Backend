package com.bangtalboys.BTS_Backend.board.dto.response;

import com.bangtalboys.BTS_Backend.board.calculator.PopularityCalculator;
import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.utils.enums.BoardType;
import com.bangtalboys.BTS_Backend.utils.enums.ContactMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BoardResponse  {
    private Long id;
    private BoardType type;
    private String title;
    private String description;
    private String memberName;
    private String themeName;
    private String storeName;
    private Date escapeDate;
    private Long recruitPeople;
    private String contactUrl;
    private ContactMethod contactMethod;
    private Date createdAt;
    private Date updatedAt;
    private Date recruitDeadline;
    private Long hit;
    private String reportStatus;
    private int likeCount;
    private int commentCount;
    @JsonProperty("isPopular")
    private boolean isPopular;


    public BoardResponse(Board board, String status) {
        this.id = board.getId();
        this.type = board.getType();
        this.title = board.getTitle();
        this.memberName = board.getMember().getNickname();
        this.themeName = board.getTheme() != null ? board.getTheme().getTitle() : null;
        this.storeName = board.getTheme() != null ?  board.getTheme().getStore().getName() : null;
        this.description = board.getDescription();
        this.recruitDeadline = board.getRecruit_deadline();
        this.escapeDate = board.getEscape_date() != null ? board.getEscape_date() : null;
        this.recruitPeople = board.getRecruit_people();
        this.contactUrl = board.getContact_url();
        this.contactMethod = board.getContact_method();
        this.hit = board.getHit();
        this.reportStatus = status;
        this.likeCount = board.getLikes().size();
        this.commentCount = board.getComments().size();
        this.isPopular = PopularityCalculator.isPopular(board);
        this.createdAt = board.getCreated_at();
        this.updatedAt = board.getUpdated_at();
    }
}
