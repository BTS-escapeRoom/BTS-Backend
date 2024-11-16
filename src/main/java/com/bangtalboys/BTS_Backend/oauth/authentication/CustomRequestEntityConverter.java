package com.bangtalboys.BTS_Backend.oauth.authentication;


//public class CustomRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {
//
//    private OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;
//
//    public CustomRequestEntityConverter() {
//        defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
//    }
//
//    private final String APPLE_URL = "https://appleid.apple.com";
//    private final String APPLE_KEY_PATH = "static/apple/{apple로 부터 받은 key파일명}.p8";
//    private final String APPLE_CLIENT_ID = "client_secret";
//    private final String APPLE_TEAM_ID = "{apple로 부터 받은 teamId}";
//    private final String APPLE_KEY_ID = "{apple로 부터 받은 keyId}";
//
//
//    @Override
//    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
//        RequestEntity<?> entity = defaultConverter.convert(req);
//        String registrationId = req.getClientRegistration().getRegistrationId();
//        MultiValueMap<String, String> params = (MultiValueMap<String, String>) entity.getBody();
//        if (registrationId.contains("apple")) {
//            params.set("client_secret", createClientSecret());
//        }
//        return new RequestEntity<>(params, entity.getHeaders(),
//                entity.getMethod(), entity.getUrl());
//    }
//
////    public PrivateKey getPrivateKey() throws IOException {
////        ClassPathResource resource = new ClassPathResource(APPLE_KEY_PATH);
////        // 배포시 jar 파일을 찾지 못함
////        //String privateKey = new String(Files.readAllBytes(Paths.get(resource.getURI())));
////
////        InputStream in = resource.getInputStream();
////        PEMParser pemParser = new PEMParser(new StringReader(IOUtils.toString(in, StandardCharsets.UTF_8)));
////        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
////        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
////        return converter.getPrivateKey(object);
////    }
//
//    public String createClientSecret() {
//        var resource = new ClassPathResource("static/private_key.p8"); // 1
//        try (
//                InputStream inputStream = resource.getInputStream();
//                StringReader reader = new StringReader(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8))
//        ) {
//            var pemParser = new PEMParser(reader); // 2
//            var pemObject = pemParser.readObject();
//            var converter = new JcaPEMKeyConverter();
//            var privateKey = converter.getPrivateKey(
//                    PrivateKeyInfo.getInstance(pemObject)
//            );
//            var now = System.currentTimeMillis();
//            var builder = Jwts.builder();
////            builder.header().add(tokenHeader()) // 3
////                    .and();
////            builder.signWith(privateKey) // 4
////                    .issuer(teamId)
////                    .issuedAt(new Date(now))
////                    .expiration(new Date(now + 10 * MINUTE))
////                    .audience().add(AUDIENCE)
////                    .subject(clientId)
////                    .and();
//            return builder.compact(); // 5
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
