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
@Table(
    name = "season",
    uniqueConstraints = @UniqueConstraint(columnNames = {"serie_id", "number"})
)
public class SeasonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // no puede ser nulo
    @Column(nullable = false)
    private Integer number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "serie_id",
            nullable = false
    )
    private SerieEntity serie;

    @OneToMany(
            mappedBy = "season",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("number ASC")
    @Builder.Default // para que lo agregue al crear una temporada
    private List<EpisodeEntity> episodeEntities = new ArrayList<>();

    public void addEpisode(EpisodeEntity episode) {
        episodeEntities.add(episode);
        episode.setSeason(this);
    }

    public void removeEpisode(EpisodeEntity episode) {
        episodeEntities.remove(episode);
        episode.setSeason(null);
    }
}
