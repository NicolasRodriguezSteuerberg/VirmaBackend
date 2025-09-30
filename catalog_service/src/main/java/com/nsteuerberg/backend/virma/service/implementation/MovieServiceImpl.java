package com.nsteuerberg.backend.virma.service.implementation;

import com.nsteuerberg.backend.virma.persistance.entity.MovieEntity;
import com.nsteuerberg.backend.virma.persistance.repository.IMovieRepository;
import com.nsteuerberg.backend.virma.presentation.dto.request.movie.MovieCreateRequest;
import com.nsteuerberg.backend.virma.presentation.dto.response.*;
import com.nsteuerberg.backend.virma.presentation.dto.response.movies.CreatedMovieResponse;
import com.nsteuerberg.backend.virma.presentation.dto.response.movies.MovieResponse;
import com.nsteuerberg.backend.virma.service.exceptions.ContentNotExistsException;
import com.nsteuerberg.backend.virma.service.http.responses.user_activity.UserFilmResponse;
import com.nsteuerberg.backend.virma.service.interfaces.IMovieService;
import com.nsteuerberg.backend.virma.utils.constants.ContentTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements IMovieService {
    private final IMovieRepository movieRepository;
    private final CommonMediaServiceImpl commonMediaService;

    public MovieServiceImpl(IMovieRepository movieRepository, CommonMediaServiceImpl commonMediaService) {
        this.movieRepository = movieRepository;
        this.commonMediaService = commonMediaService;
    }

    @Override
    public CreatedMovieResponse saveFilm(MovieCreateRequest movieCreateRequest) {
        MovieEntity movie = movieRepository.save(
                MovieEntity.builder()
                        .contentType(ContentTypeEnum.MOVIE)
                        .title(movieCreateRequest.title())
                        .description(movieCreateRequest.description())
                        .coverUrl(movieCreateRequest.coverUrl())
                        .releaseDate(movieCreateRequest.releaseDate())
                        .durationSeconds(movieCreateRequest.durationSeconds())
                        .fileUrl(movieCreateRequest.fileUrl())
                .build()
        );
        return CreatedMovieResponse.builder()
                .title(movie.getTitle())
                .description(movie.getDescription())
                .coverUrl(commonMediaService.createUrlByEndpoint(movie.getCoverUrl()))
                .releaseDate(movie.getReleaseDate())
                .durationSeconds(movie.getDurationSeconds())
                .fileUrl(commonMediaService.createUrlByEndpoint(movie.getFileUrl()))
                .build();

    }

    // ToDo devolver la informacion de las peliculas con el tiempo visto del usuario
    @Override
    public PagedResponse<MovieResponse> getPagenableFilm(Pageable pageable) {
        Page<MovieEntity> filmEntityPage = movieRepository.findAll(pageable);
        List<MovieEntity> filmEntities = filmEntityPage.getContent();
        List<MovieResponse> movieResponses = filmEntities.stream()
                .map(this::toMovieResponse)
                .toList();

        return new PagedResponse<MovieResponse>(
                movieResponses,
                PageInfo.builder()
                        .number(filmEntityPage.getNumber())
                        .size(filmEntityPage.getSize())
                        .totalPages(filmEntityPage.getTotalPages())
                        .totalElements(filmEntityPage.getTotalElements())
                        .last(filmEntityPage.isLast())
                        .build()
        );
    }

    // ToDo devolver el tiempo visto del usuario
    @Override
    public MovieResponse getMovieById(Long filmId) {
        MovieEntity film = movieRepository.findById(filmId)
                .orElseThrow(() -> new ContentNotExistsException("Film with id " + filmId + " doesn't exist"));
        return toMovieResponse(film);
    }

    private MovieResponse toMovieResponse(MovieEntity movieEntity){
        return MovieResponse.builder()
                .title(movieEntity.getTitle())
                .description(movieEntity.getDescription())
                .releaseDate(movieEntity.getReleaseDate())
                .coverUrl(movieEntity.getCoverUrl())
                .durationSeconds(movieEntity.getDurationSeconds())
                .build();
    }
}
