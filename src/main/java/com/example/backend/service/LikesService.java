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

@Service
public class LikesService {
    private final PhotoLikeRepository photoLikeRepository;
    private final SpotLikeRepository spotLikeRepository;
    private final PhotoRepository photoRepository;
    private final SpotRepository spotRepository;

    public LikesService(
            PhotoLikeRepository photoLikeRepository,
            SpotLikeRepository spotLikeRepository,
            PhotoRepository photoRepository,
            SpotRepository spotRepository
    ) {
        this.photoLikeRepository = photoLikeRepository;
        this.spotLikeRepository = spotLikeRepository;
        this.photoRepository = photoRepository;
        this.spotRepository = spotRepository;
    }

    @Transactional
    public boolean togglePhotoLike(Long photoId, User user) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        PhotoLikeId likeId = new PhotoLikeId(user.getId(), photoId);
        boolean isLiked = photoLikeRepository.existsByUserIdAndPhotoId(user.getId(), photoId);

        if (isLiked) {
            photoLikeRepository.deleteByUserIdAndPhotoId(user.getId(), photoId);
            return false; // Like usunięty
        } else {
            PhotoLike photoLike = PhotoLike.builder()
                    .id(likeId)
                    .user(user)
                    .photo(photo)
                    .build();
            photoLikeRepository.save(photoLike);
            return true; // Like dodany
        }
    }

    @Transactional
    public boolean toggleSpotLike(Long spotId, User user) {
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new RuntimeException("Spot not found"));

        SpotLikeId likeId = new SpotLikeId(user.getId(), spotId);
        boolean isLiked = spotLikeRepository.existsByUserIdAndSpotId(user.getId(), spotId);

        if (isLiked) {
            spotLikeRepository.deleteByUserIdAndSpotId(user.getId(), spotId);
            return false; // Like usunięty
        } else {
            SpotLike spotLike = SpotLike.builder()
                    .id(likeId)
                    .user(user)
                    .spot(spot)
                    .build();
            spotLikeRepository.save(spotLike);
            return true; // Like dodany
        }
    }

    @Transactional(readOnly = true)
    public boolean isPhotoLiked(Long photoId, User user) {
        return photoLikeRepository.existsByUserIdAndPhotoId(user.getId(), photoId);
    }

    @Transactional(readOnly = true)
    public boolean isSpotLiked(Long spotId, User user) {
        return spotLikeRepository.existsByUserIdAndSpotId(user.getId(), spotId);
    }

    @Transactional(readOnly = true)
    public long getPhotoLikeCount(Long photoId) {
        return photoLikeRepository.countByPhotoId(photoId);
    }

    @Transactional(readOnly = true)
    public long getSpotLikeCount(Long spotId) {
        return spotLikeRepository.countBySpotId(spotId);
    }
}

