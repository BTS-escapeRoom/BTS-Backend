package com.bangtalboys.BTS_Backend.review.dto.response;

import com.bangtalboys.BTS_Backend.review.domain.Review;
import lombok.Data;

@Data
public class ReviewHistoryResponse {
    private Long reviewId;
    private String storeName;
    private String themeTitle;
    private Integer time;
    private Boolean isSuccess;
    private Boolean isDisplay;

    public ReviewHistoryResponse(Review review) {
        this.reviewId = review.getId();
        this.storeName = review.getTheme().getStore().getName();
        this.themeTitle = review.getTheme().getTitle();
        this.time = review.getTime();
        this.isSuccess = review.getIsSuccess();
        this.isDisplay = review.getIsDisplay();



    }
}


