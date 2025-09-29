package com.nsteuerberg.virma.auth_service.presentation.dto.responses;

public record TokenAuthenticationResponse (
        String refreshToken,
        String accessToken
){
}
