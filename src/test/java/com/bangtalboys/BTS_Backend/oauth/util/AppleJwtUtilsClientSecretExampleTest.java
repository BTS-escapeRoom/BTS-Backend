package com.bangtalboys.BTS_Backend.oauth.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Base64;

/**
 * Apple Client Secret 생성 예시 테스트
 * 
 * createClientSecret() 메서드가 생성하는 JWT 토큰의 구조를 확인합니다.
 */
@SpringBootTest
@ActiveProfiles("local")
class AppleJwtUtilsClientSecretExampleTest {

    @Autowired
    private AppleJwtUtils appleJwtUtils;

    @Test
    void printClientSecretExample() {
        // 실제 clientSecret 생성
        String clientSecret = appleJwtUtils.createClientSecret();
        
        System.out.println("=".repeat(80));
        System.out.println("생성된 Apple Client Secret (JWT):");
        System.out.println("=".repeat(80));
        System.out.println(clientSecret);
        System.out.println();
        
        // JWT 구조 분석 (디코딩)
        String[] parts = clientSecret.split("\\.");
        if (parts.length == 3) {
            System.out.println("=".repeat(80));
            System.out.println("JWT 구조 분석:");
            System.out.println("=".repeat(80));
            
            // Header 디코딩
            String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
            System.out.println("1. Header (Base64URL 디코딩):");
            System.out.println(headerJson);
            System.out.println();
            
            // Payload 디코딩
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            System.out.println("2. Payload (Base64URL 디코딩):");
            System.out.println(payloadJson);
            System.out.println();
            
            // Signature는 검증용이므로 표시하지 않음
            System.out.println("3. Signature (ES256 서명):");
            System.out.println(parts[2].substring(0, Math.min(50, parts[2].length())) + "...");
            System.out.println();
            
            // Claims 파싱하여 상세 정보 출력
            try {
                // 서명 검증 없이 디코딩만 수행 (예시용)
                String payload = parts[1];
                byte[] payloadBytes = Base64.getUrlDecoder().decode(payload);
                String payloadString = new String(payloadBytes);
                
                System.out.println("=".repeat(80));
                System.out.println("Payload 상세 정보:");
                System.out.println("=".repeat(80));
                System.out.println("  - iss (발급자): " + extractJsonValue(payloadString, "iss"));
                System.out.println("  - sub (주제/Client ID): " + extractJsonValue(payloadString, "sub"));
                System.out.println("  - aud (수신자): " + extractJsonValue(payloadString, "aud"));
                System.out.println("  - iat (발급 시간): " + extractJsonValue(payloadString, "iat"));
                System.out.println("  - exp (만료 시간): " + extractJsonValue(payloadString, "exp"));
                System.out.println();
                
            } catch (Exception e) {
                System.out.println("Payload 파싱 중 오류: " + e.getMessage());
            }
        }
        
        System.out.println("=".repeat(80));
        System.out.println("사용 예시:");
        System.out.println("=".repeat(80));
        System.out.println("POST https://appleid.apple.com/auth/token");
        System.out.println("Content-Type: application/x-www-form-urlencoded");
        System.out.println();
        System.out.println("client_id=bangtal-boys");
        System.out.println("client_secret=" + clientSecret);
        System.out.println("grant_type=authorization_code");
        System.out.println("code=cdf9f6508762a4705b08d33c78e9c1ade.0.sruwy.llCE57tAPCc53_VFkxbbdA");
        System.out.println("redirect_uri=http://localhost:8080/login/oauth2/code/apple");
        System.out.println("=".repeat(80));
    }
    
    private String extractJsonValue(String json, String key) {
        try {
            int keyIndex = json.indexOf("\"" + key + "\"");
            if (keyIndex == -1) return "N/A";
            
            int colonIndex = json.indexOf(":", keyIndex);
            int valueStart = colonIndex + 1;
            
            // 값 추출
            char firstChar = json.charAt(valueStart);
            while (firstChar == ' ' || firstChar == '\t') {
                valueStart++;
                firstChar = json.charAt(valueStart);
            }
            
            if (firstChar == '"') {
                // 문자열 값
                int valueEnd = json.indexOf("\"", valueStart + 1);
                return json.substring(valueStart + 1, valueEnd);
            } else {
                // 숫자 값
                int valueEnd = valueStart + 1;
                while (valueEnd < json.length() && 
                       (Character.isDigit(json.charAt(valueEnd)) || json.charAt(valueEnd) == '.')) {
                    valueEnd++;
                }
                return json.substring(valueStart, valueEnd);
            }
        } catch (Exception e) {
            return "N/A";
        }
    }
}

