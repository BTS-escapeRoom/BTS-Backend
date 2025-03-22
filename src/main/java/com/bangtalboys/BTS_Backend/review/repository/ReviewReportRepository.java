package com.bangtalboys.BTS_Backend.review.repository;

import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.review.domain.Review;
import com.bangtalboys.BTS_Backend.review.domain.ReviewReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, Long> {

    Optional<ReviewReport> findByReviewAndMember(Review review, Member member);

}