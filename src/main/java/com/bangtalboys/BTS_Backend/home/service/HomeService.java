package com.bangtalboys.BTS_Backend.home.service;

import com.bangtalboys.BTS_Backend.home.dto.response.HomeResponse;
import com.bangtalboys.BTS_Backend.utils.enums.UiType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    public List<HomeResponse> getHome() {
        List<HomeResponse> homeResponseList = new ArrayList<>();
        HomeResponse Banner = new HomeResponse(UiType.Banner.name(), "빅배너", "apis.bangtal-boys.com/v1/themes/random");
        homeResponseList.add(Banner);

        HomeResponse PopularBand = new HomeResponse(UiType.General.name(), "인기 있는 테마", "apis.bangtal-boys.com/themes/popular");
        homeResponseList.add(PopularBand);

        HomeResponse Recent = new HomeResponse(UiType.General.name(), "최신 테마", "apis.bangtal-boys.com/themes/recent");
        homeResponseList.add(Recent);

        HomeResponse RealtimePopularBand = new HomeResponse(UiType.Ranking.name(), "실시간 인기 테마", "apis.bangtal-boys.com/themes/popular/realtime");
        homeResponseList.add(RealtimePopularBand);

        HomeResponse MostLiked = new HomeResponse(UiType.Ranking.name(), "관심 급상승 테마", "apis.bangtal-boys.com/themes/most-liked");
        homeResponseList.add(MostLiked);

        return homeResponseList;
    }
}
