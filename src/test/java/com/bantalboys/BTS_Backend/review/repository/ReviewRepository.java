package com.bantalboys.BTS_Backend.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<com.bantalboys.BTS_Backend.review.domain.Review, Long> {
    @Query("SELECT r FROM Review r WHERE (:themeId IS NULL OR r.theme.id = :themeId)")
    List<com.bantalboys.BTS_Backend.review.domain.Review> findAllByThemeIdOrAllOrderByCreatedAtDesc(Long themeId);
    List<com.bantalboys.BTS_Backend.review.domain.Review> findAllByMemberIdOrderByCreatedAtDesc(Long memberId);
    List<com.bantalboys.BTS_Backend.review.domain.Review> findAllByMemberIdAndIsDisplayTrueOrderByCreatedAtDesc(Long memberId);
    @Query("SELECT COUNT(r) > 0 FROM Review r " +
            "WHERE r.theme.id = :themeId " +
            "AND r.member.id = :memberId " +
            "AND r.updatedAt >= :oneHourAgo")
    boolean existsRecentReviews(
            @Param("themeId") Long themeId,
            @Param("memberId") Long memberId,
            @Param("oneHourAgo") LocalDateTime oneHourAgo);

    List<com.bantalboys.BTS_Backend.review.domain.Review> findAllByMemberIdAndIdIn(Long memberId, List<Long> reviewIds);

}
