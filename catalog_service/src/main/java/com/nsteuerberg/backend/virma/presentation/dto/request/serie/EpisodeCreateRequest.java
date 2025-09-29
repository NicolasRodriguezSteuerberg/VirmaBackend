package com.nsteuerberg.backend.virma.presentation.dto.request.serie;

import jakarta.validation.constraints.NotNull;

public record EpisodeCreateRequest (
        @NotNull Integer number,
        @NotNull Integer durationSeconds,
        @NotNull String fileUrl,
        String coverUrl
){
}
