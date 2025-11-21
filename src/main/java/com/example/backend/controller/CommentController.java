package com.example.backend.controller;

import com.example.backend.dto.CommentResponseDto;
import com.example.backend.dto.CreateCommentDto;
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

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<String> addComment(@Valid @RequestBody CreateCommentDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        commentService.addComment(dto, currentUser.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body("Comment added successfully");
    }

    @GetMapping("/spot/{spotId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsForSpot(@PathVariable Long spotId) {
        List<CommentResponseDto> comments = commentService.getCommentsForSpot(spotId);
        return ResponseEntity.ok(comments);
    }
    /*
    @GetMapping("/photo/{photoId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsForPhoto(@PathVariable Long photoId) {
        List<CommentResponseDto> comments = commentService.getCommentsForPhoto(photoId);
        return ResponseEntity.ok(comments);
    }

     */
}