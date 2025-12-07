package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.service.LikesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/likes")
@RestController
public class LikesController {
    private final LikesService likesService;

    public LikesController(LikesService likesService) {
        this.likesService = likesService;
    }

    @PostMapping("/photos/{photoId}")
    public ResponseEntity<Map<String, Object>> togglePhotoLike(@PathVariable Long photoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        boolean isLiked = likesService.togglePhotoLike(photoId, currentUser);
        long likeCount = likesService.getPhotoLikeCount(photoId);

        return ResponseEntity.ok(Map.of(
                "isLiked", isLiked,
                "likeCount", likeCount
        ));
    }

    @PostMapping("/spots/{spotId}")
    public ResponseEntity<Map<String, Object>> toggleSpotLike(@PathVariable Long spotId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        boolean isLiked = likesService.toggleSpotLike(spotId, currentUser);
        long likeCount = likesService.getSpotLikeCount(spotId);

        return ResponseEntity.ok(Map.of(
                "isLiked", isLiked,
                "likeCount", likeCount
        ));
    }

    @GetMapping("/photos/{photoId}")
    public ResponseEntity<Map<String, Object>> getPhotoLikeStatus(@PathVariable Long photoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        boolean isLiked = likesService.isPhotoLiked(photoId, currentUser);
        long likeCount = likesService.getPhotoLikeCount(photoId);

        return ResponseEntity.ok(Map.of(
                "isLiked", isLiked,
                "likeCount", likeCount
        ));
    }

    @GetMapping("/spots/{spotId}")
    public ResponseEntity<Map<String, Object>> getSpotLikeStatus(@PathVariable Long spotId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        boolean isLiked = likesService.isSpotLiked(spotId, currentUser);
        long likeCount = likesService.getSpotLikeCount(spotId);

        return ResponseEntity.ok(Map.of(
                "isLiked", isLiked,
                "likeCount", likeCount
        ));
    }
}