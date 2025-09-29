package com.nsteuerberg.virma.auth_service.presentation.dto.responses;

public record RsaPublicKeyResponse (
        String kty,
        String n,
        String e
){
}
