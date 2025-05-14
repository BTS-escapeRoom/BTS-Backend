package com.bangtalboys.BTS_Backend.theme.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.bangtalboys.BTS_Backend.genre.domain.Genre;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.store.domain.Store;
import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeResponse;
import com.bangtalboys.BTS_Backend.theme.repository.ThemeLikeRepository;
import com.bangtalboys.BTS_Backend.theme.repository.ThemeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.management.GarbageCollectorMXBean;
import java.util.Optional;

@DisplayName("ThemeService 단위 테스트")
@SpringBootTest
class ThemeServiceTest {

    @Autowired
    ThemeService themeService;

    @MockBean
    ThemeRepository themeRepository;

    @MockBean
    ThemeLikeRepository themeLikeRepository;

    @MockBean
    MemberRepository memberRepository;

    @BeforeEach
    void setUp(){
        themeService = new ThemeService(themeRepository, themeLikeRepository, memberRepository);
    }

    @Test
    @DisplayName("theme 조회 테스트")
    void testGetOneTheme() {
        // given
        Theme theme = createSampleTheme();
        when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));

        // when
        ThemeResponse result = themeService.getOneTheme(1L);

        // then
        assertNotNull(result);
        assertEquals("test", result.getTitle());
    }


    private Theme createSampleTheme() {
        Theme theme = new Theme();
        theme.setId(1L);
        theme.setTitle("test");
        theme.setGenreType(createSampleGenre());
        theme.setStore(createSampleStore());
        return theme;
    }

    private Genre createSampleGenre() {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("genre");
        return genre;
    }

    private Store createSampleStore() {
        Store store = new Store();
        store.setId(1L);
        store.setName("store");
        return store;
    }
}

