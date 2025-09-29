package com.nsteuerberg.backend.virma.persistance.repository;

import com.nsteuerberg.backend.virma.persistance.entity.FilmEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFilmRepository extends JpaRepository<FilmEntity, Long> {
    Page<FilmEntity> findAll(Pageable pageable);

    Page<FilmEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
