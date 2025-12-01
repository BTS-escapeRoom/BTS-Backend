package com.bangtalboys.BTS_Backend.review.dto.request;

import lombok.Data;

import com.bangtalboys.BTS_Backend.utils.enums.TimeType;

import java.time.LocalDateTime;

@Data
public class ReviewRequest {
    private String content;
    private Integer people;
    private Integer time;
    private TimeType timeType;
    private Integer scareScore;
    private Integer activityScore;
    private Float difficulty;
    private Integer hints;
    private LocalDateTime visitDate;
    private Boolean isSuccess;
    private Long themeId;
}
