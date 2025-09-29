package com.nsteuerberg.virma.auth_service.util.token;

import org.apache.logging.log4j.util.InternalException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Component
public class RefreshTokenProvider {

    @Bean
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Bean
    public String hashToken(String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new InternalException(e.getMessage());
        }
    }
}
