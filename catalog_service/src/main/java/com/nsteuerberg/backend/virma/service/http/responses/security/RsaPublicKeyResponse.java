package com.nsteuerberg.backend.virma.service.http.responses.security;

public record RsaPublicKeyResponse (
        String kty,
        String n,
        String e
){
}
