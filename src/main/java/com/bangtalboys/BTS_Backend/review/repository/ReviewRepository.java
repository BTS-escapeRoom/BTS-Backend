package com.bangtalboys.BTS_Backend.review.repository;

import com.bangtalboys.BTS_Backend.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE (:themeId IS NULL OR r.theme.id = :themeId)")
    List<Review> findAllByThemeIdOrAllOrderByCreatedAtDesc(Long themeId);
    List<Review> findAllByMemberIdOrderByCreatedAtDesc(Long memberId);
    @Query("SELECT COUNT(r) > 0 FROM Review r " +
            "WHERE r.theme.id = :themeId " +
            "AND r.member.id = :memberId " +
            "AND r.updatedAt >= :oneHourAgo")
    boolean existsRecentReviews(
            @Param("themeId") Long themeId,
            @Param("memberId") Long memberId,
            @Param("oneHourAgo") LocalDateTime oneHourAgo);

}
