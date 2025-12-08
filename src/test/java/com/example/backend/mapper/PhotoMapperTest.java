package com.example.backend.mapper;

import com.example.backend.dto.PhotoDto;
import com.example.backend.model.Photo;
import com.example.backend.model.Spot;
import com.example.backend.model.User;
import com.example.backend.service.LikesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhotoMapperTest {

    @Mock
    private LikesService likesService;

    @InjectMocks
    private PhotoMapper photoMapper;

    @Test
    void toDto_ShouldMapAllFieldsCorrectly_WhenUserIsAuthenticated() {

        User author = new User();
        author.setId(50L);
        author.setDisplayName("Fotograf Jan");
        author.setAvatarUrl("avatar.png");

        Spot spot = new Spot();
        spot.setId(10L);

        Photo photo = Photo.builder()
                .id(1L)
                .url("http://server.com/image.jpg")
                .thumbnailUrl("http://server.com/thumb.jpg")
                .caption("Piękny widok")
                .createdAt(LocalDateTime.now())
                .author(author)
                .spot(spot)
                .build();

        User currentUser = new User();
        currentUser.setId(5L);

        when(likesService.getPhotoLikeCount(1L)).thenReturn(100L);
        when(likesService.isPhotoLiked(1L, currentUser)).thenReturn(true);

        PhotoDto result = photoMapper.toDto(photo, currentUser);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Piękny widok", result.getCaption());
        assertEquals(10L, result.getSpotId());

        assertNotNull(result.getAuthor());
        assertEquals("Fotograf Jan", result.getAuthor().getDisplayName());

        // Sprawdzamy Lajki
        assertEquals(100L, result.getLikeCount());
        assertTrue(result.getIsLiked());
    }

    @Test
    void toDto_ShouldSetIsLikedToFalse_WhenUserIsNotAuthenticated() {

        User author = new User();
        author.setId(50L);

        Photo photo = Photo.builder()
                .id(1L)
                .author(author)
                .build();

        when(likesService.getPhotoLikeCount(1L)).thenReturn(5L);

        PhotoDto result = photoMapper.toDto(photo, null);

        assertEquals(5L, result.getLikeCount());
        assertFalse(result.getIsLiked());
    }
}