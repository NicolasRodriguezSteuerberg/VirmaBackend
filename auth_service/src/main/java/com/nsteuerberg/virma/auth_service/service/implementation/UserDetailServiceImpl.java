package com.nsteuerberg.virma.auth_service.service.implementation;

import com.nsteuerberg.virma.auth_service.persistance.entity.RoleEntity;
import com.nsteuerberg.virma.auth_service.persistance.entity.UserEntity;
import com.nsteuerberg.virma.auth_service.persistance.repository.IUserRepository;
import com.nsteuerberg.virma.auth_service.service.security.CustomUserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final IUserRepository userRepository;

    public UserDetailServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Usuario no encontrado")
        );

        Set<SimpleGrantedAuthority> authorities = getAuthorities(userEntity.getRoleEntitySet());

        return new CustomUserDetails(
                userEntity.getId(),
                username,
                userEntity.getPassword(),
                //userEntity.isEnabled(),
                authorities
        );
    }

    public Set<SimpleGrantedAuthority> getAuthorities(Set<RoleEntity> roleEntities) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        roleEntities.forEach(role ->
                authorities.add(
                        new SimpleGrantedAuthority("ROLE_".concat(role.getRole().name()))
                )
        );

        roleEntities.stream()
                .flatMap(roleEntity -> roleEntity.getPermissionEntitySet().stream())
                .forEach(permissionEntity ->
                        authorities.add(
                                new SimpleGrantedAuthority(permissionEntity.getPermission().name())
                        )
                );

        return authorities;
    }
}
