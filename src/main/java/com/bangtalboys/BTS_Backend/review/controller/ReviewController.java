package com.bangtalboys.BTS_Backend.review.controller;

import com.bangtalboys.BTS_Backend.oauth.dto.CustomOAuth2User;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import com.bangtalboys.BTS_Backend.review.dto.ReviewListResponse;
import com.bangtalboys.BTS_Backend.review.dto.ReviewRequest;
import com.bangtalboys.BTS_Backend.review.dto.ReviewResponse;
import com.bangtalboys.BTS_Backend.review.service.ReviewService;
import com.bangtalboys.BTS_Backend.utils.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name="테마 리뷰 API")
@RequestMapping("/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "리뷰 단건 조회")
    @GetMapping("/{reviewId}")
    public ResponseEntity<Response<ReviewResponse>> getOneReviews(
            @PathVariable Long reviewId
    ) {

        return ResponseEntity.ok(Response.ok(reviewService.getOneReview(reviewId)));
    }

    @Operation(summary = "리뷰 목록 조회")
    @GetMapping("")
    public ResponseEntity<Response<List<ReviewListResponse>>> getAllReviews(
            @RequestParam(required = true) Long themeId
    ) {

        return ResponseEntity.ok(Response.ok(reviewService.getAllReviews(themeId)));
    }

    @Operation(summary = "리뷰 등록")
    @PostMapping("")
    public ResponseEntity<Response<ReviewResponse>> createReview(
            @AuthenticationPrincipal CustomOAuth2User oauth2User,
            @RequestBody ReviewRequest reviewRequest
    ) {

        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(reviewService.createReview(reviewRequest, memberId)));
    }

    @Operation(summary = "리뷰 수정")
    @PutMapping("/{reviewId}")
    public ResponseEntity<Response<ReviewResponse>> updateReview(
            @AuthenticationPrincipal CustomOAuth2User oauth2User,
            @PathVariable Long reviewId,
            @RequestBody ReviewRequest reviewRequest
    ) {

        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(reviewService.updateReview(reviewId, reviewRequest, memberId)));
    }

    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Response<String>> deleteReview(
            @AuthenticationPrincipal CustomOAuth2User oauth2User,
            @PathVariable Long reviewId
    ) {

        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(reviewService.deleteReview(reviewId, memberId)));
    }

    @Operation(summary = "내가 쓴 리뷰 조회")
    @GetMapping("/me")
    public ResponseEntity<Response<List<ReviewListResponse>>> getMyReview(
            @AuthenticationPrincipal CustomOAuth2User oauth2User
    ) {

        Long memberId = oauth2User.getId();
        return ResponseEntity.ok(Response.ok(reviewService.getMyReview(memberId)));
    }
}
