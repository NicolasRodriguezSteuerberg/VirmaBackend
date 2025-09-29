package com.nsteuerberg.backend.virma.presentation.dto.response;

import java.util.List;

public record PagedResponse<T> (
        List<T> content,
        PageInfo page
){
}
