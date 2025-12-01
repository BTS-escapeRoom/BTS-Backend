package com.bangtalboys.BTS_Backend.review.dto.response;

import com.bangtalboys.BTS_Backend.review.domain.Review;
import lombok.Data;
import com.bangtalboys.BTS_Backend.utils.enums.TimeType;

import java.time.LocalDateTime;

@Data
public class ReviewListResponse {
    private Long id;
    private String content;
    private Integer people;
    private Integer elapsedTime;
    private Integer remainingTime;
    private TimeType timeType;
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
        this.scareScore = review.getScareScore();
        this.activityScore = review.getActivityScore();
        this.difficulty = review.getDifficulty();
        this.visitDate = review.getVisitDate();
        this.hints = review.getHints();
        this.isSuccess = review.getIsSuccess();
        this.createdAt = review.getCreatedAt();
        this.isMyReview = isMine;
        this.nickname = review.getMember().getNickname();
        this.timeType = review.getTimeType();

        int themeTime = review.getTheme().getTime() * 60;
        int time = review.getTime() != null ? review.getTime() : 0;

        if (review.getTimeType() == TimeType.ELAPSED) {
            // 기록된 값이 '걸린 시간'인 경우
            elapsedTime = time;
            remainingTime = Math.max(0, themeTime - time);

        } else if (review.getTimeType() == TimeType.RAMAINING) {
            // 기록된 값이 '남은 시간'인 경우
            remainingTime = time;
            elapsedTime = Math.max(0, themeTime - time);

        } else {
            // NONE 이거나 기타 경우
            elapsedTime = 0;
            remainingTime = 0;
        }
    }
}
