package com.nsteuerberg.backend.virma.presentation.dto.response;

import lombok.Builder;

@Builder
public record PageInfo (
        int number,
        int size,
        long totalElements,
        int totalPages,
        boolean last
){
}
