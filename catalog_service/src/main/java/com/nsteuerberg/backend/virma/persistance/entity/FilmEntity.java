package com.nsteuerberg.backend.virma.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "film")
public class FilmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Column(name = "cover_url")
    private String coverUrl;
    @Column(name = "file_url", nullable = false)
    private String fileUrl;
    @Column(name = "duration_seconds", nullable = false)
    private Integer durationSeconds;
}
