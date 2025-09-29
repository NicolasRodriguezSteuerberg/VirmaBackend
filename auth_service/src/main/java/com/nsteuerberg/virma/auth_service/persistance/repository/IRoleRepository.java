package com.nsteuerberg.virma.auth_service.persistance.repository;

import com.nsteuerberg.virma.auth_service.persistance.entity.RoleEntity;
import com.nsteuerberg.virma.auth_service.util.constants.Roles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends CrudRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findRoleEntityByRole(Roles role);
}
