package com.example.backend.repository;

import com.example.backend.model.ForLater;
import com.example.backend.model.ForLaterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ForLaterRepository extends JpaRepository<ForLater, ForLaterId> {


    @Query("SELECT fl FROM ForLater fl JOIN FETCH fl.spot WHERE fl.user.id = :userId ORDER BY fl.savedAt DESC")
    List<ForLater> findAllByUserIdOrderBySavedAtDesc(Long userId);


    @Query("SELECT fl.id.spotId FROM ForLater fl WHERE fl.id.userId = :userId")
    Set<Long> findSavedSpotIdsByUserId(Long userId);
}