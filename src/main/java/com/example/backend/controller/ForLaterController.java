package com.example.backend.controller;

import com.example.backend.dto.SpotDto;
import com.example.backend.mapper.SpotMapper;
import com.example.backend.model.Spot;
import com.example.backend.model.User;
import com.example.backend.service.ForLaterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/for-later")
@RestController
public class ForLaterController {
    private final ForLaterService forLaterService;
    private final SpotMapper spotMapper;

    public ForLaterController(ForLaterService forLaterService, SpotMapper spotMapper) {
        this.forLaterService = forLaterService;
        this.spotMapper = spotMapper;
    }
    // Pobranie zapisanych na później spotów
    @GetMapping
    public ResponseEntity<List<SpotDto>> getSavedSpots() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<Spot> spots = forLaterService.getSavedSpots(currentUser);
        return ResponseEntity.ok(spotMapper.toDtoList(spots));
    }

    // Zapisanie spotu na później
    @PostMapping("/{spotId}")
    public ResponseEntity<?> saveSpotForLater(@PathVariable Long spotId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        try {
            boolean alreadySaved = forLaterService.isSpotSavedForLater(spotId, currentUser);
            
            forLaterService.saveSpotForLater(spotId, currentUser);
            
            if (alreadySaved) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Spot not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            throw e;
        }
    }

    // Usunięcie spotu z listy zapisanych na później
    @DeleteMapping("/{spotId}")
    public ResponseEntity<Void> removeSpotFromForLater(@PathVariable Long spotId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        try {
            forLaterService.removeSpotFromForLater(spotId, currentUser);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Spot is not saved for later")) {
                return ResponseEntity.notFound().build();
            }
            throw e;
        }
    }

    // Sprawdzenie czy spot jest zapisany na później
    @GetMapping("/{spotId}")
    public ResponseEntity<Boolean> isSpotSavedForLater(@PathVariable Long spotId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        boolean isSaved = forLaterService.isSpotSavedForLater(spotId, currentUser);
        return ResponseEntity.ok(isSaved);
    }
}

