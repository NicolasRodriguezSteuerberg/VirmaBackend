package com.nsteuerberg.backend.virma.presentation.dto.response;

public record FilmUserResponse(
        FilmResponse film,
        UserFilmStateResponse userState
){
}
