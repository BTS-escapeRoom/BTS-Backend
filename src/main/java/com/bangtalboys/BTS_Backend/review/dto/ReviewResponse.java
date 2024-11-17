package com.bangtalboys.BTS_Backend.review.dto;

import com.bangtalboys.BTS_Backend.review.domain.Review;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private Long id;
    private String content;
    private Integer people;
    private Integer time;
    private Integer scareScore;
    private Integer activityScore;
    private Integer hardScore;
    private LocalDateTime visitDate;
    private Boolean isSuccess;
    private LocalDateTime createdAt;


    public ReviewResponse(Review review) {
        id = review.getId();
        content = review.getContent();
        people = review.getPeople();
        time = review.getTime();
        scareScore = review.getScareScore();
        activityScore = review.getActivityScore();
        hardScore = review.getHardScore();
        visitDate = review.getVisitDate();
        isSuccess = review.getIsSuccess();
        createdAt = review.getCreatedAt();
    }
}
