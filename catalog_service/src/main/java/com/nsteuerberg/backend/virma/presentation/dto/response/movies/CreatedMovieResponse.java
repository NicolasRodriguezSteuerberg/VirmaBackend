package com.nsteuerberg.backend.virma.presentation.dto.response.movies;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreatedMovieResponse(
        String title,
        String description,
        String coverUrl,
        LocalDate releaseDate,
        Integer durationSeconds,
        String fileUrl
){
}
