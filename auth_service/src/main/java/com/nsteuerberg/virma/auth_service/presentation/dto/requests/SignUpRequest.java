package com.nsteuerberg.virma.auth_service.presentation.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String confirmPassword
) {
}
