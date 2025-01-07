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
            "AND (:difficulty IS NULL OR t.difficulty >= :difficulty) " +
            "AND (:genreId IS NULL OR t.genreType.id = :genreId) " +
            "AND (:districtId IS NULL OR t.store.district.id = :districtId) " +
            "AND (:cityId IS NULL OR t.store.district.city.id = :cityId)")
    List<Theme> findByTitleAndPeoplesAndGenreAndDifficultyAndDistrictOrCity(
            @Param("title") String title,
            @Param("peoples") Integer peoples,
            @Param("difficulty") Integer difficulty,
            @Param("genreId") Long genreId,
            @Param("districtId") Long districtId,
            @Param("cityId") Long cityId);

    @Query("SELECT tl.theme FROM ThemeLike tl WHERE tl.member.id = :memberId")
    List<Theme> findLikeThemeByMemberId(@Param("memberId") Long memberId);
}
