package com.nsteuerberg.backend.virma.service.interfaces;

import com.nsteuerberg.backend.virma.presentation.dto.request.movie.MovieCreateRequest;
import com.nsteuerberg.backend.virma.presentation.dto.response.FilmUserResponse;
import com.nsteuerberg.backend.virma.presentation.dto.response.PagedResponse;
import com.nsteuerberg.backend.virma.presentation.dto.response.movies.CreatedMovieResponse;
import com.nsteuerberg.backend.virma.presentation.dto.response.movies.MovieResponse;
import org.springframework.data.domain.Pageable;


public interface IMovieService {
    CreatedMovieResponse saveFilm(MovieCreateRequest movieCreateRequest);
    PagedResponse<MovieResponse> getPagenableFilm(Pageable pageable);
    MovieResponse getMovieById(Long filmId);
}
