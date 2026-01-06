package com.example.backend.service;

import com.example.backend.model.Photo;
import com.example.backend.model.PhotoLike;
import com.example.backend.model.PhotoLikeId;
import com.example.backend.model.Spot;
import com.example.backend.model.SpotLike;
import com.example.backend.model.SpotLikeId;
import com.example.backend.model.User;
import com.example.backend.repository.PhotoLikeRepository;
import com.example.backend.repository.PhotoRepository;
import com.example.backend.repository.SpotLikeRepository;
import com.example.backend.repository.SpotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LikeService {
    private final SpotLikeRepository spotLikeRepository;
    private final PhotoLikeRepository photoLikeRepository;
    private final SpotRepository spotRepository;
    private final PhotoRepository photoRepository;

    public LikeService(
            SpotLikeRepository spotLikeRepository,
            PhotoLikeRepository photoLikeRepository,
            SpotRepository spotRepository,
            PhotoRepository photoRepository
    ) {
        this.spotLikeRepository = spotLikeRepository;
        this.photoLikeRepository = photoLikeRepository;
        this.spotRepository = spotRepository;
        this.photoRepository = photoRepository;
    }

    // --- Spot Likes ---

    @Transactional(readOnly = true)
    public Long getSpotLikeCount(Long spotId) {
        return spotLikeRepository.countBySpotId(spotId);
    }

    @Transactional
    public void likeSpot(Long spotId, User user) {
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new RuntimeException("Spot not found"));

        Optional<SpotLike> existingLike = spotLikeRepository.findById_UserIdAndId_SpotId(
                user.getId(), spotId
        );

        if (existingLike.isPresent()) {
            return;
        }

        SpotLikeId spotLikeId = new SpotLikeId(user.getId(), spotId);
        SpotLike spotLike = SpotLike.builder()
                .id(spotLikeId)
                .user(user)
                .spot(spot)
                .build();

        spotLikeRepository.save(spotLike);
    }

    @Transactional
    public void unlikeSpot(Long spotId, User user) {
        Optional<SpotLike> spotLike = spotLikeRepository.findById_UserIdAndId_SpotId(
                user.getId(), spotId
        );

        if (spotLike.isEmpty()) {
            throw new RuntimeException("Spot is not liked");
        }

        spotLikeRepository.deleteById_UserIdAndId_SpotId(user.getId(), spotId);
    }

    @Transactional(readOnly = true)
    public boolean isSpotLiked(Long spotId, User user) {
        Optional<SpotLike> spotLike = spotLikeRepository.findById_UserIdAndId_SpotId(
                user.getId(), spotId
        );
        return spotLike.isPresent();
    }

    // --- Photo Likes ---

    @Transactional(readOnly = true)
    public Long getPhotoLikeCount(Long photoId) {
        return photoLikeRepository.countByPhotoId(photoId);
    }

    @Transactional
    public void likePhoto(Long photoId, User user) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        Optional<PhotoLike> existingLike = photoLikeRepository.findById_UserIdAndId_PhotoId(
                user.getId(), photoId
        );

        if (existingLike.isPresent()) {
            return;
        }

        PhotoLikeId photoLikeId = new PhotoLikeId(user.getId(), photoId);
        PhotoLike photoLike = PhotoLike.builder()
                .id(photoLikeId)
                .user(user)
                .photo(photo)
                .build();

        photoLikeRepository.save(photoLike);
    }

    @Transactional
    public void unlikePhoto(Long photoId, User user) {
        Optional<PhotoLike> photoLike = photoLikeRepository.findById_UserIdAndId_PhotoId(
                user.getId(), photoId
        );

        if (photoLike.isEmpty()) {
            throw new RuntimeException("Photo is not liked");
        }

        photoLikeRepository.deleteById_UserIdAndId_PhotoId(user.getId(), photoId);
    }

    @Transactional(readOnly = true)
    public boolean isPhotoLiked(Long photoId, User user) {
        Optional<PhotoLike> photoLike = photoLikeRepository.findById_UserIdAndId_PhotoId(
                user.getId(), photoId
        );
        return photoLike.isPresent();
    }
}