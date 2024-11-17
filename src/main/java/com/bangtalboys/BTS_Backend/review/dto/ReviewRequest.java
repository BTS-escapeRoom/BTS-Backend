package com.bangtalboys.BTS_Backend.review.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewRequest {
    private String content;
    private Integer people;
    private Integer time;
    private Integer scareScore;
    private Integer activityScore;
    private Integer hardScore;
    private LocalDateTime visitDate;
    private boolean isSuccess;
    private Long themeId;
}
