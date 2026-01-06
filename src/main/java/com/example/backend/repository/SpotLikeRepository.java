package com.example.backend.repository;

import com.example.backend.model.SpotLike;
import com.example.backend.model.SpotLikeId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpotLikeRepository extends CrudRepository<SpotLike, SpotLikeId> {

    // Sprawdzenie czy użytkownik polubił konkretny spot
    Optional<SpotLike> findById_UserIdAndId_SpotId(Long userId, Long spotId);

    // Usunięcie like'a
    void deleteById_UserIdAndId_SpotId(Long userId, Long spotId);

    // Liczenie likeów dla spota
    @Query("SELECT COUNT(sl) FROM SpotLike sl WHERE sl.id.spotId = :spotId")
    Long countBySpotId(@Param("spotId") Long spotId);
}