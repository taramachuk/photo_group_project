package com.example.backend.controller;

import com.example.backend.dto.SpotDto;
import com.example.backend.dto.PhotoDto;
import com.example.backend.model.Spot;
import com.example.backend.model.Photo;
import com.example.backend.repository.SpotRepository;
import com.example.backend.repository.PhotoRepository;
import com.example.backend.dto.LoginUserDto;
import com.example.backend.model.User;
import com.example.backend.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import com.example.backend.dto.UpdateUserDto;
import com.example.backend.repository.UserRepository;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;
    private final SpotRepository spotRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final Logger logger = LogManager.getLogger(UserController.class);

    public UserController(UserService userService, SpotRepository spotRepository, PhotoRepository photoRepository, UserRepository userRepository) {
        this.userService = userService;
        this.spotRepository = spotRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<LoginUserDto> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        logger.info(currentUser.getEmail());

        LoginUserDto dto = new LoginUserDto();
        dto.setEmail(currentUser.getEmail());
        dto.setPassword(currentUser.getPassword());

        return ResponseEntity.ok(dto);
    }
    @GetMapping("/me/spots")
    public ResponseEntity<List<SpotDto>> getMySpots() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();


        List<Spot> spots = spotRepository.findByAuthor(currentUser);

        if (spots == null) return ResponseEntity.ok(Collections.emptyList());

        List<SpotDto> dtos = new ArrayList<>();
        for (Spot spot : spots) {
            SpotDto dto = new SpotDto();
            dto.setId(spot.getId());
            dto.setTitle(spot.getTitle());
            // do dopisania
            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/me/photos")
    public ResponseEntity<List<PhotoDto>> getMyPhotos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        List<Photo> photos = photoRepository.findByAuthor(currentUser);

        if (photos == null) return ResponseEntity.ok(Collections.emptyList());

        List<PhotoDto> dtos = new ArrayList<>();
        for (Photo photo : photos) {
            PhotoDto dto = new PhotoDto();
            dto.setId(photo.getId());
            dto.setUrl(photo.getUrl());
            dto.setThumbnailUrl(photo.getThumbnailUrl());

            if (photo.getSpot() != null) {
                dto.setSpotId(photo.getSpot().getId());
            }

            dtos.add(dto);
        }

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/me/saved")
    public ResponseEntity<List<SpotDto>> getSavedSpots() {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateCurrentUser(@RequestBody UpdateUserDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();


        if (dto.getDisplayName() != null) currentUser.setDisplayName(dto.getDisplayName());
        if (dto.getBio() != null) currentUser.setBio(dto.getBio());
        if (dto.getAvatarUrl() != null) currentUser.setAvatarUrl(dto.getAvatarUrl());


        User savedUser = userRepository.save(currentUser);

        return ResponseEntity.ok(savedUser);
    }

}
