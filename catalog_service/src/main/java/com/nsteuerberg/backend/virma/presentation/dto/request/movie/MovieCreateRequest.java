package com.nsteuerberg.backend.virma.presentation.dto.request.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MovieCreateRequest(
        @NotBlank(message = "Title of the film can't be blank")
        String title,
        String description,
        @NotBlank(message = "Cover url of the film can't be blank")
        String coverUrl,
        LocalDate releaseDate,
        @NotBlank(message = "Url of the film can't be blank")
        String fileUrl,
        @NotNull(message = "Duration of the film can't be null")
        Integer durationSeconds
) {
}
