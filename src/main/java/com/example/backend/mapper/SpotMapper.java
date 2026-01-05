package com.example.backend.mapper;

import com.example.backend.dto.AddressDto;
import com.example.backend.dto.AuthorDto;
import com.example.backend.dto.SpotDto;
import com.example.backend.model.Address;
import com.example.backend.model.Spot;
import com.example.backend.model.SpotTag;
import com.example.backend.model.User;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.SpotLikeRepository;
import com.example.backend.repository.SpotTagRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpotMapper {

    private final SpotTagRepository spotTagRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final SpotLikeRepository spotLikeRepository;

    public SpotMapper(SpotTagRepository spotTagRepository, CommentRepository commentRepository, CommentMapper commentMapper, SpotLikeRepository spotLikeRepository) {
        this.spotTagRepository = spotTagRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.spotLikeRepository = spotLikeRepository;
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

        List<com.example.backend.dto.CommentDto> comments = null;
        if (spot.getId() != null) {
            List<com.example.backend.model.Comment> spotComments = commentRepository.findBySpotIdOrderByCreatedAtDesc(spot.getId());
            comments = commentMapper.toDtoList(spotComments);
        }

        Integer likes = null;
        if (spot.getId() != null) {
            Long likeCount = spotLikeRepository.countBySpotId(spot.getId());
            likes = likeCount != null ? likeCount.intValue() : 0;
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
                .comments(comments)
                .likes(likes)
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

