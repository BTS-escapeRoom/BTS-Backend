package com.bangtalboys.BTS_Backend.oauth.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.stereotype.Component;

@Component
public class UrlUtils {
    public boolean isSafeReturnUrl(String url) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();

            // 로컬 개발 환경 허용 (선택)
            if (host == null) return false;

            return host.endsWith("bangtal-boys.com") || host.equals("localhost");
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
