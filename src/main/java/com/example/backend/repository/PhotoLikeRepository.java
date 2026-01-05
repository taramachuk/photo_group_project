package com.example.backend.repository;

import com.example.backend.model.PhotoLike;
import com.example.backend.model.PhotoLikeId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoLikeRepository extends CrudRepository<PhotoLike, PhotoLikeId> {

    // Sprawdzenie czy użytkownik polubił konkretne zdjęcie
    Optional<PhotoLike> findById_UserIdAndId_PhotoId(Long userId, Long photoId);

    // Usunięcie like'a
    void deleteById_UserIdAndId_PhotoId(Long userId, Long photoId);

    // Liczenie likeów dla zdjęcia
    @Query("SELECT COUNT(pl) FROM PhotoLike pl WHERE pl.id.photoId = :photoId")
    Long countByPhotoId(@Param("photoId") Long photoId);
}
