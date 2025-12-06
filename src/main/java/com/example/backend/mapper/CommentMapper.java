package com.example.backend.mapper;

import com.example.backend.dto.AuthorDto;
import com.example.backend.dto.CommentDto;
import com.example.backend.model.Comment;
import com.example.backend.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .author(toAuthorDto(comment.getAuthor()))
                .photoId(comment.getPhoto() != null ? comment.getPhoto().getId() : null)
                .spotId(comment.getSpot() != null ? comment.getSpot().getId() : null)
                .build();
    }

    public List<CommentDto> toDtoList(List<Comment> comments) {
        if (comments == null) {
            return null;
        }
        return comments.stream()
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

