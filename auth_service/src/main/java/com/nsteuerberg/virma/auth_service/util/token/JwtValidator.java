package com.nsteuerberg.virma.auth_service.util.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtValidator {
    private final String startToken = "Bearer ";

    private final RSAPublicKey rsaPublicKey;

    public JwtValidator(RSAPublicKey rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }

    public DecodedJWT validateToken(String token) {
        try {
            if (token.startsWith(startToken)){
                token = token.substring(startToken.length());
            }
            Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException e){
            throw new JWTVerificationException(e.getMessage());
        }
    }

    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public String extractIssuer(DecodedJWT decodedJWT) {
        return decodedJWT.getIssuer();
    }

    public Set<GrantedAuthority> extractAuthorities(DecodedJWT decodedJWT) {
        Claim claim = decodedJWT.getClaim("authorities");
        return claim.asList(String.class).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
