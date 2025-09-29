package com.nsteuerberg.backend.virma.service.implementation;

import com.nsteuerberg.backend.virma.persistance.entity.FilmEntity;
import com.nsteuerberg.backend.virma.persistance.repository.IFilmRepository;
import com.nsteuerberg.backend.virma.presentation.dto.request.film.FilmCreateRequest;
import com.nsteuerberg.backend.virma.presentation.dto.response.*;
import com.nsteuerberg.backend.virma.service.http.responses.user_activity.UserFilmResponse;
import com.nsteuerberg.backend.virma.service.interfaces.IFilmService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements IFilmService {
    private final IFilmRepository filmRepository;
    private final CommonMediaServiceImpl commonMediaService;

    public FilmServiceImpl(IFilmRepository filmRepository, CommonMediaServiceImpl commonMediaService) {
        this.filmRepository = filmRepository;
        this.commonMediaService = commonMediaService;
    }

    @Override
    public void saveFilm(FilmCreateRequest filmCreateRequest) {
        // ToDo Comprobar que exista el archivo de la pelicula y recoger la duracion del video
        filmRepository.save(FilmEntity.builder()
                .title(filmCreateRequest.title())
                .description(filmCreateRequest.description())
                .coverUrl(filmCreateRequest.coverUrl())
                .fileUrl(filmCreateRequest.fileUrl())
                .durationSeconds(filmCreateRequest.durationSeconds())
                .build()
        );
    }

    @Override
    public PagedResponse<FilmUserResponse> getPagenableFilm(Pageable pageable, Long userId) {
        Page<FilmEntity> filmEntityPage = filmRepository.findAll(pageable);
        List<FilmEntity> filmEntities = filmEntityPage.getContent();

        // ToDo recoger la informacion del usuario
        //List<UserFilmEntity> userFilmEntities = userFilmRepository.findByUserIdAndFilmIn(userId, filmEntities);
        List<UserFilmResponse> userFilmEntities = null;
        List<FilmUserResponse> filmUserResponses = mapUserFilms(filmEntityPage.getContent(), userFilmEntities);

        return new PagedResponse<FilmUserResponse>(
                filmUserResponses,
                PageInfo.builder()
                        .number(filmEntityPage.getNumber())
                        .size(filmEntityPage.getSize())
                        .totalPages(filmEntityPage.getTotalPages())
                        .totalElements(filmEntityPage.getTotalElements())
                        .last(filmEntityPage.isLast())
                        .build()
        );
    }

    public FilmUserResponse getFilmById(Long filmId, Long userId) {
        FilmEntity film = filmRepository.findById(filmId).orElseThrow();
        // ToDo recoger la informacion del usuario
        //Optional<UserFilmEntity> userFilm = userFilmRepository.findById(new UserFilmId(userId, filmId));
        Optional<UserFilmResponse> userFilm = Optional.empty();
        return new FilmUserResponse(
            FilmResponse.builder()
                  .id(film.getId())
                  .title(film.getTitle())
                  .durationSeconds(film.getDurationSeconds())
                  .fileUrl(commonMediaService.createUrlByEndpoint(film.getFileUrl()))
                  .coverUrl(commonMediaService.createUrlByEndpoint(film.getCoverUrl()))
                  .build(),
            userFilm.isPresent()
                ? new UserFilmStateResponse(
                    userFilm.get().liked(),
                    userFilm.get().watchedSeconds()
                )
                : null
        );
    }

    private List<FilmUserResponse> mapUserFilms(List<FilmEntity> filmEntities, List<UserFilmResponse> userFilmEntities) {
        // creamos un mapa para que sea mas facil acceder a la info de la peli, cogiendo como clave el id de la pelicula
        Map<Long, UserFilmResponse> userFilmMap;
        if (userFilmEntities != null) {
            userFilmMap = userFilmEntities.stream()
                    .collect(Collectors.toMap(uf -> uf.filmId(), uf -> uf));
        } else {
            userFilmMap = new HashMap<Long, UserFilmResponse>();
        }

        return filmEntities.stream()
                .map(film -> {
                    UserFilmResponse uf = userFilmMap.getOrDefault(
                            film.getId(),
                            new UserFilmResponse(
                                film.getId(),
                                null,
                                null
                            )
                    );
                    return new FilmUserResponse(
                            FilmResponse.builder()
                                    .id(film.getId())
                                    .title(film.getTitle())
                                    .description(film.getDescription())
                                    .coverUrl(commonMediaService.createUrlByEndpoint(film.getCoverUrl()))
                                    .fileUrl(commonMediaService.createUrlByEndpoint(film.getFileUrl()))
                                    .durationSeconds(film.getDurationSeconds())
                                    .build(),
                            new UserFilmStateResponse(
                                    uf.liked(),
                                    uf.watchedSeconds()
                            )
                    );
                }).toList();
    }
}
