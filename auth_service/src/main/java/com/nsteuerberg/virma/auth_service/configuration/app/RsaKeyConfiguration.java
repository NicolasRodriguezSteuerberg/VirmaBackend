package com.nsteuerberg.virma.auth_service.configuration.app;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class RsaKeyConfiguration {

    @Value("${token.jwt.rsa.private-key}")
    private Resource privateKeyResource;
    @Value("${token.jwt.rsa.public-key}")
    private Resource publicKeyResource;

    @Bean
    public RSAPrivateKey getRsaPrivateKey() throws Exception{
        String privateKeyPEM = new String(
                privateKeyResource.getInputStream().readAllBytes(),
                java.nio.charset.StandardCharsets.UTF_8
        );

        String strippedKey = privateKeyPEM
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decodedKey = Base64.getDecoder().decode(strippedKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    @Bean
    public RSAPublicKey getRsaPublicKey() throws Exception {
        String publicKeyPEM = new String(
                publicKeyResource.getInputStream().readAllBytes(),
                java.nio.charset.StandardCharsets.UTF_8
        );

        String strippedKey = publicKeyPEM
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decodedKey = Base64.getDecoder().decode(strippedKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

}
