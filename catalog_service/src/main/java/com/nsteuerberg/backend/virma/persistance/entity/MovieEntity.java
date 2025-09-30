package com.nsteuerberg.backend.virma.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "movies")
public class MovieEntity extends ContentEntity{
    @Column(name = "duration_seconds", nullable = false)
    private Integer durationSeconds;
    @Column(name = "file_url")
    private String fileUrl;
}
