package com.nsteuerberg.backend.virma.presentation.dto.exceptions;

public record ExceptionResponse(
        String type,
        String message
) {
}
