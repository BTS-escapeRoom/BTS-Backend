package com.bangtalboys.BTS_Backend.genre.repository;

import com.bangtalboys.BTS_Backend.genre.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
