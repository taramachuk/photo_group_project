package com.example.backend.repository;

import com.example.backend.model.PhotoLike;
import com.example.backend.model.PhotoLikeId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoLikeRepository extends CrudRepository<PhotoLike, PhotoLikeId> {
    Optional<PhotoLike> findByUserIdAndPhotoId(Long userId, Long photoId);

    boolean existsByUserIdAndPhotoId(Long userId, Long photoId);

    long countByPhotoId(Long photoId);

    void deleteByUserIdAndPhotoId(Long userId, Long photoId);
}