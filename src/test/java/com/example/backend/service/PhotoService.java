package com.example.backend.service;

import com.example.backend.dto.UploadPhotoDto;
import com.example.backend.model.Photo;
import com.example.backend.model.Spot;
import com.example.backend.model.User;
import com.example.backend.repository.PhotoRepository;
import com.example.backend.repository.SpotRepository;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;
    @Mock
    private SpotRepository spotRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PhotoService photoService;

    @Test
    void savePicture_ShouldReturnSavedPhoto_WhenDataIsValid() throws IOException {
        User author = new User();
        author.setId(1L);

        Spot spot = new Spot();
        spot.setId(10L);

        UploadPhotoDto dto = new UploadPhotoDto();
        dto.setSpotId(10L);
        dto.setCaption("Testowe zdjecie");

        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "jakas-binarna-zawartosc-zdjecia".getBytes()
        );

        when(spotRepository.findById(10L)).thenReturn(Optional.of(spot));

        when(photoRepository.save(any(Photo.class))).thenAnswer(i -> i.getArguments()[0]);

        Photo result = photoService.savePicture(file, dto, author);

        assertNotNull(result);
        assertEquals("Testowe zdjecie", result.getCaption());
        assertTrue(result.getUrl().contains("test.jpg"));
        assertEquals(author, result.getAuthor());
        assertEquals(spot, result.getSpot());

        verify(photoRepository, times(1)).save(any(Photo.class));
    }



    @Test
    void deletePhoto_ShouldDelete_WhenUserIsAuthor() {
        Long photoId = 100L;
        User author = new User();
        author.setId(1L);

        Photo photo = new Photo();
        photo.setId(photoId);
        photo.setAuthor(author);

        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));

        photoService.deletePhoto(photoId, author);

        verify(photoRepository, times(1)).delete(photo);
    }

    @Test
    void deletePhoto_ShouldThrowException_WhenUserIsNotAuthor() {
        Long photoId = 100L;
        User author = new User();
        author.setId(1L);

        User stranger = new User();
        stranger.setId(99L);

        Photo photo = new Photo();
        photo.setId(photoId);
        photo.setAuthor(author);

        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));

        assertThrows(com.example.backend.exception.UnauthorizedException.class, () -> {
            photoService.deletePhoto(photoId, stranger);
        });

        verify(photoRepository, never()).delete(any(Photo.class));
    }




}


