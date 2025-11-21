package com.example.backend.service;

import com.example.backend.dto.CommentResponseDto;
import com.example.backend.dto.CreateCommentDto;
import com.example.backend.model.Comment;
import com.example.backend.model.Photo;
import com.example.backend.model.Spot;
import com.example.backend.model.User;
import com.example.backend.repository.CommentRepository;
// import com.example.backend.repository.PhotoRepository;
import com.example.backend.repository.SpotRepository;
import com.example.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final SpotRepository spotRepository;
    private final UserRepository userRepository;
    // private final PhotoRepository photoRepository;

    public CommentService(
            CommentRepository commentRepository,
            SpotRepository spotRepository,
            UserRepository userRepository
            // PhotoRepository photoRepository
    ) {
        this.commentRepository = commentRepository;
        this.spotRepository = spotRepository;
        this.userRepository = userRepository;
        // this.photoRepository = photoRepository;
    }

    @Transactional
    public void addComment(CreateCommentDto dto, String userEmail) {

        User author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));


        Comment.CommentBuilder commentBuilder = Comment.builder()
                .content(dto.getContent())
                .author(author);


        if (dto.getSpotId() != null) {
            Spot spot = spotRepository.findById(dto.getSpotId())
                    .orElseThrow(() -> new RuntimeException("Spot not found with id: " + dto.getSpotId()));
            commentBuilder.spot(spot);
        }
        else if (dto.getPhotoId() != null) {
            // Logika dla zdjęcia
            /*
            Photo photo = photoRepository.findById(dto.getPhotoId())
                    .orElseThrow(() -> new RuntimeException("Photo not found with id: " + dto.getPhotoId()));
            commentBuilder.photo(photo);
            */
            throw new RuntimeException("Komentowanie zdjęć nie jest jeszcze zaimplementowane");
        } else {
            throw new RuntimeException("Komentarz musi być przypisany do Spota lub Zdjęcia");
        }


        commentRepository.save(commentBuilder.build());
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsForSpot(Long spotId) {

        List<Comment> comments = commentRepository.findBySpotIdOrderByCreatedAtDesc(spotId);


        return comments.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }


    private CommentResponseDto mapToResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorName(comment.getAuthor().getUsername())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}