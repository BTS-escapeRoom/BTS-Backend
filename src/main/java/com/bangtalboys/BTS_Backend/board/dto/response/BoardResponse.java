package com.bangtalboys.BTS_Backend.board.dto.response;

import com.bangtalboys.BTS_Backend.board.calculator.PopularityCalculator;
import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeResponse;
import com.bangtalboys.BTS_Backend.utils.enums.BoardType;
import com.bangtalboys.BTS_Backend.utils.enums.ContactMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BoardResponse {
    private Long id;
    private BoardType type;
    private String title;
    private String description;

    private Long memberId;
    private String memberName;
    private String profileImg;

    private ThemeResponse theme;  // <-- 엔티티 대신 DTO!

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

    @JsonProperty("isLike")
    private boolean isLike;

    public BoardResponse(Board board, String status, boolean isLike) {
        this.id = board.getId();
        this.type = board.getType();
        this.title = board.getTitle();
        this.description = board.getDescription();

        this.memberId = board.getMember().getId();
        this.memberName = board.getMember().getNickname();
        this.profileImg = board.getMember().getProfileImg();

        this.theme = board.getTheme() != null ? new ThemeResponse(board.getTheme()) : null;

        this.escapeDate = board.getEscape_date();
        this.recruitPeople = board.getRecruit_people();
        this.contactUrl = board.getContact_url();
        this.contactMethod = board.getContact_method();
        this.hit = board.getHit();

        this.reportStatus = status;
        this.likeCount = board.getLikes().size();
        this.commentCount = board.getComments().size();
        this.isPopular = PopularityCalculator.isPopular(board);
        this.isLike = isLike;
        this.recruitDeadline = board.getRecruit_deadline() != null ? board.getRecruit_deadline() : null;
        this.createdAt = board.getCreated_at();
        this.updatedAt = board.getUpdated_at();
    }
}


