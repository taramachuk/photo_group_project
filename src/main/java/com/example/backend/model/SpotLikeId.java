package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SpotLikeId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "spot_id")
    private Long spotId;
}
