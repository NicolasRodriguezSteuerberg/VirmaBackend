package com.nsteuerberg.backend.virma.persistance.repository;

import com.nsteuerberg.backend.virma.persistance.entity.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMovieRepository extends JpaRepository<MovieEntity, Long> {
    Page<MovieEntity> findAll(Pageable pageable);

    Page<MovieEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
