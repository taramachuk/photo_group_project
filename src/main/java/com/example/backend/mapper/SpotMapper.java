package com.example.backend.mapper;

import com.example.backend.dto.AddressDto;
import com.example.backend.dto.AuthorDto;
import com.example.backend.dto.SpotDto;
import com.example.backend.model.Address;
import com.example.backend.model.Spot;
import com.example.backend.model.SpotTag;
import com.example.backend.model.User;
import com.example.backend.repository.SpotTagRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpotMapper {

    private final SpotTagRepository spotTagRepository;

    public SpotMapper(SpotTagRepository spotTagRepository) {
        this.spotTagRepository = spotTagRepository;
    }

    public SpotDto toDto(Spot spot) {
        if (spot == null) {
            return null;
        }

        List<String> tagNames = null;
        if (spot.getId() != null) {
            List<SpotTag> spotTags = spotTagRepository.findById_SpotId(spot.getId());
            tagNames = spotTags.stream()
                    .map(spotTag -> spotTag.getTag().getName())
                    .collect(Collectors.toList());
        }

        return SpotDto.builder()
                .id(spot.getId())
                .title(spot.getTitle())
                .description(spot.getDescription())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .createdAt(spot.getCreatedAt())
                .categoryId(spot.getCategoryId())
                .author(toAuthorDto(spot.getAuthor()))
                .address(toAddressDto(spot.getAddress()))
                .tagNames(tagNames)
                .build();
    }

    public List<SpotDto> toDtoList(List<Spot> spots) {
        if (spots == null) {
            return null;
        }
        return spots.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

  
    private AuthorDto toAuthorDto(User user) {
        if (user == null) {
            return null;
        }

        return AuthorDto.builder()
                .id(user.getId())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .build();
    }

    private AddressDto toAddressDto(Address address) {
        if (address == null) {
            return null;
        }

        return AddressDto.builder()
                .id(address.getId())
                .name(address.getName())
                .country(address.getCountry())
                .region(address.getRegion())
                .build();
    }
}

