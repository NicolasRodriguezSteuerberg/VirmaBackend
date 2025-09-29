package com.nsteuerberg.backend.virma.service.interfaces;

import com.nsteuerberg.backend.virma.presentation.dto.request.serie.EpisodeCreateRequest;
import com.nsteuerberg.backend.virma.presentation.dto.request.serie.SeasonCreateRequest;
import com.nsteuerberg.backend.virma.presentation.dto.request.serie.SerieCreateRequest;
import com.nsteuerberg.backend.virma.presentation.dto.response.PagedResponse;
import com.nsteuerberg.backend.virma.presentation.dto.response.serie.*;
import org.springframework.data.domain.Pageable;

public interface ISerieService {
    PagedResponse<SerieInfoResponse> getPageableSerieInfo(Pageable pageable);
    SerieSeasonEpidoseResponse getSerieCompleteInfo(Long serieId, Long userId);
    EpisodeReproduceResponse getEpisode(Long episodeId, Long userId);
    SerieSeasonEpidoseResponse createSerie(SerieCreateRequest serieCreateRequest);
    SeasonEpisodeResponse createSeason(Long serieId, SeasonCreateRequest seasonCreateRequest);
    EpisodeInfoResponse createEpisode(Long serieId, Long seasonId, EpisodeCreateRequest episodeCreateRequest);
}
