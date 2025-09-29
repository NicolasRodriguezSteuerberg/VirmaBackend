package com.nsteuerberg.backend.virma.persistance.repository;

import com.nsteuerberg.backend.virma.persistance.entity.SeasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISeasonRepository extends JpaRepository<SeasonEntity, Long> {

}
