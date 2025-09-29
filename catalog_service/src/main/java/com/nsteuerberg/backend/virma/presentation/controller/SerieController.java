package com.nsteuerberg.backend.virma.presentation.controller;

import com.nsteuerberg.backend.virma.presentation.dto.request.serie.EpisodeCreateRequest;
import com.nsteuerberg.backend.virma.presentation.dto.request.serie.SeasonCreateRequest;
import com.nsteuerberg.backend.virma.presentation.dto.request.serie.SerieCreateRequest;
import com.nsteuerberg.backend.virma.presentation.dto.response.PagedResponse;
import com.nsteuerberg.backend.virma.presentation.dto.response.serie.*;
import com.nsteuerberg.backend.virma.service.implementation.SerieServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("series")
public class SerieController {
    private final SerieServiceImpl serieService;

    public SerieController(SerieServiceImpl serieService) {
        this.serieService = serieService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedResponse<SerieInfoResponse> serieInfoResponsePagedResponse (Pageable pageable) {
        return serieService.getPageableSerieInfo(pageable);
    }

    @GetMapping("info/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SerieSeasonEpidoseResponse serieSeasonEpidoseResponse (
            @PathVariable Long id,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getPrincipal().toString());
        return serieService.getSerieCompleteInfo(id, userId);
    }

    @GetMapping("episode/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EpisodeReproduceResponse getEpisode(
            @PathVariable(name = "id") Long episodeId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getPrincipal().toString());
        return serieService.getEpisode(episodeId, userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SerieSeasonEpidoseResponse createSerie(
            @RequestBody @Valid SerieCreateRequest serieCreateRequest
    ) {
        return serieService.createSerie(serieCreateRequest);
    }

    @PostMapping("{id}/seasons")
    @ResponseStatus(HttpStatus.CREATED)
    public SeasonEpisodeResponse createSeason(
            @RequestBody @Valid SeasonCreateRequest seasonCreateRequest,
            @PathVariable(name = "id") Long serieId
    ) {
        return serieService.createSeason(serieId, seasonCreateRequest);
    }

    @PostMapping("{serieId}/seasons/{seasonId}")
    @ResponseStatus(HttpStatus.CREATED)
    public EpisodeInfoResponse createEpisode(
            @PathVariable(name = "serieId") Long serieId,
            @PathVariable(name = "seasonId") Long seasonId,
            @RequestBody @Valid EpisodeCreateRequest episodeCreateRequest
    ){
        return serieService.createEpisode(serieId, seasonId, episodeCreateRequest);
    }
}
