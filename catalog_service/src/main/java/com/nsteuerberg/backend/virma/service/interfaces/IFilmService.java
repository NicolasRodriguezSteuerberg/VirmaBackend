package com.nsteuerberg.backend.virma.service.interfaces;

import com.nsteuerberg.backend.virma.presentation.dto.request.film.FilmCreateRequest;
import com.nsteuerberg.backend.virma.presentation.dto.response.FilmUserResponse;
import com.nsteuerberg.backend.virma.presentation.dto.response.PagedResponse;
import org.springframework.data.domain.Pageable;


public interface IFilmService {
    void saveFilm(FilmCreateRequest filmCreateRequest);
    PagedResponse<FilmUserResponse> getPagenableFilm(Pageable pageable, Long userId);
}
