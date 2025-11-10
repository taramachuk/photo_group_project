package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "for_later")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForLater {

    @EmbeddedId
    private ForLaterId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("spotId")
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;

    @Column(name = "saved_at", nullable = false, updatable = false)
    private LocalDateTime savedAt;
}
