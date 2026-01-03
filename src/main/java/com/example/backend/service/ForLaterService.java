package com.example.backend.service;

import com.example.backend.model.ForLater;
import com.example.backend.model.ForLaterId;
import com.example.backend.model.Spot;
import com.example.backend.model.User;
import com.example.backend.repository.ForLaterRepository;
import com.example.backend.repository.SpotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ForLaterService {
    private final ForLaterRepository forLaterRepository;
    private final SpotRepository spotRepository;

    public ForLaterService(
            ForLaterRepository forLaterRepository,
            SpotRepository spotRepository
    ) {
        this.forLaterRepository = forLaterRepository;
        this.spotRepository = spotRepository;
    }

    @Transactional(readOnly = true)
    public List<Spot> getSavedSpots(User user) {
        List<Spot> spots = forLaterRepository.findSpotsByUserId(user.getId());
        
        // Lazy loading dla relacji
        spots.forEach(spot -> {
            if (spot.getAuthor() != null) spot.getAuthor().getEmail();
            if (spot.getAddress() != null) spot.getAddress().getName();
        });
        
        return spots;
    }

    @Transactional
    public void saveSpotForLater(Long spotId, User user) {
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new RuntimeException("Spot not found"));

        Optional<ForLater> existingForLater = forLaterRepository.findById_UserIdAndId_SpotId(
                user.getId(), spotId
        );

        if (existingForLater.isPresent()) {
            return;
        }

        ForLaterId forLaterId = new ForLaterId(user.getId(), spotId);
        ForLater forLater = ForLater.builder()
                .id(forLaterId)
                .user(user)
                .spot(spot)
                .savedAt(LocalDateTime.now())
                .build();

        forLaterRepository.save(forLater);
    }

    @Transactional
    public void removeSpotFromForLater(Long spotId, User user) {
        Optional<ForLater> forLater = forLaterRepository.findById_UserIdAndId_SpotId(
                user.getId(), spotId
        );

        if (forLater.isEmpty()) {
            throw new RuntimeException("Spot is not saved for later");
        }

        forLaterRepository.deleteById_UserIdAndId_SpotId(user.getId(), spotId);
    }

    @Transactional(readOnly = true)
    public boolean isSpotSavedForLater(Long spotId, User user) {
        Optional<ForLater> forLater = forLaterRepository.findById_UserIdAndId_SpotId(
                user.getId(), spotId
        );
        return forLater.isPresent();
    }
}

