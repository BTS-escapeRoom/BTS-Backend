package com.bangtalboys.BTS_Backend.oauth.util;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class AppleJwtUtil {

    @Value("${apple.key-id:}")
    private String keyId;

    @Value("${apple.team-id:}")
    private String teamId;

    @Value("${apple.client-id:}")
    private String clientId;

    /**
     * Apple OAuth2용 JWT 클라이언트 시크릿 생성
     * @param privateKey Apple에서 제공한 private key
     * @return JWT 클라이언트 시크릿
     */
    public String generateClientSecret(String privateKey) {
        // Apple 설정이 없으면 빈 문자열 반환 (기본 클라이언트 시크릿 사용)
        if (keyId == null || keyId.isEmpty() || teamId == null || teamId.isEmpty() || 
            clientId == null || clientId.isEmpty() || privateKey == null || privateKey.isEmpty()) {
            return "";
        }

        try {
            // Private key 파싱
            String cleanPrivateKey = privateKey
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(cleanPrivateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PrivateKey pk = keyFactory.generatePrivate(keySpec);

            // JWT 생성
            Date now = new Date();
            Date expiration = new Date(now.getTime() + 180 * 24 * 60 * 60 * 1000L); // 180일

            return Jwts.builder()
                    .header().add("kid", keyId).and()
                    .issuer(teamId)
                    .audience().add("https://appleid.apple.com").and()
                    .subject(clientId)
                    .issuedAt(now)
                    .expiration(expiration)
                    .signWith(pk)
                    .compact();

        } catch (Exception e) {
            throw new RuntimeException("Apple JWT Client Secret 생성 실패", e);
        }
    }
}
