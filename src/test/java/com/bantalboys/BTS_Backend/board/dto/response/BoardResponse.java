package com.bantalboys.BTS_Backend.board.dto.response;

import com.bangtalboys.BTS_Backend.board.domain.Board;
import com.bangtalboys.BTS_Backend.board.domain.BoardReport;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeListResponse;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeResponse;
import lombok.Data;

import java.util.Date;
import java.util.Optional;

@Data
public class BoardResponse  {
    private Long id;
    private String type;
    private String title;
    private String description;
    private Date recruit_deadline;
    private Date escape_date;
    private Long recruit_people;
    private String contact_url;
    private String contact_method;
    private Long hit;
    private String reportStatus;

    public BoardResponse(Board board, String status) {
        this.id = board.getId();
        this.type = board.getType();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.recruit_deadline = board.getRecruit_deadline();
        this.escape_date = board.getEscape_date();
        this.recruit_people = board.getRecruit_people();
        this.contact_url = board.getContact_url();
        this.contact_method = board.getContact_method();
        this.hit = board.getHit();
        this.reportStatus = status;
    }

}
