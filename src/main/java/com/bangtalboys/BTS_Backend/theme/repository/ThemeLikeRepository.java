package com.bangtalboys.BTS_Backend.theme.repository;

import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.theme.domain.ThemeLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThemeLikeRepository extends JpaRepository<ThemeLike, Long> {
    Optional<ThemeLike> findByMemberAndTheme(Member member, Theme theme);
}
