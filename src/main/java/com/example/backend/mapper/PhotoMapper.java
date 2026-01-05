package com.example.backend.mapper;

import com.example.backend.dto.AuthorDto;
import com.example.backend.dto.PhotoDto;
import com.example.backend.model.Photo;
import com.example.backend.model.User;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.PhotoLikeRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhotoMapper {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PhotoLikeRepository photoLikeRepository;

    public PhotoMapper(CommentRepository commentRepository, CommentMapper commentMapper, PhotoLikeRepository photoLikeRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.photoLikeRepository = photoLikeRepository;
    }

    public PhotoDto toDto(Photo photo) {
        if (photo == null) {
            return null;
        }

        List<com.example.backend.dto.CommentDto> comments = null;
        if (photo.getId() != null) {
            List<com.example.backend.model.Comment> photoComments = commentRepository.findByPhotoIdOrderByCreatedAtDesc(photo.getId());
            comments = commentMapper.toDtoList(photoComments);
        }

        Integer likes = null;
        if (photo.getId() != null) {
            Long likeCount = photoLikeRepository.countByPhotoId(photo.getId());
            likes = likeCount != null ? likeCount.intValue() : 0;
        }

        return PhotoDto.builder()
                .id(photo.getId())
                .url(photo.getUrl())
                .thumbnailUrl(photo.getThumbnailUrl())
                .caption(photo.getCaption())
                .createdAt(photo.getCreatedAt())
                .author(toAuthorDto(photo.getAuthor()))
                .spotId(photo.getSpot() != null ? photo.getSpot().getId() : null)
                .comments(comments)
                .likes(likes)
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

