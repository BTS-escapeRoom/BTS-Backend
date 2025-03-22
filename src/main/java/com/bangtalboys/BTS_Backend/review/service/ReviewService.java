package com.bangtalboys.BTS_Backend.review.service;

import com.bangtalboys.BTS_Backend.config.error.exception.ForbiddenException;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.review.domain.Review;
import com.bangtalboys.BTS_Backend.review.domain.ReviewReport;
import com.bangtalboys.BTS_Backend.review.dto.request.ReviewReportRequest;
import com.bangtalboys.BTS_Backend.review.dto.request.ReviewRequest;
import com.bangtalboys.BTS_Backend.review.dto.response.ReviewAvailableResponse;
import com.bangtalboys.BTS_Backend.review.dto.response.ReviewListResponse;
import com.bangtalboys.BTS_Backend.review.dto.response.ReviewResponse;
import com.bangtalboys.BTS_Backend.review.repository.ReviewReportRepository;
import com.bangtalboys.BTS_Backend.review.repository.ReviewRepository;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.theme.repository.ThemeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewReportRepository reviewReportRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReviewResponse getOneReview(Long reviewId, Long memberId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        return review
                .map(rv -> new ReviewResponse(rv, rv.getMember().getId().equals(memberId)))
                .orElseThrow(NotFoundException::new);
    }

    public List<ReviewListResponse> getAllReviews(Long themeId, Long memberId) {
        List<Review> reviewList = reviewRepository.findAllByThemeIdOrAllOrderByCreatedAtDesc(themeId);

        return reviewList.stream()
                .map(review -> new ReviewListResponse(review, review.getMember().getId().equals(memberId)))
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewResponse createReview(ReviewRequest reviewRequest, Long memberId) {
        Theme theme = themeRepository.findById(reviewRequest.getThemeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid theme ID: " + reviewRequest.getThemeId()));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));

        Review review = Review.builder()
                .content(reviewRequest.getContent())
                .people(reviewRequest.getPeople())
                .time(reviewRequest.getTime())
                .scareScore(reviewRequest.getScareScore())
                .activityScore(reviewRequest.getActivityScore())
                .difficulty(reviewRequest.getDifficulty())
                .visitDate(reviewRequest.getVisitDate())
                .hints(reviewRequest.getHints())
                .isSuccess(reviewRequest.getIsSuccess())
                .theme(theme)
                .member(member)
                .build();

        reviewRepository.save(review);
        return new ReviewResponse(review, true);
    }

    @Transactional
    public ReviewResponse updateReview(Long reviewId, ReviewRequest reviewRequest, Long memberId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new);

        if (!review.getMember().getId().equals(memberId)) {
            throw new ForbiddenException();
        }

        review.setContent(reviewRequest.getContent());
        review.setPeople(reviewRequest.getPeople());
        review.setTime(reviewRequest.getTime());
        review.setScareScore(reviewRequest.getScareScore());
        review.setActivityScore(reviewRequest.getActivityScore());
        review.setDifficulty(reviewRequest.getDifficulty());
        review.setVisitDate(reviewRequest.getVisitDate());
        review.setHints(reviewRequest.getHints());
        review.setIsSuccess(reviewRequest.getIsSuccess());

        reviewRepository.save(review);
        return new ReviewResponse(review, true);
    }

    public String deleteReview(Long reviewId, Long memberId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new);

        if (!review.getMember().getId().equals(memberId)) {
            throw new ForbiddenException();
        }

        reviewRepository.delete(review);
        return "Review deleted";
    }

    public List<ReviewListResponse> getMyReview(Long memberId) {
        List<Review> reviewList = reviewRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId);
        return reviewList.stream()
                .map(review -> new ReviewListResponse(review, true))
                .collect(Collectors.toList());
    }

    public ReviewAvailableResponse getReviewAvailable(Long themeId, Long memberId) {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        Boolean isAvailable = reviewRepository.existsRecentReviews(themeId, memberId, oneHourAgo);
        return new ReviewAvailableResponse(!isAvailable);
    }

    public String createReviewReport(Long memberId, ReviewReportRequest reviewReportRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException::new);
        Review review = reviewRepository.findById(reviewReportRequest.getReviewId()).orElseThrow(NotFoundException::new);

        Optional<ReviewReport> existReviewReport = reviewReportRepository.findByReviewAndMember(review, member);

        if (existReviewReport.isPresent()) {
            reviewReportRepository.delete(existReviewReport.get());
            return "리뷰 신고 취소 완료";
        }

        ReviewReport reviewReport = ReviewReport.builder()
                .review(review)
                .member(member)
                .description(reviewReportRequest.getDescription())
                .build();
        reviewReportRepository.save(reviewReport);
        return "리뷰 신고 완료";
    }
}
