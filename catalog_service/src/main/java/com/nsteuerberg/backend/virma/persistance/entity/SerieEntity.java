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
@Table(name = "serie")
public class SerieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    // ToDo hacer que no sea null
    @Column(name = "cover_url")
    private String coverUrl;

    @OneToMany(
            mappedBy = "serie",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @OrderBy("number ASC")
    @Builder.Default
    private List<SeasonEntity> seasonEntities = new ArrayList<>();

    public void addSeason(SeasonEntity season) {
        seasonEntities.add(season);
        season.setSerie(this);
    }

    public void removeSeason(SeasonEntity season) {
        seasonEntities.remove(season);
        season.setSerie(null);
    }
}
