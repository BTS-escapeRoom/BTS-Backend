package com.bangtalboys.BTS_Backend.review.service;

import com.bangtalboys.BTS_Backend.config.error.exception.ForbiddenException;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.review.domain.Review;
import com.bangtalboys.BTS_Backend.review.dto.ReviewListResponse;
import com.bangtalboys.BTS_Backend.review.dto.ReviewRequest;
import com.bangtalboys.BTS_Backend.review.dto.ReviewResponse;
import com.bangtalboys.BTS_Backend.review.repository.ReviewRepository;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.theme.repository.ThemeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReviewResponse getOneReview(Long reviewId) {

        Optional<Review> review = reviewRepository.findById(reviewId);
        return review.map(ReviewResponse::new).orElseThrow(NotFoundException::new);
    }

    public List<ReviewListResponse> getAllReview(Long themeId) {
        List<Review> reviewList = reviewRepository.findAllByThemeIdOrderByCreatedAtDesc(themeId);
        return reviewList.stream().map(ReviewListResponse::new).collect(Collectors.toList());
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
                .hardScore(reviewRequest.getHardScore())
                .visitDate(reviewRequest.getVisitDate())
                .isSuccess(reviewRequest.isSuccess())
                .theme(theme)
                .member(member)
                .build();

        reviewRepository.save(review);
        return new ReviewResponse(review);
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
        review.setActivityScore(review.getActivityScore());
        review.setHardScore(review.getHardScore());
        review.setVisitDate(review.getVisitDate());

        reviewRepository.save(review);
        return new ReviewResponse(review);
    }

    public String deleteReview(Long reviewId, Long memberId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(NotFoundException::new);

        if (!review.getMember().getId().equals(memberId)) {
            throw new ForbiddenException();
        }

        reviewRepository.delete(review);
        return "Review deleted";
    }
}
