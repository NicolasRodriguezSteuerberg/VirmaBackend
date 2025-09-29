package com.nsteuerberg.backend.virma.presentation.dto.request.film;

public record FilmCreateRequest(
        String title,
        String description,
        String coverUrl,
        String fileUrl,
        Integer durationSeconds
) {
}
