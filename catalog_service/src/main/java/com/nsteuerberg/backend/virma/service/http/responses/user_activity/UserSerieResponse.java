package com.nsteuerberg.backend.virma.service.http.responses.user_activity;

import java.time.LocalDateTime;

public record UserSerieResponse (
        Boolean liked,
        Long lastEpisodeId,
        LocalDateTime lastTimeWatched
) {}
