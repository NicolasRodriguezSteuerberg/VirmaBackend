package com.nsteuerberg.backend.virma.presentation.dto.request.serie;

public record EpisodeWatchedRequest (
        Long serieId,
        Long episodeId,
        Integer watchedSeconds
){
}
