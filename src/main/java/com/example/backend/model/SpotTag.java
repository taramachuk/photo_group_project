package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "spot_tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotTag {

    @EmbeddedId
    private SpotTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("spotId")
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;
}
