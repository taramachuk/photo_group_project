package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SpotTagId implements Serializable {

    @Column(name = "spot_id")
    private Long spotId;

    @Column(name = "tag_id")
    private Long tagId;
}
