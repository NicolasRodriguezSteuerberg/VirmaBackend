package com.nsteuerberg.backend.virma.presentation.dto.request.serie;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SeasonCreateRequest (
        @NotNull Integer number,
        // permitir crear una season con varios episodios
        List<EpisodeCreateRequest> episodes
){
}
