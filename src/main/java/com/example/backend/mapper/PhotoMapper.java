package com.example.backend.mapper;

import com.example.backend.dto.AuthorDto;
import com.example.backend.dto.PhotoDto;
import com.example.backend.model.Photo;
import com.example.backend.model.User;
import com.example.backend.service.LikesService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhotoMapper {

    private LikesService likesService;

    public PhotoMapper(LikesService likesService) {
        this.likesService = likesService;
    }

    public PhotoDto toDto(Photo photo) {
        return toDto(photo, null);
    }

    public PhotoDto toDto(Photo photo, User currentUser) {
        if (photo == null) {
            return null;
        }

        Long likeCount = likesService.getPhotoLikeCount(photo.getId());
        Boolean isLiked = currentUser != null
                ? likesService.isPhotoLiked(photo.getId(), currentUser)
                : false;


        return PhotoDto.builder()
                .id(photo.getId())
                .url(photo.getUrl())
                .thumbnailUrl(photo.getThumbnailUrl())
                .caption(photo.getCaption())
                .createdAt(photo.getCreatedAt())
                .author(toAuthorDto(photo.getAuthor()))
                .spotId(photo.getSpot() != null ? photo.getSpot().getId() : null)
                .likeCount(likeCount)
                .isLiked(isLiked)
                .build();
    }

    public List<PhotoDto> toDtoList(List<Photo> photos) {
        return toDtoList(photos, null);
    }

    public List<PhotoDto> toDtoList(List<Photo> photos, User currentUser) {
        if (photos == null) {
            return null;
        }
        return photos.stream()
                .map(photo -> toDto(photo, currentUser))
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

