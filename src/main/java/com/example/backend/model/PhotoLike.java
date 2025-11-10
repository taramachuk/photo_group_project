package com.example.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "photo_likes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoLike {

    @EmbeddedId
    private PhotoLikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("photoId")
    @JoinColumn(name = "photo_id", nullable = false)
    private Photo photo;
}
