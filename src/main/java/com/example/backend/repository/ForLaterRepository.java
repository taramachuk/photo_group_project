package com.example.backend.repository;

import com.example.backend.model.ForLater;
import com.example.backend.model.ForLaterId;
import com.example.backend.model.Spot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForLaterRepository extends CrudRepository<ForLater, ForLaterId> {

    // Pobranie wszystkich zapisanych spotów użytkownika
    List<ForLater> findById_UserId(Long userId);

    // Sprawdzenie czy użytkownik zapisał konkretny spot
    Optional<ForLater> findById_UserIdAndId_SpotId(Long userId, Long spotId);

    // Usunięcie zapisanego spota
    void deleteById_UserIdAndId_SpotId(Long userId, Long spotId);

    // Pobranie zapisanych na później spotów   
    @Query(value = "SELECT s.* FROM spots s " +
           "INNER JOIN for_later fl ON s.id = fl.spot_id " +
           "WHERE fl.user_id = :userId " +
           "ORDER BY fl.saved_at DESC",
           nativeQuery = true)
    List<Spot> findSpotsByUserId(@Param("userId") Long userId);
}

