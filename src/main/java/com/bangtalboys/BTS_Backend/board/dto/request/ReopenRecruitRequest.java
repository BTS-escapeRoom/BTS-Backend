package com.bangtalboys.BTS_Backend.board.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReopenRecruitRequest {
    private Date recruitDeadline;
}
