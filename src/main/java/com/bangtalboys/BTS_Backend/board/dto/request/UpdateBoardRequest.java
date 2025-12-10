package com.bangtalboys.BTS_Backend.board.dto.request;

import com.bangtalboys.BTS_Backend.utils.enums.ContactMethod;
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
    private ContactMethod contact_method;

    public UpdateBoardRequest(String title, String description, Date recruitDeadline, Date escapeDate,Long recruit_people, String contactUrl, ContactMethod contactMethod) {
        this.title = title;
        this.description = description;
        this.recruit_deadline = recruitDeadline;
        this.escape_date = escapeDate;
        this.recruit_people = recruit_people;
        this.contact_url = contactUrl;
        this.contact_method = contactMethod;

    }
}
