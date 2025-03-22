package com.bangtalboys.BTS_Backend.review.dto.response;

import com.bangtalboys.BTS_Backend.review.domain.Review;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private Long id;
    private String content;
    private Integer people;
    private Integer time;
    private Float difficulty;
    private Integer scareScore;
    private Integer activityScore;
    private LocalDateTime visitDate;

    private Integer hints;
    private Boolean isSuccess;
    private LocalDateTime createdAt;

    private Boolean isMyReview;


    public ReviewResponse(Review review, Boolean isMine) {
        id = review.getId();
        content = review.getContent();
        people = review.getPeople();
        time = review.getTime();
        scareScore = review.getScareScore();
        activityScore = review.getActivityScore();
        difficulty = review.getDifficulty();
        visitDate = review.getVisitDate();
        isSuccess = review.getIsSuccess();
        createdAt = review.getCreatedAt();
        isMyReview = isMine;
    }
}
