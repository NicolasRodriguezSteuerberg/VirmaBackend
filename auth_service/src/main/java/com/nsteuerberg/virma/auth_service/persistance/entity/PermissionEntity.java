package com.nsteuerberg.virma.auth_service.persistance.entity;

import com.nsteuerberg.virma.auth_service.util.constants.Permissions;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "permissions")
public class PermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    @Enumerated(value = EnumType.STRING)
    private Permissions permission;
}
