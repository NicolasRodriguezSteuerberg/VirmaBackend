package com.nsteuerberg.virma.auth_service.util.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nsteuerberg.virma.auth_service.presentation.dto.responses.RsaPublicKeyResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;

@Component
public class JwtProvider {

    @Value("${token.jwt.with-user}")
    private String userGenerator;

    @Value("${token.jwt.expired-minutes}")
    private Long expiredMinutes;

    @Autowired
    private final RSAPublicKey rsaPublicKey;
    @Autowired
    private final RSAPrivateKey rsaPrivateKey;
    @Getter
    private final RsaPublicKeyResponse publicKeyResponse;

    public JwtProvider(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) {
        this.rsaPublicKey = rsaPublicKey;
        this.rsaPrivateKey = rsaPrivateKey;
        this.publicKeyResponse = getPublicKey(rsaPublicKey);
    }

    public String createToken(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);

        Instant now = Instant.now();

        return JWT.create()
                .withIssuer(userGenerator)
                .withSubject(userId)
                .withClaim("authorities", authorities)
                .withIssuedAt(now)
                .withExpiresAt(now.plus(expiredMinutes, ChronoUnit.MINUTES))
                .sign(algorithm);
    }

    private RsaPublicKeyResponse getPublicKey(RSAPublicKey publicKey) {
        String modulus = Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.getModulus().toByteArray());
        String exponent = Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.getPublicExponent().toByteArray());
        return new RsaPublicKeyResponse("RSA", modulus, exponent);
    }
}
