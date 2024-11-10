package com.bangtalboys.BTS_Backend.home.service;

import com.bangtalboys.BTS_Backend.theme.domain.Theme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    public String getHome() {
        return "BangtalBoys";
    }
}
