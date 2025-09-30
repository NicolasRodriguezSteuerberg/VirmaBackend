package com.nsteuerberg.backend.virma.presentation.controller;

import com.nsteuerberg.backend.virma.presentation.dto.request.movie.MovieCreateRequest;
import com.nsteuerberg.backend.virma.presentation.dto.response.FilmUserResponse;
import com.nsteuerberg.backend.virma.presentation.dto.response.PagedResponse;
import com.nsteuerberg.backend.virma.presentation.dto.response.movies.CreatedMovieResponse;
import com.nsteuerberg.backend.virma.presentation.dto.response.movies.MovieResponse;
import com.nsteuerberg.backend.virma.service.implementation.CommonMediaServiceImpl;
import com.nsteuerberg.backend.virma.service.implementation.MovieServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("movie")
public class MovieController {
    private final MovieServiceImpl movie;
    private final CommonMediaServiceImpl commonMediaService;

    public MovieController(MovieServiceImpl movie, CommonMediaServiceImpl commonMediaService) {
        this.movie = movie;
        this.commonMediaService = commonMediaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedMovieResponse createMovie(@RequestBody @Valid MovieCreateRequest movieCreateRequest) throws FileNotFoundException {
        if (!commonMediaService.isUrlValid(movieCreateRequest.fileUrl(), false)) {
            throw new FileNotFoundException("Film url doesn't exists");
        }
        if (!commonMediaService.isUrlValid(movieCreateRequest.coverUrl(), true)){
            throw new FileNotFoundException("Cover url doesn't exists");
        }
        return movie.saveFilm(movieCreateRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedResponse<MovieResponse> getFilms(
            Pageable pageable
    ) {
        return movie.getPagenableFilm(pageable);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public MovieResponse getMovieById(
            @PathVariable Long id
    ) {
        return movie.getMovieById(id);
    }
}
