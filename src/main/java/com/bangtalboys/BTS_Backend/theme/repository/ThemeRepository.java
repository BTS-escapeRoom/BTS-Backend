package com.bangtalboys.BTS_Backend.theme.repository;

import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    @Query("SELECT t FROM Theme t " +
            "WHERE (:keyword IS NULL OR t.title LIKE %:keyword% OR t.store.name LIKE %:keyword%) " +
            "AND (:peoples IS NULL OR (t.minimumPeople <= :peoples AND :peoples <= t.maximumPeople)) " +
            "AND (:minDiff IS NULL OR t.difficulty >= :minDiff) " +
            "AND (:maxDiff IS NULL OR t.difficulty <= :maxDiff) " +
            "AND (:genreIdList IS NULL OR t.genreType.id IN :genreIdList) " +
            "AND (:districtIdList IS NULL OR t.store.district.id IN :districtIdList) " +
            "AND (:cityIdList IS NULL OR t.store.district.city.id IN :cityIdList)")
    List<Theme> findByKeywordAndPeoplesAndGenreAndDifficultyAndDistrictOrCity(
            @Param("keyword") String keyword,
            @Param("peoples") Integer peoples,
            @Param("minDiff") Integer minDiff,
            @Param("maxDiff") Integer maxDiff,
            @Param("genreIdList") List<Long> genreIdList,
            @Param("districtIdList") List<Long> districtIdList,
            @Param("cityIdList") List<Long> cityIdList);

    @Query("SELECT t FROM Theme t " +
            "JOIN ThemeLike tl ON t.id = tl.theme.id " +
            "WHERE tl.member.id = :memberId")
    List<Theme> findThemeByMemberId(
            @Param("memberId") Long memberId);

    @Query("SELECT t FROM Theme t WHERE t.id IN (:ids)")
    List<Theme> findAllByIds(@Param("ids") List<Long> ids);

    @Query("SELECT t, MAX(b.hit) AS max_hit FROM Theme t INNER JOIN Board b ON b.theme.id = t.id GROUP BY b.theme.id ORDER BY max_hit DESC Limit 20")
    List<Theme> findTop20RealtimePopularThemes();


    @Query("SELECT t, Count(tl.id) AS tl_count FROM Theme t INNER JOIN ThemeLike tl ON t.id = tl.theme.id GROUP BY t.id ORDER BY tl_count DESC LIMIT 20")
    List<Theme> findTop20LikedThemes();

    List<Theme> findTop20ByOrderByRegistrationDateDesc();
}
