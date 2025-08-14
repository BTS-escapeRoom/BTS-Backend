package com.bantalboys.BTS_Backend.review.dto.request;

import lombok.Data;

@Data
public class ReviewReportRequest {
    private Long reviewId;
    private String description;
}