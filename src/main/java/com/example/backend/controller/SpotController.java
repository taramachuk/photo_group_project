package com.example.backend.controller;

import com.example.backend.model.Spot;
import com.example.backend.service.SpotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/spots")
@RestController
public class SpotController {
    private final SpotService spotService;

    public SpotController(SpotService spotService) {
        this.spotService = spotService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Spot>> searchByTitle(@RequestParam String title) {
        List<Spot> spots = spotService.searchByTitle(title);
        return ResponseEntity.ok(spots);
    }

    @GetMapping("/map")
    public ResponseEntity<List<Spot>> getSpotsInMapArea(
            @RequestParam BigDecimal minLat,
            @RequestParam BigDecimal maxLat,
            @RequestParam BigDecimal minLng,
            @RequestParam BigDecimal maxLng
    ) {
        List<Spot> spots = spotService.getSpotsInMapArea(minLat, maxLat, minLng, maxLng);
        return ResponseEntity.ok(spots);
    }

    @GetMapping("/map/search")
    public ResponseEntity<List<Spot>> searchSpotsInMapAreaByTitle(
            @RequestParam BigDecimal minLat,
            @RequestParam BigDecimal maxLat,
            @RequestParam BigDecimal minLng,
            @RequestParam BigDecimal maxLng,
            @RequestParam String title
    ) {
        List<Spot> spots = spotService.searchSpotsInMapAreaByTitle(minLat, maxLat, minLng, maxLng, title);
        return ResponseEntity.ok(spots);
    }

    @GetMapping("/map/tag")
    public ResponseEntity<List<Spot>> searchSpotsInMapAreaByTag(
            @RequestParam BigDecimal minLat,
            @RequestParam BigDecimal maxLat,
            @RequestParam BigDecimal minLng,
            @RequestParam BigDecimal maxLng,
            @RequestParam String tagName
    ) {
        List<Spot> spots = spotService.searchSpotsInMapAreaByTag(minLat, maxLat, minLng, maxLng, tagName);
        return ResponseEntity.ok(spots);
    }

    @GetMapping("/map/search/advanced")
    public ResponseEntity<List<Spot>> searchSpotsInMapAreaByTagAndTitle(
            @RequestParam BigDecimal minLat,
            @RequestParam BigDecimal maxLat,
            @RequestParam BigDecimal minLng,
            @RequestParam BigDecimal maxLng,
            @RequestParam String tagName,
            @RequestParam String title
    ) {
        List<Spot> spots = spotService.searchSpotsInMapAreaByTagAndTitle(
                minLat, maxLat, minLng, maxLng, tagName, title
        );
        return ResponseEntity.ok(spots);
    }

    @GetMapping("/tag")
    public ResponseEntity<List<Spot>> searchByTagName(@RequestParam String tagName) {
        List<Spot> spots = spotService.searchByTagName(tagName);
        return ResponseEntity.ok(spots);
    }

    @GetMapping("/tag/search")
    public ResponseEntity<List<Spot>> searchByTagNameAndTitle(
            @RequestParam String tagName,
            @RequestParam String title
    ) {
        List<Spot> spots = spotService.searchByTagNameAndTitle(tagName, title);
        return ResponseEntity.ok(spots);
    }
}

