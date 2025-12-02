package com.example.backend.controller;

import com.example.backend.dto.CreatePhotoDto;
import com.example.backend.dto.PhotoDto;
import com.example.backend.dto.UploadPhotoDto;
import com.example.backend.mapper.PhotoMapper;
import com.example.backend.model.Photo;
import com.example.backend.model.User;
import com.example.backend.service.PhotoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadPhoto(
            @RequestPart("image") MultipartFile file,
            @RequestPart("data") String dataJson
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        try {
            ObjectMapper mapper = new ObjectMapper();
            UploadPhotoDto dto = mapper.readValue(dataJson, UploadPhotoDto.class);

            Photo photo = photoService.savePicture(file, dto, currentUser);

            return ResponseEntity.ok(Map.of(
                    "message", "Upload OK",
                    "url", photo.getUrl(),
                    "id", photo.getId()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Upload failed", "error", e.getMessage()));        }
    }
}

