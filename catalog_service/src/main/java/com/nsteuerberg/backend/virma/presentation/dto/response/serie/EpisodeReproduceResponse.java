package com.nsteuerberg.backend.virma.presentation.dto.response.serie;

import lombok.Builder;

@Builder
public record EpisodeReproduceResponse(
        Long serieId,
        Long episodeId,
        Integer number,
        // Es posible que no haga falta, ya que los reproductores saben cuanto dura
        Integer durationSeconds,
        String fileUrl,
        // Importante para saber donde quedo el usuario
        Integer watchedSeconds,
        // para que el frontend sepa si hay siguiente episodio
        NextEpisodeResponse nextEpisode

) {
}