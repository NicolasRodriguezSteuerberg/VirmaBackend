package com.nsteuerberg.backend.virma.presentation.controller;

import com.nsteuerberg.backend.virma.presentation.dto.request.film.FilmCreateRequest;
import com.nsteuerberg.backend.virma.presentation.dto.response.FilmUserResponse;
import com.nsteuerberg.backend.virma.presentation.dto.response.PagedResponse;
import com.nsteuerberg.backend.virma.service.implementation.CommonMediaServiceImpl;
import com.nsteuerberg.backend.virma.service.implementation.FilmServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("movie")
public class FilmController {
    private final FilmServiceImpl filmService;
    private final CommonMediaServiceImpl commonMediaService;

    public FilmController(FilmServiceImpl filmService, CommonMediaServiceImpl commonMediaService) {
        this.filmService = filmService;
        this.commonMediaService = commonMediaService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    // ToDo cambiarlo por created
    public void createFilm(@RequestBody @Valid FilmCreateRequest filmCreateRequest) throws FileNotFoundException {
        if (!commonMediaService.isUrlValid(filmCreateRequest.fileUrl())) {
            throw new FileNotFoundException("No existe el archivo o no pertenece al servidor");
        }
        filmService.saveFilm(filmCreateRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedResponse<FilmUserResponse> getFilms(
            Pageable pageable,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getPrincipal().toString());
        return filmService.getPagenableFilm(pageable, userId);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public FilmUserResponse getFilm(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getPrincipal().toString());
        return filmService.getFilmById(id, userId);
    }
}
