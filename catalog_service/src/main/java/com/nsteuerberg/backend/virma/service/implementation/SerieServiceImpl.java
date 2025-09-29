package com.nsteuerberg.backend.virma.service.implementation;

import com.nsteuerberg.backend.virma.persistance.entity.EpisodeEntity;
import com.nsteuerberg.backend.virma.persistance.entity.SeasonEntity;
import com.nsteuerberg.backend.virma.persistance.entity.SerieEntity;
import com.nsteuerberg.backend.virma.persistance.repository.IEpisodeRepository;
import com.nsteuerberg.backend.virma.persistance.repository.ISeasonRepository;
import com.nsteuerberg.backend.virma.persistance.repository.ISerieRepository;
import com.nsteuerberg.backend.virma.presentation.dto.request.serie.EpisodeCreateRequest;
import com.nsteuerberg.backend.virma.presentation.dto.request.serie.SeasonCreateRequest;
import com.nsteuerberg.backend.virma.presentation.dto.request.serie.SerieCreateRequest;
import com.nsteuerberg.backend.virma.presentation.dto.response.PageInfo;
import com.nsteuerberg.backend.virma.presentation.dto.response.PagedResponse;
import com.nsteuerberg.backend.virma.presentation.dto.response.serie.*;
import com.nsteuerberg.backend.virma.service.interfaces.ISerieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class SerieServiceImpl implements ISerieService {
    private static final Logger log = LoggerFactory.getLogger(SerieServiceImpl.class);

    private final ISerieRepository serieRepository;
    private final ISeasonRepository seasonRepository;
    private final IEpisodeRepository episodeRepository;
    private final CommonMediaServiceImpl commonMediaService;

    public SerieServiceImpl(ISerieRepository serieRepository, ISeasonRepository seasonRepository, IEpisodeRepository episodeRepository, CommonMediaServiceImpl commonMediaService) {
        this.serieRepository = serieRepository;
        this.seasonRepository = seasonRepository;
        this.episodeRepository = episodeRepository;
        this.commonMediaService = commonMediaService;
    }

    @Override
    public PagedResponse<SerieInfoResponse> getPageableSerieInfo(Pageable pageable) {
        log.info("GET_PAGEABLE_SERIE_INFO:: Recogiendo la informacion de las series...");
        Page<SerieEntity> serieEntityPage = serieRepository.findAll(pageable);
        log.info("GET_PAGEABLE_SERIE_INFO:: Parseando las series a la Lista de SerieInfoResponse...");
        List<SerieInfoResponse> serieInfoResponses = serieEntityPage.getContent().stream()
                .map(this::createSerieInfo)
                .toList();
        log.info("GET_PAGEABLE_SERIE_INFO:: Construyendo la respueta...");
        return new PagedResponse<>(
                serieInfoResponses,
                PageInfo.builder()
                        .number(serieEntityPage.getNumber())
                        .size(serieEntityPage.getSize())
                        .totalElements(serieEntityPage.getTotalElements())
                        .totalPages(serieEntityPage.getTotalPages())
                        .last(serieEntityPage.isLast())
                        .build()
        );
    }

    @Override
    public SerieSeasonEpidoseResponse getSerieCompleteInfo(Long serieId, Long userId) {
        log.info("GET_SERIE_COMPLETE_INFO:: Recogiendo la informacion completa de la serie: {}", serieId);
        SerieEntity serieEntity = serieRepository.findById(serieId).orElseThrow();
        log.info("GET_SERIE_COMPLETE_INFO:: Construyendo la informacion completa de la serie: {}", serieId);

        // ToDo recoger la informacion de la serie
        return createSerieSeasonEpisodeResponse(serieEntity, null);
    }

    @Override
    public EpisodeReproduceResponse getEpisode(Long episodeId, Long userId) {
        log.info("GET_EPISODE:: Recogiendo la informacion del episodio {} para el usuario {}", episodeId, userId);
        EpisodeEntity episode = episodeRepository.findById(episodeId).orElseThrow();
        SeasonEntity season = episode.getSeason();
        Long serieId = season.getId();
        // recogemos el siguiente episodio
        EpisodeEntity nextEpisode = episodeRepository.findNextInSameSeason(season.getId(), episode.getNumber()+1)
                .orElse(episodeRepository.findFirstInNextSeason(serieId, season.getNumber()+1)
                        .orElse(null)
                );
        log.info("GET_EPISODE:: Devolviendo la respuesta de la informacion del episodio {} para el usuario {}", episodeId, userId);
        return EpisodeReproduceResponse.builder()
                .serieId(serieId)
                .episodeId(episodeId)
                .number(episode.getNumber())
                .fileUrl(commonMediaService.createUrlByEndpoint(episode.getFileUrl()))
                .durationSeconds(episode.getDurationSeconds())
                .watchedSeconds(null) // ToDo agregar llamada al servicio
                .nextEpisode(nextEpisode!=null
                    ? new NextEpisodeResponse(nextEpisode.getId(), nextEpisode.getNumber())
                    : null
                )
                .build();
    }

    @Override
    public SerieSeasonEpidoseResponse createSerie(SerieCreateRequest serieCreateRequest) {
        log.info("CREATE_SERIE:: Creando serie {}...", serieCreateRequest.title());
        SerieEntity serieEntity = SerieEntity.builder()
                .title(serieCreateRequest.title())
                .description(serieCreateRequest.description())
                .coverUrl(serieCreateRequest.coverUrl())
                .build();
        if (serieCreateRequest.seasons() != null && !serieCreateRequest.seasons().isEmpty()) {
            log.info("CREATE_SERIE:: La serie {} tiene informacion de temporadas, agregandolas...", serieCreateRequest.title());
            serieCreateRequest.seasons().forEach(season -> {
                log.info("CREATE_SERIE:: Creando la temporada {} para la serie {}", season.number(), serieCreateRequest.title());
                SeasonEntity seasonEntity = SeasonEntity.builder()
                        .number(season.number())
                        .build();

                addEpisodesToSeason(season.episodes(), seasonEntity, serieEntity);

                serieEntity.addSeason(seasonEntity);
            });
        }
        log.info("CREATE_SERIE:: Guardando la serie {} en la base de datos...", serieCreateRequest.title());
        SerieEntity saveSerie = serieRepository.save(serieEntity);
        log.info("CREATE_SERIE:: Construyendo la respuesta para la serie {}", serieCreateRequest.title());


        return createSerieSeasonEpisodeResponse(saveSerie, null);
    }

    @Override
    public SeasonEpisodeResponse createSeason(Long serieId, SeasonCreateRequest seasonCreateRequest) {
        log.info("CREATE_SEASON:: Creando nueva temporada...");
        SerieEntity serie = serieRepository.findById(serieId).orElseThrow();
        log.info("CREATE_SEASON:: Creando nueva temporada para la serie {}", serie.getTitle());
        SeasonEntity season = SeasonEntity.builder()
                .number(seasonCreateRequest.number())
                .build();
        addEpisodesToSeason(seasonCreateRequest.episodes(), season, serie);

        // se podría agregar a la serie la temporada y guardar toda la serie, pero puede ser algo ineficiente
        season.setSerie(serie);
        log.info("CREATE_SEASON:: Guardando la temporada {} de la serie {}", season.getNumber(), serie.getTitle());
        SeasonEntity savedSeason = seasonRepository.save(season);
        return createSeasonInfoResponse(savedSeason, null);
    }

    @Override
    public EpisodeInfoResponse createEpisode(Long serieId, Long seasonId, EpisodeCreateRequest episodeCreateRequest) {
        // comprobamos que la temporada a la que se quiere agregar el episodio este en la misma serie
        SeasonEntity season = seasonRepository.findById(seasonId).orElseThrow();
        if (serieId != season.getSerie().getId()) throw new IllegalArgumentException("El id de la temporada no corresponde con el id de la serie");
        EpisodeEntity episode = EpisodeEntity.builder()
                .number(episodeCreateRequest.number())
                .durationSeconds(episodeCreateRequest.durationSeconds())
                .coverUrl(episodeCreateRequest.coverUrl() == null
                        ? season.getSerie().getCoverUrl()
                        : episodeCreateRequest.coverUrl()
                )
                .fileUrl(episodeCreateRequest.fileUrl())
                .season(season)
                .build();
        EpisodeEntity savedEpisode = episodeRepository.save(episode);
        return createEpisodeInfoResponse(savedEpisode, null);
    }


    private void addEpisodesToSeason(List<EpisodeCreateRequest> episodes, SeasonEntity season, SerieEntity serie) {
        if (episodes == null || episodes.isEmpty()) return;
        log.info("ADD_EPISODES:: La temporada {} de la serie {} tiene episodios, agregandolos...", season.getNumber(), serie.getTitle());
        episodes.stream()
                .map(episode -> EpisodeEntity.builder()
                        .number(episode.number())
                        .durationSeconds(episode.durationSeconds())
                        // si no hay un cover especifo para el episodio le ponemos el de la propia serie
                        .coverUrl(episode.coverUrl()==null
                                ? serie.getCoverUrl()
                                : episode.coverUrl()
                        )
                        .fileUrl(episode.fileUrl())
                        .build()
                )
                .forEach(episode -> {
                    if (!commonMediaService.isUrlValid(episode.getFileUrl())) throw new NoSuchElementException(String.format(
                            "La url %s del episodio S%d:Ep%d de la serie %s es invalido",
                            episode.getFileUrl(), season.getNumber(), episode.getNumber(), serie.getTitle()
                    ));
                    log.info("Agregando el episodio {} a la temporada {} de la serie {}", episode.getNumber(), season.getNumber(), serie.getTitle());
                    season.addEpisode(episode);
                });
    }

    private SerieSeasonEpidoseResponse createSerieSeasonEpisodeResponse(SerieEntity serie, Map<Long, Integer> userWatchedSecondsEp) {
        return SerieSeasonEpidoseResponse.builder()
                .serieInfo(createSerieInfo(serie))
                .seasonList(serie.getSeasonEntities()==null
                        ? List.of()
                        : serie.getSeasonEntities().stream()
                            .map(season -> createSeasonInfoResponse(season, userWatchedSecondsEp))
                            .toList()
                )
                .build();
    }

    private SerieInfoResponse createSerieInfo(SerieEntity serie) {
        return SerieInfoResponse.builder()
                .id(serie.getId())
                .title(serie.getTitle())
                .description(serie.getDescription())
                .coverUrl(commonMediaService.createUrlByEndpoint(serie.getCoverUrl()))
                .build();
    }

    private SeasonEpisodeResponse createSeasonInfoResponse(SeasonEntity season, Map<Long, Integer> userWatchedSecondsEp) {
        return SeasonEpisodeResponse.builder()
                .id(season.getId())
                .number(season.getNumber())
                .episodeList(season.getEpisodeEntities()==null || season.getEpisodeEntities().isEmpty()
                        ? List.of()
                        : season.getEpisodeEntities().stream()
                            .map(episode -> createEpisodeInfoResponse(episode, userWatchedSecondsEp))
                            .toList()
                )
                .build();
    }

    private EpisodeInfoResponse createEpisodeInfoResponse(EpisodeEntity episode, Map<Long, Integer> userWatchedSecondsEp){
        Integer watchedSeconds = null;
        if (userWatchedSecondsEp != null) watchedSeconds = userWatchedSecondsEp.get(episode.getId());
        return EpisodeInfoResponse.builder()
                .id(episode.getId())
                .durationSeconds(episode.getDurationSeconds())
                .userWatchedSeconds(watchedSeconds)
                .number(episode.getNumber())
                .coverUrl(commonMediaService.createUrlByEndpoint(episode.getCoverUrl()))
                .build();
    }

}
