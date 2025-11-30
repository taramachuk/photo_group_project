package com.example.backend.service;

import com.example.backend.dto.CreatePhotoDto;
import com.example.backend.exception.UnauthorizedException;
import com.example.backend.model.Photo;
import com.example.backend.model.Spot;
import com.example.backend.model.User;
import com.example.backend.repository.PhotoRepository;
import com.example.backend.repository.SpotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final SpotRepository spotRepository;

    public PhotoService(
            PhotoRepository photoRepository,
            SpotRepository spotRepository
    ) {
        this.photoRepository = photoRepository;
        this.spotRepository = spotRepository;
    }

    @Transactional
    public Photo createPhoto(CreatePhotoDto dto, User author) {
        Spot spot = spotRepository.findById(dto.getSpotId())
                .orElseThrow(() -> new RuntimeException("Spot not found"));

        Photo photo = Photo.builder()
                .url(dto.getUrl())
                .thumbnailUrl(dto.getThumbnailUrl())
                .caption(dto.getCaption())
                .author(author)
                .spot(spot)
                .createdAt(LocalDateTime.now())
                .build();

        photo = photoRepository.save(photo);

        return photo;
    }

    @Transactional(readOnly = true)
    public List<Photo> getPhotosBySpotId(Long spotId) {
        List<Photo> photos = photoRepository.findBySpotIdOrderByCreatedAtDesc(spotId);
        
        photos.forEach(photo -> {
            if (photo.getAuthor() != null) photo.getAuthor().getEmail();
            if (photo.getSpot() != null) photo.getSpot().getId();
        });
        
        return photos;
    }

    @Transactional(readOnly = true)
    public Optional<Photo> getPhotoById(Long id) {
        Optional<Photo> photoOptional = photoRepository.findById(id);
        
        if (photoOptional.isPresent()) {
            Photo photo = photoOptional.get();
            if (photo.getAuthor() != null) photo.getAuthor().getEmail();
            if (photo.getSpot() != null) photo.getSpot().getId();
        }
        
        return photoOptional;
    }

    @Transactional
    public void deletePhoto(Long id, User currentUser) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        if (photo.getAuthor() == null || photo.getAuthor().getId() != currentUser.getId()) {
            throw new UnauthorizedException("You can only delete your own photos");
        }

        photoRepository.delete(photo);
    }
}

