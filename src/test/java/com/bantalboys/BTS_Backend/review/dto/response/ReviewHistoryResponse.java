package com.bantalboys.BTS_Backend.review.dto.response;

import com.bantalboys.BTS_Backend.review.domain.Review;
import lombok.Data;

@Data
public class ReviewHistoryResponse {
    private Long reviewId;
    private String storeName;
    private String themeTitle;
    private Integer time;

    public ReviewHistoryResponse(Review review) {
        this.reviewId = review.getId();
        this.storeName = review.getTheme().getStore().getName();
        this.themeTitle = review.getTheme().getTitle();
        this.time = review.getTime();


    }
}


