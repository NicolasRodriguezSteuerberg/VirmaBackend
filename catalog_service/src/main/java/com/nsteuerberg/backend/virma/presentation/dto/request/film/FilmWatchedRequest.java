package com.nsteuerberg.backend.virma.presentation.dto.request.film;

public record FilmWatchedRequest (
        Long filmId,
        Integer watchedSeconds
){
}
