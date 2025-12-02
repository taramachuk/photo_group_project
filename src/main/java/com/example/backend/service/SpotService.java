package com.example.backend.service;

import com.example.backend.dto.CreateSpotDto;
import com.example.backend.dto.UpdateSpotDto;
import com.example.backend.exception.UnauthorizedException;
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

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Spot> searchByTitle(String title) {
        List<Spot> spots = spotRepository.findByTitleContainingIgnoreCase(title);
        
        spots.forEach(spot -> {
            if (spot.getAuthor() != null) spot.getAuthor().getEmail();
            if (spot.getAddress() != null) spot.getAddress().getName();
        });
        return spots;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Spot> getSpotsInMapArea(
            BigDecimal minLatitude,
            BigDecimal maxLatitude,
            BigDecimal minLongitude,
            BigDecimal maxLongitude
    ) {
        List<Spot> spots = spotRepository.findByLatitudeBetweenAndLongitudeBetween(
                minLatitude, maxLatitude, minLongitude, maxLongitude
        );
        
        spots.forEach(spot -> {
            if (spot.getAuthor() != null) spot.getAuthor().getEmail();
            if (spot.getAddress() != null) spot.getAddress().getName();
        });
        return spots;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Spot> searchSpotsInMapAreaByTitle(
            BigDecimal minLatitude,
            BigDecimal maxLatitude,
            BigDecimal minLongitude,
            BigDecimal maxLongitude,
            String title
    ) {
        List<Spot> spots = spotRepository.findByLatitudeBetweenAndLongitudeBetweenAndTitleContainingIgnoreCase(
                minLatitude, maxLatitude, minLongitude, maxLongitude, title
        );
        
        spots.forEach(spot -> {
            if (spot.getAuthor() != null) spot.getAuthor().getEmail();
            if (spot.getAddress() != null) spot.getAddress().getName();
        });
        return spots;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Spot> searchSpotsInMapAreaByTag(
            BigDecimal minLatitude,
            BigDecimal maxLatitude,
            BigDecimal minLongitude,
            BigDecimal maxLongitude,
            String tagName
    ) {
        List<Spot> spots = spotRepository.findSpotsInAreaByTag(
                minLatitude, maxLatitude, minLongitude, maxLongitude, tagName
        );
        spots.forEach(spot -> {
            if (spot.getAuthor() != null) spot.getAuthor().getEmail();
            if (spot.getAddress() != null) spot.getAddress().getName();
        });
        return spots;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Spot> searchSpotsInMapAreaByTagAndTitle(
            BigDecimal minLatitude,
            BigDecimal maxLatitude,
            BigDecimal minLongitude,
            BigDecimal maxLongitude,
            String tagName,
            String title
    ) {
        List<Spot> spots = spotRepository.findSpotsInAreaByTagAndTitle(
                minLatitude, maxLatitude, minLongitude, maxLongitude, tagName, title
        );
        spots.forEach(spot -> {
            if (spot.getAuthor() != null) spot.getAuthor().getEmail();
            if (spot.getAddress() != null) spot.getAddress().getName();
        });
        return spots;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Spot> searchByTagName(String tagName) {
        List<Spot> spots = spotRepository.findByTagName(tagName);
        spots.forEach(spot -> {
            if (spot.getAuthor() != null) spot.getAuthor().getEmail();
            if (spot.getAddress() != null) spot.getAddress().getName();
        });
        return spots;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Spot> searchByTagNameAndTitle(String tagName, String title) {
        List<Spot> spots = spotRepository.findByTagNameAndTitleContainingIgnoreCase(tagName, title);
       
        spots.forEach(spot -> {
            if (spot.getAuthor() != null) spot.getAuthor().getEmail();
            if (spot.getAddress() != null) spot.getAddress().getName();
        });
        return spots;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Optional<Spot> getSpotById(Long id) {
        Optional<Spot> spotOptional = spotRepository.findById(id);
        
        if (spotOptional.isPresent()) {
            Spot spot = spotOptional.get();
            if (spot.getAuthor() != null) spot.getAuthor().getEmail();
            if (spot.getAddress() != null) spot.getAddress().getName();
        }
        
        return spotOptional;
    }

    @Transactional
    public Spot updateSpot(Long id, UpdateSpotDto dto, User currentUser) {
        Spot spot = spotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spot not found"));

        // Weryfikacja autora
        if (spot.getAuthor() == null || spot.getAuthor().getId() != currentUser.getId()) {
            throw new UnauthorizedException("You can only edit your own spots");
        }

        // Aktualizacja podstawowych pól
        if (dto.getTitle() != null) {
            spot.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            spot.setDescription(dto.getDescription());
        }
        if (dto.getLatitude() != null) {
            spot.setLatitude(dto.getLatitude());
        }
        if (dto.getLongitude() != null) {
            spot.setLongitude(dto.getLongitude());
        }
        if (dto.getCategoryId() != null) {
            spot.setCategoryId(dto.getCategoryId());
        }

        // Aktualizacja adresu (jeśli się zmienił)
        if (dto.getAddressName() != null || dto.getAddressCountry() != null || dto.getAddressRegion() != null) {
            String addressName = dto.getAddressName() != null ? dto.getAddressName() : spot.getAddress().getName();
            String addressCountry = dto.getAddressCountry() != null ? dto.getAddressCountry() : spot.getAddress().getCountry();
            String addressRegion = dto.getAddressRegion() != null ? dto.getAddressRegion() : spot.getAddress().getRegion();

            Address newAddress = getOrCreateAddress(addressName, addressCountry, addressRegion);
            spot.setAddress(newAddress);
        }

        spot = spotRepository.save(spot);

        // Aktualizacja tagów (jeśli są podane)
        if (dto.getTagNames() != null) {
            // Usunięcie starych tagów
            List<SpotTag> existingTags = spotTagRepository.findById_SpotId(spot.getId());
            spotTagRepository.deleteAll(existingTags);

            // Dodanie nowych tagów
            if (!dto.getTagNames().isEmpty()) {
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
        }

        return spot;
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

