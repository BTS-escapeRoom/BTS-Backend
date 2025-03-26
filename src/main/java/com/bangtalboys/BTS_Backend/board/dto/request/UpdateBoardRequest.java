package com.bangtalboys.BTS_Backend.board.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class UpdateBoardRequest {
    private String title;
    private String description;
    private Date recruit_deadline;
    private Date escape_date;
    private Long recruit_people;
    private String contact_url;
    private String contact_method;

    public UpdateBoardRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
