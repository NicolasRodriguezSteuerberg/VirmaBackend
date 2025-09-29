package com.nsteuerberg.virma.auth_service.persistance.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "refresh_tokens")
@CompoundIndex(def = "{'userId': 1, 'deviceId': 1}", unique = true)
public class RefreshTokenEntity {
    @Id
    private String id;
    private Long userId;
    private String deviceId;
    // hash token
    private String token;
    @Indexed(expireAfter = "0s")
    private Instant expiredDate;
}

