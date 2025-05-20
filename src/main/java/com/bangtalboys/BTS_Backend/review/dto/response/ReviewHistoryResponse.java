package com.bangtalboys.BTS_Backend.review.dto.response;

import com.bangtalboys.BTS_Backend.review.domain.Review;
import com.bangtalboys.BTS_Backend.store.domain.Store;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
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


