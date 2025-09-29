package com.nsteuerberg.backend.virma.persistance.repository;

import com.nsteuerberg.backend.virma.persistance.entity.SerieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISerieRepository extends JpaRepository<SerieEntity, Long> {
    // De forma LAZY
    Page<SerieEntity> findAll(Pageable pageable);
    Page<SerieEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // Forma EAGER no funciona, da error de multipleBagFetchException
    @EntityGraph(attributePaths = {"seasonEntities", "seasonEntities.episodeEntities"})
    Optional<SerieEntity> findSerieById(Long id);
}
