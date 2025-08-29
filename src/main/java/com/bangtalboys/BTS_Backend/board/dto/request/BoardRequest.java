package com.bangtalboys.BTS_Backend.board.dto.request;

import com.bangtalboys.BTS_Backend.utils.enums.BoardType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class BoardRequest {
    private Long themeId;
    private BoardType type;
    private String title;
    private String description;
    private Date recruit_deadline;
    private Date escape_date;
    private Long recruit_people;
    private String contact_url;
    private String contact_method;

    public BoardRequest(Long themeId,BoardType type, String title, String description, Date recruit_deadline, Date escape_date, Long recruit_people, String contact_url, String contact_method) {
        this.themeId = themeId;
        this.type = type;
        this.title = title;
        this.description = description;
        this.recruit_deadline = recruit_deadline;
        this.escape_date = escape_date;
        this.recruit_people = recruit_people;
        this.contact_url = contact_url;
        this.contact_method = contact_method;
    }
}