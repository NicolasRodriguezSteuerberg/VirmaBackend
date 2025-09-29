package com.nsteuerberg.virma.auth_service.persistance.repository;

import com.nsteuerberg.virma.auth_service.persistance.entity.PermissionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPermissionEntity extends CrudRepository<PermissionEntity, Integer> {
}
