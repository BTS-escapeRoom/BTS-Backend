package com.bangtalboys.BTS_Backend.review.dto.response;

import lombok.Data;

@Data
public class ReviewAvailableResponse {
    private Boolean isAvailable;

    public ReviewAvailableResponse(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
