package com.nsteuerberg.backend.virma.presentation.dto.response;

import lombok.Builder;

@Builder
public record FilmResponse (
        Long id,
        String title,
        String description,
        String coverUrl,
        String fileUrl,
        Integer durationSeconds
){
}
