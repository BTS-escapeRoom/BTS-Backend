package com.bangtalboys.BTS_Backend.review.repository;

import com.bangtalboys.BTS_Backend.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    public interface ReviewWithReportedProjection {
        Review getReview();
        Boolean getIsReported();
    }

    @Query("SELECT r as review, CASE WHEN rr.id IS NOT NULL THEN true ELSE false END as isReported " +
            "FROM Review r " +
            "LEFT JOIN ReviewReport rr ON rr.review.id = r.id AND rr.member.id = :memberId " +
            "WHERE (:themeId IS NULL OR r.theme.id = :themeId) " +
            "ORDER BY r.createdAt DESC")
    List<ReviewWithReportedProjection> findAllWithReportByThemeIdOrAllOrderByCreatedAtDesc(
            @Param("themeId") Long themeId,
            @Param("memberId") Long memberId
    );
    List<Review> findAllByMemberIdOrderByCreatedAtDesc(Long memberId);
    List<Review> findAllByMemberIdAndIsDisplayTrueOrderByCreatedAtDesc(Long memberId);
    @Query("SELECT COUNT(r) > 0 FROM Review r " +
            "WHERE r.theme.id = :themeId " +
            "AND r.member.id = :memberId " +
            "AND r.updatedAt >= :oneHourAgo")
    boolean existsRecentReviews(
            @Param("themeId") Long themeId,
            @Param("memberId") Long memberId,
            @Param("oneHourAgo") LocalDateTime oneHourAgo);

    List<Review> findAllByMemberIdAndIdIn(Long memberId, List<Long> reviewIds);

}
