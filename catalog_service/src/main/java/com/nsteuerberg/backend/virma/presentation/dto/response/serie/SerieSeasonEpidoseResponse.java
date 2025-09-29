package com.nsteuerberg.backend.virma.presentation.dto.response.serie;

import lombok.Builder;

import java.util.List;

@Builder
public record SerieSeasonEpidoseResponse(
        SerieInfoResponse serieInfo,
        List<SeasonEpisodeResponse> seasonList
) {
}
