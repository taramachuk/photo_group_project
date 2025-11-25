package com.example.backend.mapper;

import com.example.backend.dto.AuthorDto;
import com.example.backend.dto.PhotoDto;
import com.example.backend.model.Photo;
import com.example.backend.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhotoMapper {

    public PhotoDto toDto(Photo photo) {
        if (photo == null) {
            return null;
        }

        return PhotoDto.builder()
                .id(photo.getId())
                .url(photo.getUrl())
                .thumbnailUrl(photo.getThumbnailUrl())
                .caption(photo.getCaption())
                .createdAt(photo.getCreatedAt())
                .author(toAuthorDto(photo.getAuthor()))
                .spotId(photo.getSpot() != null ? photo.getSpot().getId() : null)
                .build();
    }

    public List<PhotoDto> toDtoList(List<Photo> photos) {
        if (photos == null) {
            return null;
        }
        return photos.stream()
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
}

