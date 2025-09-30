package com.nsteuerberg.backend.virma.persistance.entity;

import com.nsteuerberg.backend.virma.utils.constants.ContentTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "content")
@Inheritance(strategy = InheritanceType.JOINED)
public class ContentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Column(name = "cover_url")
    private String coverUrl;
    @Column(name = "release_date")
    private LocalDate releaseDate;
    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentTypeEnum contentType;
}
