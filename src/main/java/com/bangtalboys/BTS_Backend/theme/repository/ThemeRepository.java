package com.bangtalboys.BTS_Backend.theme.repository;

import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    @Query("SELECT t FROM Theme t " +
            "WHERE (:title IS NULL OR t.title LIKE %:title%) " +
            "AND (:peoples IS NULL OR (t.minimumPeople <= :peoples AND :peoples <= t.maximumPeople)) " +
            "AND (:genre IS NULL OR t.genre = :genre) " +
            "AND (:difficulty IS NULL OR t.difficulty >= :difficulty) " +
            "AND (:districtId IS NULL OR t.store.district.id = :districtId) " +
            "AND (:cityId IS NULL OR t.store.district.city.id = :cityId)")
    List<Theme> findByTitleAndPeoplesAndGenreAndDifficultyAndDistrictOrCity(
            @Param("title") String title,
            @Param("peoples") Integer peoples,
            @Param("genre") String genre,
            @Param("difficulty") Integer difficulty,
            @Param("districtId") Long districtId,
            @Param("cityId") Long cityId);
}
