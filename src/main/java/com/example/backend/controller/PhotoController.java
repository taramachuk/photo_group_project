package com.example.backend.controller;

import com.example.backend.dto.CreatePhotoDto;
import com.example.backend.dto.PhotoDto;
import com.example.backend.mapper.PhotoMapper;
import com.example.backend.model.Photo;
import com.example.backend.model.User;
import com.example.backend.service.PhotoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/photos")
@RestController
public class PhotoController {
    private final PhotoService photoService;
    private final PhotoMapper photoMapper;

    public PhotoController(PhotoService photoService, PhotoMapper photoMapper) {
        this.photoService = photoService;
        this.photoMapper = photoMapper;
    }

    @PostMapping
    public ResponseEntity<PhotoDto> createPhoto(@Valid @RequestBody CreatePhotoDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Photo createdPhoto = photoService.createPhoto(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(photoMapper.toDto(createdPhoto));
    }

    @GetMapping("/spot/{spotId}")
    public ResponseEntity<List<PhotoDto>> getPhotosBySpotId(@PathVariable Long spotId) {
        List<Photo> photos = photoService.getPhotosBySpotId(spotId);
        return ResponseEntity.ok(photoMapper.toDtoList(photos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoDto> getPhotoById(@PathVariable Long id) {
        return photoService.getPhotoById(id)
                .map(photo -> ResponseEntity.ok(photoMapper.toDto(photo)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        photoService.deletePhoto(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}

