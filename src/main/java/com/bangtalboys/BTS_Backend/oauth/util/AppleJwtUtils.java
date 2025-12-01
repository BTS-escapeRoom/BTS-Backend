package com.bangtalboys.BTS_Backend.oauth.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class AppleJwtUtils{

    // .p8 파일에서 읽어온 비공개 키
    private PrivateKey privateKey; 
    
    @Value("${spring.security.oauth2.client.registration.apple.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.provider.apple.key-id}")
    private String keyId;
    @Value("${spring.security.oauth2.client.provider.apple.team-id}")
    private String teamId;
    @Value("${spring.security.oauth2.client.provider.apple.private-key}")
    private String privateKeyString;

    private static final String APPLE_JWKS_URL = "https://appleid.apple.com/auth/keys";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ApplePublicKeys cachedApplePublicKeys;
    private long lastCacheUpdateTime = 0;
    // Apple 공개 키를 1시간 동안 캐시합니다
    private static final long CACHE_DURATION_MS = 3600_000;

    @PostConstruct
    private void initPrivateKey() {
        try {
            // 1. Convert \n string to actual newline, then remove headers, footers, and all whitespace/newlines
            String pem = privateKeyString
                    .replace("\\n", "\n") // Convert \n string to actual newline
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", ""); // Removes all whitespace, including \n

            // 2. Base64 decode the remaining string
            byte[] decodedKey = Base64.getDecoder().decode(pem);

            // 3. PKCS#8 format key spec
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);

            // 4. EC algorithm KeyFactory
            KeyFactory keyFactory = KeyFactory.getInstance("EC");

            // 5. Generate PrivateKey
            this.privateKey = keyFactory.generatePrivate(keySpec);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Apple private key.", e);
        }
    }


    public String createClientSecret() {
        Instant now = Instant.now();
        Instant expiration = now.plus(180, ChronoUnit.DAYS); // 6개월 유효

        // JJWT를 사용하여 client secret 빌드
        return Jwts.builder()
                .header()
                    .keyId(keyId) // kid (키 ID)
                    .type("JWT")
                    .and()
                .issuer(teamId) // iss (발급자)
                .issuedAt(Date.from(now)) // iat (발급 시간)
                .expiration(Date.from(expiration)) // exp (만료 시간)
                .audience()
                    .add("https://appleid.apple.com") // aud (수신자)
                    .and()
                .subject(clientId) // sub (주제)
                .signWith(this.privateKey, Jwts.SIG.ES256) // ES256과 우리의 비공개 키로 서명
                .compact();
    }

    public Claims decodeIdToken(String idToken, String nonce) {
        try {
            // 1. [수동 파싱] 헤더에서 kid 추출
            String kid = getKidFromTokenHeader(idToken);

            // 2. kid로 Apple 공개 키 조회
            PublicKey publicKey = getApplePublicKey(kid);
            
            // 3. 조회한 공개 키로 토큰 검증
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(publicKey) // 공개 키로 검증
                    .requireIssuer("https://appleid.apple.com") // iss (발급자)
                    .requireAudience(clientId) // aud (수신자)
                    .require("nonce", nonce) // nonce (재전송 방지 토큰)
                    .build()
                    .parseSignedClaims(idToken);

            // 파싱에 성공하면 서명과 클레임이 유효한 것임
            return jws.getPayload();

        } catch (Exception e) {
            log.error("Failed to decode or validate Apple ID Token: {}", e.getMessage(), e);
            throw new SecurityException("Apple ID Token is invalid.", e);
        }
    }

    private String getKidFromTokenHeader(String idToken) {
        try {
            String[] tokenParts = idToken.split("\\.");
            if (tokenParts.length < 2) { // 헤더, 페이로드 최소 2개
                throw new IllegalArgumentException("Invalid ID Token format");
            }
            String headerPart = tokenParts[0];
            byte[] headerBytes = Base64.getUrlDecoder().decode(headerPart);
            String headerJson = new String(headerBytes, java.nio.charset.StandardCharsets.UTF_8);
            
            JsonNode headerNode = objectMapper.readTree(headerJson);
            if (headerNode.has("kid")) {
                return headerNode.get("kid").asText();
            } else {
                throw new SecurityException("ID Token header does not contain 'kid'");
            }
        } catch (Exception e) {
            throw new SecurityException("Failed to parse ID Token header to find 'kid'", e);
        }
    }

    /**
     * 헬퍼: 캐시에서 올바른 Apple 공개 키를 가져오거나 새로 fetch합니다.
     */
    private PublicKey getApplePublicKey(String kid) {
        ApplePublicKeys publicKeys = getApplePublicKeys(); // 캐시에서 가져오거나 새로 fetch
        
        ApplePublicKey jwk = publicKeys.getKeys().stream()
                .filter(k -> k.getKid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new SecurityException("Apple Public Key not found for kid: " + kid));
        
        // Apple은 RS256을 사용하므로 키 타입은 RSA
        return createPublicKeyFromJwk(jwk);
    }

    /**
     * 헬퍼: Apple의 JWK(n, e 구성요소)를 Java PublicKey 객체로 변환합니다.
     */
    private PublicKey createPublicKeyFromJwk(ApplePublicKey jwk) {
        if (!"RSA".equals(jwk.getKty())) {
            throw new SecurityException("Invalid key type. Expected RSA, got: " + jwk.getKty());
        }
        try {
            // Apple은 n과 e를 Base64URL로 인코딩된 문자열로 제공함
            BigInteger n = new BigInteger(1, Base64.getUrlDecoder().decode(jwk.getN()));
            BigInteger e = new BigInteger(1, Base64.getUrlDecoder().decode(jwk.getE()));

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create public key from JWK", e);
        }
    }

    /**
     * 헬퍼: Apple 공개 키를 가져오고 캐시하는 것을 관리합니다.
     */
    private ApplePublicKeys getApplePublicKeys() {
        // 캐시가 비어있거나 만료되었는지 확인
        if (this.cachedApplePublicKeys == null || (System.currentTimeMillis() - this.lastCacheUpdateTime > CACHE_DURATION_MS)) {
            log.info("Apple public keys cache is empty or expired. Fetching new keys...");
            try {
                URL url = new URL(APPLE_JWKS_URL);
                // Jackson을 사용하여 직접 JWKSet JSON 파싱
                this.cachedApplePublicKeys = objectMapper.readValue(url, ApplePublicKeys.class);
                this.lastCacheUpdateTime = System.currentTimeMillis();

            } catch (Exception e) {
                // fetch 실패 시 예외를 던지지 말고, 가능하다면 이전 캐시 사용
                if (this.cachedApplePublicKeys == null) {
                    throw new RuntimeException("Failed to fetch Apple public keys and no cache is available.", e);
                }
                log.warn("Using stale Apple public key cache due to fetch error.");
            }
        }
        return this.cachedApplePublicKeys;
    }

    /**
     * Apple의 JWKSet (키 목록)을 위한 DTO
     */
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ApplePublicKeys {
        private List<ApplePublicKey> keys;
    }

    /**
     * 단일 Apple 공개 키(JWK)를 위한 DTO
     */
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ApplePublicKey {
        private String kty; // 키 유형 (예: "RSA")
        private String kid; // 키 ID
        private String alg; // 알고리즘 (예: "RS256")
        private String n;   // 모듈러스 (RSA용)
        private String e;   // 지수 (RSA용)
    }
}