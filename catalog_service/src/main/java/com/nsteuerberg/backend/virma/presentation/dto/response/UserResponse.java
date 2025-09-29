package com.nsteuerberg.backend.virma.presentation.dto.response;

import lombok.Builder;

@Builder
public record UserResponse (
        Long id,
        String username,
        String profileUrl
){
}
