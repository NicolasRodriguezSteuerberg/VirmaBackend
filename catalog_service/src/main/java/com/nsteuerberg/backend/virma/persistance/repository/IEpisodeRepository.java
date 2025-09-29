package com.nsteuerberg.backend.virma.persistance.repository;

import com.nsteuerberg.backend.virma.persistance.entity.EpisodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEpisodeRepository extends JpaRepository<EpisodeEntity, Long> {
    @Query("SELECT e FROM EpisodeEntity e where e.season.id = :seasonId and e.number = :nextEpisodeNumber")
    Optional<EpisodeEntity> findNextInSameSeason(@Param("seasonId") Long seasonId, @Param("nextEpisodeNumber") Integer nextEpisodeNumber);

    @Query("SELECT e FROM EpisodeEntity e where e.season.serie.id = :serieId AND e.season.number = :nextSeasonNumber AND e.number = 1")
    Optional<EpisodeEntity> findFirstInNextSeason(@Param("serieId") Long serieId, @Param("nextSeasonNumber") Integer nextSeasonNumber);
}
