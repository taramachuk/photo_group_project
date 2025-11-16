package com.example.backend.service;

import com.example.backend.model.Spot;
import com.example.backend.repository.SpotRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SpotService {
    private final SpotRepository spotRepository;

    public SpotService(SpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }

    public List<Spot> searchByTitle(String title) {
        return spotRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Spot> getSpotsInMapArea(
            BigDecimal minLatitude,
            BigDecimal maxLatitude,
            BigDecimal minLongitude,
            BigDecimal maxLongitude
    ) {
        return spotRepository.findByLatitudeBetweenAndLongitudeBetween(
                minLatitude, maxLatitude, minLongitude, maxLongitude
        );
    }

    public List<Spot> searchSpotsInMapAreaByTitle(
            BigDecimal minLatitude,
            BigDecimal maxLatitude,
            BigDecimal minLongitude,
            BigDecimal maxLongitude,
            String title
    ) {
        return spotRepository.findByLatitudeBetweenAndLongitudeBetweenAndTitleContainingIgnoreCase(
                minLatitude, maxLatitude, minLongitude, maxLongitude, title
        );
    }

    public List<Spot> searchSpotsInMapAreaByTag(
            BigDecimal minLatitude,
            BigDecimal maxLatitude,
            BigDecimal minLongitude,
            BigDecimal maxLongitude,
            String tagName
    ) {
        return spotRepository.findSpotsInAreaByTag(
                minLatitude, maxLatitude, minLongitude, maxLongitude, tagName
        );
    }

    public List<Spot> searchSpotsInMapAreaByTagAndTitle(
            BigDecimal minLatitude,
            BigDecimal maxLatitude,
            BigDecimal minLongitude,
            BigDecimal maxLongitude,
            String tagName,
            String title
    ) {
        return spotRepository.findSpotsInAreaByTagAndTitle(
                minLatitude, maxLatitude, minLongitude, maxLongitude, tagName, title
        );
    }

    public List<Spot> searchByTagName(String tagName) {
        return spotRepository.findByTagName(tagName);
    }

    public List<Spot> searchByTagNameAndTitle(String tagName, String title) {
        return spotRepository.findByTagNameAndTitleContainingIgnoreCase(tagName, title);
    }
}

