package com.bangtalboys.BTS_Backend.review.dto.response;

import com.bangtalboys.BTS_Backend.review.domain.Review;
import lombok.Data;

@Data
public class ReviewHistoryResponse {
    private Long reviewId;
    private String storeName;
    private Long themeId;
    private String themeTitle;
    private String themeThumbnail;
    private String themeGenre;
    private String themeCity;
    private String themeDistrict;
    private Integer time;
    private Boolean isSuccess;
    private Boolean isDisplay;

    public ReviewHistoryResponse(Review review) {
        this.reviewId = review.getId();
        this.storeName = review.getTheme().getStore().getName();
        this.themeId = review.getTheme().getId();
        this.themeTitle = review.getTheme().getTitle();
        this.themeThumbnail = review.getTheme().getThumbnail();
        this.themeGenre = review.getTheme().getGenre();
        this.themeCity = review.getTheme().getStore().getDistrict().getCity().getName();
        this.themeDistrict = review.getTheme().getStore().getDistrict().getName();
        this.themeId = review.getTheme().getId();
        this.time = review.getTime();
        this.isSuccess = review.getIsSuccess();
        this.isDisplay = review.getIsDisplay();



    }
}


