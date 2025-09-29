package com.nsteuerberg.backend.virma.presentation.dto.request.serie;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SerieCreateRequest (
        @NotNull String title,
        String description,
        String coverUrl,
        // permitir crear una serie con varios episodios
        List<SeasonCreateRequest> seasons
){
}
