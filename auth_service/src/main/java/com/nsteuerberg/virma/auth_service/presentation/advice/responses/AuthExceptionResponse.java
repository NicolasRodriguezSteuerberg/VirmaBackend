package com.nsteuerberg.virma.auth_service.presentation.advice.responses;

public record AuthExceptionResponse(
        String type,
        String message
) {
}
