package com.example.backend.controller;

import com.example.backend.dto.LoginUserDto;
import com.example.backend.dto.SpotDto;
import com.example.backend.mapper.SpotMapper;
import com.example.backend.model.Spot;
import com.example.backend.model.User;
import com.example.backend.service.SpotService;
import com.example.backend.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;
    private final SpotService spotService;
    private final SpotMapper spotMapper;
    private final Logger logger = LogManager.getLogger(UserController.class);

    public UserController(UserService userService, SpotService spotService, SpotMapper spotMapper) {

        this.userService = userService;
        this.spotService = spotService;
        this.spotMapper = spotMapper;
    }
    @GetMapping("/me/saved")
    public ResponseEntity<List<SpotDto>> getMySavedSpots() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();


        List<Spot> savedSpots = spotService.getSavedSpots(currentUser);


        List<SpotDto> dtos = spotMapper.toDtoList(savedSpots);


        spotService.setUserInteractionStatus(dtos, currentUser);

        return ResponseEntity.ok(dtos);
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

    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }

}
