package com.nsteuerberg.backend.virma.presentation.dto.response.serie;

import lombok.Builder;

@Builder
public record SerieInfoResponse (
        Long id,
        String title,
        String description,
        String coverUrl
){
}
