package com.bangtalboys.BTS_Backend.review.dto.response;

import com.bangtalboys.BTS_Backend.review.domain.Review;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewListResponse {
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
    private String nickname;


    public ReviewListResponse(Review review, Boolean isMine) {
        this.id = review.getId();
        this.content = review.getContent();
        this.people = review.getPeople();
        this.time = review.getTime();
        this.scareScore = review.getScareScore();
        this.activityScore = review.getActivityScore();
        this.difficulty = review.getDifficulty();
        this.visitDate = review.getVisitDate();
        this.hints = review.getHints();
        this.isSuccess = review.getIsSuccess();
        this.createdAt = review.getCreatedAt();
        this.isMyReview = isMine;
        this.nickname = review.getMember().getNickname();
    }
}
