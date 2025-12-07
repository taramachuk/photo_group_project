package com.example.backend.repository;

import com.example.backend.model.SpotLike;
import com.example.backend.model.SpotLikeId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpotLikeRepository extends CrudRepository<SpotLike, SpotLikeId> {
    Optional<SpotLike> findByUserIdAndSpotId(Long userId, Long spotId);

    boolean existsByUserIdAndSpotId(Long userId, Long spotId);

    long countBySpotId(Long spotId);

    void deleteByUserIdAndSpotId(Long userId, Long spotId);
}