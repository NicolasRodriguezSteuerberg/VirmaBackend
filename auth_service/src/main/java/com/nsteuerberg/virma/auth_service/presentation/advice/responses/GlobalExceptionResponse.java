package com.nsteuerberg.virma.auth_service.presentation.advice.responses;

import java.util.Map;

public record GlobalExceptionResponse(
        String type,
        Map<String, String> errors
) {
}
