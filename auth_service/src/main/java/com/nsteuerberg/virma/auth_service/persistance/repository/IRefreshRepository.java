package com.nsteuerberg.virma.auth_service.persistance.repository;

import com.nsteuerberg.virma.auth_service.persistance.entity.RefreshTokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRefreshRepository extends MongoRepository<RefreshTokenEntity, String> {
    Optional<RefreshTokenEntity> findByUserIdAndDeviceId(Long userId, String deviceId);

    Optional<RefreshTokenEntity> findByTokenAndDeviceId(String token, String deviceId);

}
