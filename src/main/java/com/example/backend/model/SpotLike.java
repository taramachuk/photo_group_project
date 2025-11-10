package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "spot_likes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotLike {

    @EmbeddedId
    private SpotLikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("spotId")
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;
}
