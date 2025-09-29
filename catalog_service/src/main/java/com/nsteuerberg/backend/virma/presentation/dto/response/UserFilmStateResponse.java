package com.nsteuerberg.backend.virma.presentation.dto.response;

public record UserFilmStateResponse (
        Boolean liked,
        Integer watchedSeconds
){
}
