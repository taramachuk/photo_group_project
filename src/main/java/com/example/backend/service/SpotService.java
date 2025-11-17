package com.example.backend.service;

import com.example.backend.dto.CreateSpotDto;
import com.example.backend.model.Address;
import com.example.backend.model.Spot;
import com.example.backend.model.SpotTag;
import com.example.backend.model.SpotTagId;
import com.example.backend.model.Tag;
import com.example.backend.model.User;
import com.example.backend.repository.AddressRepository;
import com.example.backend.repository.SpotRepository;
import com.example.backend.repository.SpotTagRepository;
import com.example.backend.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SpotService {
    private final SpotRepository spotRepository;
    private final AddressRepository addressRepository;
    private final TagRepository tagRepository;
    private final SpotTagRepository spotTagRepository;

    public SpotService(
            SpotRepository spotRepository,
            AddressRepository addressRepository,
            TagRepository tagRepository,
            SpotTagRepository spotTagRepository
    ) {
        this.spotRepository = spotRepository;
        this.addressRepository = addressRepository;
        this.tagRepository = tagRepository;
        this.spotTagRepository = spotTagRepository;
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

    @Transactional
    public Spot createSpot(CreateSpotDto dto, User author) {
       
        Address address = getOrCreateAddress(
                dto.getAddressName(),
                dto.getAddressCountry(),
                dto.getAddressRegion()
        );

        Spot spot = Spot.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .categoryId(dto.getCategoryId())
                .author(author)
                .address(address)
                .createdAt(LocalDateTime.now())
                .build();

        spot = spotRepository.save(spot);

        // Obsługa tagów
        if (dto.getTagNames() != null && !dto.getTagNames().isEmpty()) {
            for (String tagName : dto.getTagNames()) {
                if (tagName != null && !tagName.trim().isEmpty()) {
                    String normalizedTagName = tagName.trim().toLowerCase();
                    Tag tag = getOrCreateTag(normalizedTagName);
                    
                    SpotTagId spotTagId = new SpotTagId(spot.getId(), tag.getId());
                    SpotTag spotTag = SpotTag.builder()
                            .id(spotTagId)
                            .spot(spot)
                            .tag(tag)
                            .build();
                    
                    spotTagRepository.save(spotTag);
                }
            }
        }

        return spot;
    }

    private Address getOrCreateAddress(String name, String country, String region) {
       
        Optional<Address> existingAddress;
        
        if (region != null && !region.trim().isEmpty()) {
            existingAddress = addressRepository.findByNameAndCountryAndRegion(name, country, region);
        } else {
            existingAddress = addressRepository.findByNameAndCountry(name, country);
        }

        if (existingAddress.isPresent()) {
            return existingAddress.get();
        }

        Address newAddress = new Address();
        newAddress.setName(name);
        newAddress.setCountry(country);
        newAddress.setRegion(region);

        return addressRepository.save(newAddress);
    }

    private Tag getOrCreateTag(String tagName) {
        Optional<Tag> existingTag = tagRepository.findByName(tagName);

        if (existingTag.isPresent()) {
            return existingTag.get();
        }

        Tag newTag = new Tag();
        newTag.setName(tagName);

        return tagRepository.save(newTag);
    }
}

