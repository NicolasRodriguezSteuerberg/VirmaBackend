package com.nsteuerberg.backend.virma.presentation.dto.response.serie;

import lombok.Builder;

// ToDo agregar la informacion del usuario de los episodios
@Builder
public record EpisodeInfoResponse (
        Long id,
        Integer number,
        String coverUrl,
        Integer durationSeconds,
        Integer userWatchedSeconds
){
}
