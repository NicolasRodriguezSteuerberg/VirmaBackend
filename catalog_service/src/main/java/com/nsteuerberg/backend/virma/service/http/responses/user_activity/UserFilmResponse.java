package com.nsteuerberg.backend.virma.service.http.responses.user_activity;

public record UserFilmResponse(
        Long filmId,
        Boolean liked,
        Integer watchedSeconds
){}

