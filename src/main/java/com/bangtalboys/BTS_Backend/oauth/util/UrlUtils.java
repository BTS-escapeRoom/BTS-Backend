package com.bangtalboys.BTS_Backend.oauth.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.stereotype.Component;

@Component
public class UrlUtils {
    public boolean isSafeReturnUrl(String url) {
        if (url == null || url.trim().isEmpty()) return false;
    
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            String host = uri.getHost();
    
            // 1. 커스텀 딥링크 스킴 허용
            if ("naverbangtalsonyeondan".equals(scheme)) {
                return true;
            }
    
            // 2. 호스트 기반 체크 (HTTP/HTTPS URL 용)
            if (host != null) {
                if (host.endsWith("bangtal-boys.com") || host.equals("localhost")) {
                    return true;
                }
            }
    
            return false;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
