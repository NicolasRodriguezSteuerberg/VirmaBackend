package com.nsteuerberg.backend.virma.service.interfaces;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;

import java.security.interfaces.RSAPublicKey;
import java.util.Set;

public interface IAuthService {
    RSAPublicKey getPublicKey();

    DecodedJWT validateToken(String token);

    String extractUsername(DecodedJWT decodedJWT);

    Set<GrantedAuthority> authorities(DecodedJWT decodedJWT);
}
