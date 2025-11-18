package com.example.backend.mapper;

import com.example.backend.dto.AddressDto;
import com.example.backend.dto.SpotDto;
import com.example.backend.dto.UserDto;
import com.example.backend.model.Address;
import com.example.backend.model.Spot;
import com.example.backend.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpotMapper {

    public SpotDto toDto(Spot spot) {
        if (spot == null) {
            return null;
        }

        return SpotDto.builder()
                .id(spot.getId())
                .title(spot.getTitle())
                .description(spot.getDescription())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .createdAt(spot.getCreatedAt())
                .categoryId(spot.getCategoryId())
                .author(toUserDto(spot.getAuthor()))
                .address(toAddressDto(spot.getAddress()))
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

    private UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
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

