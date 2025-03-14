package com.bangtalboys.BTS_Backend.review.dto;

import com.bangtalboys.BTS_Backend.review.domain.Review;
import lombok.Data;

@Data
public class ReviewAvailableResponse {
    private Boolean isAvailable;

    public ReviewAvailableResponse(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
