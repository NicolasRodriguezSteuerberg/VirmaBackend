package com.nsteuerberg.backend.virma.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
// El numbero del episodio tiene que ser unico de la temporada
@Table(
    name = "episode",
    uniqueConstraints = @UniqueConstraint(columnNames = {"season_id", "number"})
)
public class EpisodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer number;
    // ToDo agregar el titulo del episodio???'
    @Column(name = "duration_seconds")
    private Integer durationSeconds;
    // no se puede repetir el mismo video en episodios diferentes
    // no puede ser nulo
    @Column(
            nullable = false,
            unique = true,
            name = "file_url"
    )
    private String fileUrl;
    @Column(name = "cover_url")
    private String coverUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "season_id",
            nullable = false
    )
    private SeasonEntity season;
}
