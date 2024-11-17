package com.bangtalboys.BTS_Backend.review.repository;

import com.bangtalboys.BTS_Backend.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByThemeIdOrderByCreatedAtDesc(Long themeId);
}
