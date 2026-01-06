package com.example.backend.controller;

import com.example.backend.dto.LikeResponseDto;
import com.example.backend.model.User;
import com.example.backend.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // --- Spot Likes ---

    @PostMapping("/spots/{spotId}")
    public ResponseEntity<LikeResponseDto> likeSpot(@PathVariable Long spotId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        try {
            boolean alreadyLiked = likeService.isSpotLiked(spotId, currentUser);

            likeService.likeSpot(spotId, currentUser);

            Long likeCount = likeService.getSpotLikeCount(spotId);
            LikeResponseDto response = LikeResponseDto.builder()
                    .liked(true)
                    .likeCount(likeCount)
                    .spotId(spotId)
                    .build();

            if (alreadyLiked) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Spot not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            throw e;
        }
    }

    @DeleteMapping("/spots/{spotId}")
    public ResponseEntity<LikeResponseDto> unlikeSpot(@PathVariable Long spotId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        try {
            likeService.unlikeSpot(spotId, currentUser);
            Long likeCount = likeService.getSpotLikeCount(spotId);
            LikeResponseDto response = LikeResponseDto.builder()
                    .liked(false)
                    .likeCount(likeCount)
                    .spotId(spotId)
                    .build();
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            if (e.getMessage().equals("Spot is not liked")) {
                return ResponseEntity.notFound().build();
            }
            throw e;
        }
    }

    @GetMapping("/spots/{spotId}")
    public ResponseEntity<Long> getSpotLikeCount(@PathVariable Long spotId) {
        Long count = likeService.getSpotLikeCount(spotId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/spots/{spotId}/is-liked")
    public ResponseEntity<Boolean> isSpotLiked(@PathVariable Long spotId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        boolean isLiked = likeService.isSpotLiked(spotId, currentUser);
        return ResponseEntity.ok(isLiked);
    }

    // --- Photo Likes ---

    @PostMapping("/photos/{photoId}")
    public ResponseEntity<LikeResponseDto> likePhoto(@PathVariable Long photoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        try {
            boolean alreadyLiked = likeService.isPhotoLiked(photoId, currentUser);

            likeService.likePhoto(photoId, currentUser);

            Long likeCount = likeService.getPhotoLikeCount(photoId);
            LikeResponseDto response = LikeResponseDto.builder()
                    .liked(true)
                    .likeCount(likeCount)
                    .photoId(photoId)
                    .build();


            if (alreadyLiked) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Photo not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            throw e;
        }
    }

    @DeleteMapping("/photos/{photoId}")
    public ResponseEntity<LikeResponseDto> unlikePhoto(@PathVariable Long photoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        try {
            likeService.unlikePhoto(photoId, currentUser);
            Long likeCount = likeService.getPhotoLikeCount(photoId);
            LikeResponseDto response = LikeResponseDto.builder()
                    .liked(false)
                    .likeCount(likeCount)
                    .photoId(photoId)
                    .build();
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            if (e.getMessage().equals("Photo is not liked")) {
                return ResponseEntity.notFound().build();
            }
            throw e;
        }
    }

    @GetMapping("/photos/{photoId}")
    public ResponseEntity<Long> getPhotoLikeCount(@PathVariable Long photoId) {
        Long count = likeService.getPhotoLikeCount(photoId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/photos/{photoId}/is-liked")
    public ResponseEntity<Boolean> isPhotoLiked(@PathVariable Long photoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        boolean isLiked = likeService.isPhotoLiked(photoId, currentUser);
        return ResponseEntity.ok(isLiked);
    }
}