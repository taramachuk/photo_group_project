package com.example.backend.controller;

import com.example.backend.dto.CreateSpotDto;
import com.example.backend.dto.SpotDto;
import com.example.backend.dto.UpdateSpotDto;
import com.example.backend.mapper.SpotMapper;
import com.example.backend.model.Spot;
import com.example.backend.model.User;
import com.example.backend.service.SpotService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/spots")
@RestController
public class SpotController {
    private final SpotService spotService;
    private final SpotMapper spotMapper;

    public SpotController(SpotService spotService, SpotMapper spotMapper) {
        this.spotService = spotService;
        this.spotMapper = spotMapper;
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<SpotDto>> searchByTitle(@RequestParam String title) {
        List<Spot> spots = spotService.searchByTitle(title);
        return ResponseEntity.ok(spotMapper.toDtoList(spots));
    }

    @GetMapping("/map/search")
    public ResponseEntity<List<SpotDto>> getSpotsInMapArea(
            @RequestParam BigDecimal minLat,
            @RequestParam BigDecimal maxLat,
            @RequestParam BigDecimal minLng,
            @RequestParam BigDecimal maxLng
    ) {
        List<Spot> spots = spotService.getSpotsInMapArea(minLat, maxLat, minLng, maxLng);
        return ResponseEntity.ok(spotMapper.toDtoList(spots));
    }

    @GetMapping("/map/search/title")
    public ResponseEntity<List<SpotDto>> searchSpotsInMapAreaByTitle(
            @RequestParam BigDecimal minLat,
            @RequestParam BigDecimal maxLat,
            @RequestParam BigDecimal minLng,
            @RequestParam BigDecimal maxLng,
            @RequestParam String title
    ) {
        List<Spot> spots = spotService.searchSpotsInMapAreaByTitle(minLat, maxLat, minLng, maxLng, title);
        return ResponseEntity.ok(spotMapper.toDtoList(spots));
    }

    @GetMapping("/map/search/tag")
    public ResponseEntity<List<SpotDto>> searchSpotsInMapAreaByTag(
            @RequestParam BigDecimal minLat,
            @RequestParam BigDecimal maxLat,
            @RequestParam BigDecimal minLng,
            @RequestParam BigDecimal maxLng,
            @RequestParam String tagName
    ) {
        List<Spot> spots = spotService.searchSpotsInMapAreaByTag(minLat, maxLat, minLng, maxLng, tagName);
        return ResponseEntity.ok(spotMapper.toDtoList(spots));
    }

    @GetMapping("/map/search/advanced")
    public ResponseEntity<List<SpotDto>> searchSpotsInMapAreaByTagAndTitle(
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
        return ResponseEntity.ok(spotMapper.toDtoList(spots));
    }

    @GetMapping("/tag")
    public ResponseEntity<List<SpotDto>> searchByTagName(@RequestParam String tagName) {
        List<Spot> spots = spotService.searchByTagName(tagName);
        return ResponseEntity.ok(spotMapper.toDtoList(spots));
    }

    @GetMapping("/tag/search")
    public ResponseEntity<List<SpotDto>> searchByTagNameAndTitle(
            @RequestParam String tagName,
            @RequestParam String title
    ) {
        List<Spot> spots = spotService.searchByTagNameAndTitle(tagName, title);
        return ResponseEntity.ok(spotMapper.toDtoList(spots));
    }


    // GET /spots/{id} do aktualizacji spotu
    @GetMapping("/{id}")
    public ResponseEntity<SpotDto> getSpotById(@PathVariable Long id) {
        return spotService.getSpotById(id)
                .map(spot -> ResponseEntity.ok(spotMapper.toDto(spot)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpotDto> updateSpot(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSpotDto dto
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Spot updatedSpot = spotService.updateSpot(id, dto, currentUser);
        return ResponseEntity.ok(spotMapper.toDto(updatedSpot));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SpotDto> patchSpot(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSpotDto dto
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Spot updatedSpot = spotService.updateSpot(id, dto, currentUser);
        return ResponseEntity.ok(spotMapper.toDto(updatedSpot));
    }

    @PostMapping
    public ResponseEntity<SpotDto> createSpot(@Valid @RequestBody CreateSpotDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Spot createdSpot = spotService.createSpot(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(spotMapper.toDto(createdSpot));
    }
}

