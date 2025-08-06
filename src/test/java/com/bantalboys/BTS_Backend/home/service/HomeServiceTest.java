package com.bantalboys.BTS_Backend.home.service;

import com.bangtalboys.BTS_Backend.home.dto.response.HomeResponse;
import com.bangtalboys.BTS_Backend.home.service.HomeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HomeServiceTest {

    @InjectMocks
    private HomeService homeService;

    @Test()
    void getHome_success() {
        List<HomeResponse> result = homeService.getHome();

        assertEquals(5, result.size());
        assertEquals("빅배너", result.get(0).getTitle());
        assertEquals("인기 있는 테마", result.get(1).getTitle());
        assertEquals("최신 테마", result.get(2).getTitle());
        assertEquals("실시간 인기 테마", result.get(3).getTitle());
        assertEquals("관심 급상승 테마", result.get(4).getTitle());
    }
}
