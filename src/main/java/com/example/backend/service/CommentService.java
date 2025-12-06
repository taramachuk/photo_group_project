package com.example.backend.service;

import com.example.backend.dto.CreateCommentDto;
import com.example.backend.exception.UnauthorizedException;
import com.example.backend.model.Comment;
import com.example.backend.model.Photo;
import com.example.backend.model.Spot;
import com.example.backend.model.User;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.PhotoRepository;
import com.example.backend.repository.SpotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PhotoRepository photoRepository;
    private final SpotRepository spotRepository;

    public CommentService(
            CommentRepository commentRepository,
            PhotoRepository photoRepository,
            SpotRepository spotRepository) {
        this.commentRepository = commentRepository;
        this.photoRepository = photoRepository;
        this.spotRepository = spotRepository;
    }

    @Transactional
    public Comment createComment(CreateCommentDto dto, User author) {
        if ((dto.getPhotoId() == null && dto.getSpotId() == null) ||
            (dto.getPhotoId() != null && dto.getSpotId() != null)) {
            throw new IllegalArgumentException("Exactly one of photoId or spotId must be provided");
        }

        Comment.CommentBuilder builder = Comment.builder()
                .content(dto.getContent())
                .author(author)
                .createdAt(LocalDateTime.now());

        if (dto.getPhotoId() != null) {
            Photo photo = photoRepository.findById(dto.getPhotoId())
                    .orElseThrow(() -> new RuntimeException("Photo not found"));
            builder.photo(photo);
        } else {
            Spot spot = spotRepository.findById(dto.getSpotId())
                    .orElseThrow(() -> new RuntimeException("Spot not found"));
            builder.spot(spot);
        }

        return commentRepository.save(builder.build());
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByPhotoId(Long photoId) {
        List<Comment> comments = commentRepository.findByPhotoIdOrderByCreatedAtDesc(photoId);
        
        comments.forEach(comment -> {
            if (comment.getAuthor() != null) comment.getAuthor().getEmail();
            if (comment.getPhoto() != null) comment.getPhoto().getId();
            if (comment.getSpot() != null) comment.getSpot().getId();
        });
        
        return comments;
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsBySpotId(Long spotId) {
        List<Comment> comments = commentRepository.findBySpotIdOrderByCreatedAtDesc(spotId);
        
        comments.forEach(comment -> {
            if (comment.getAuthor() != null) comment.getAuthor().getEmail();
            if (comment.getPhoto() != null) comment.getPhoto().getId();
            if (comment.getSpot() != null) comment.getSpot().getId();
        });
        
        return comments;
    }

    @Transactional(readOnly = true)
    public Optional<Comment> getCommentById(Long id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            if (comment.getAuthor() != null) comment.getAuthor().getEmail();
            if (comment.getPhoto() != null) comment.getPhoto().getId();
            if (comment.getSpot() != null) comment.getSpot().getId();
        }
        
        return commentOptional;
    }

    @Transactional
    public void deleteComment(Long id, User currentUser) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (comment.getAuthor() == null || comment.getAuthor().getId() != currentUser.getId()) {
            throw new UnauthorizedException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }
}

