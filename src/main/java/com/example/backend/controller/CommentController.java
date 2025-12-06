package com.example.backend.controller;

import com.example.backend.dto.CommentDto;
import com.example.backend.dto.CreateCommentDto;
import com.example.backend.mapper.CommentMapper;
import com.example.backend.model.Comment;
import com.example.backend.model.User;
import com.example.backend.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/comments")
@RestController
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CreateCommentDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Comment createdComment = commentService.createComment(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.toDto(createdComment));
    }

    @GetMapping("/photo/{photoId}")
    public ResponseEntity<List<CommentDto>> getCommentsByPhotoId(@PathVariable Long photoId) {
        List<Comment> comments = commentService.getCommentsByPhotoId(photoId);
        return ResponseEntity.ok(commentMapper.toDtoList(comments));
    }

    @GetMapping("/spot/{spotId}")
    public ResponseEntity<List<CommentDto>> getCommentsBySpotId(@PathVariable Long spotId) {
        List<Comment> comments = commentService.getCommentsBySpotId(spotId);
        return ResponseEntity.ok(commentMapper.toDtoList(comments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id)
                .map(comment -> ResponseEntity.ok(commentMapper.toDto(comment)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        commentService.deleteComment(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}

