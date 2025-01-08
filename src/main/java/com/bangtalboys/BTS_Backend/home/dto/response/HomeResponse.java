package com.bangtalboys.BTS_Backend.home.dto.response;

import lombok.Data;

@Data
public class HomeResponse {
    private String ui_type;
    private String title;
    private String url;

    public HomeResponse(String ui_type, String title, String url) {
        this.ui_type = ui_type;
        this.title = title;
        this.url = url;
    }
}
