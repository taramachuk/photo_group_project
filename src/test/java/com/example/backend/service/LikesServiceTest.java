package com.example.backend.service;

import com.example.backend.model.Spot;
import com.example.backend.model.SpotLike;
import com.example.backend.model.User;
import com.example.backend.repository.PhotoLikeRepository;
import com.example.backend.repository.PhotoRepository;
import com.example.backend.repository.SpotLikeRepository;
import com.example.backend.repository.SpotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikesServiceTest {

    @Mock
    private SpotLikeRepository spotLikeRepository;

    @Mock
    private SpotRepository spotRepository;

    @Mock
    private PhotoLikeRepository photoLikeRepository;
    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    private LikesService likesService;

    @Test
    void toggleSpotLike_ShouldAddLike_WhenNotLikedYet() {
        Long spotId = 100L;
        User user = new User();
        user.setId(1L);
        Spot spot = new Spot();
        spot.setId(spotId);

        when(spotRepository.findById(spotId)).thenReturn(Optional.of(spot));
        when(spotLikeRepository.existsByUserIdAndSpotId(user.getId(), spotId)).thenReturn(false);

        boolean result = likesService.toggleSpotLike(spotId, user);

        assertTrue(result, "Metoda powinna zwrócić true (lajk dodany)");

        verify(spotLikeRepository, times(1)).save(any(SpotLike.class));

        verify(spotLikeRepository, never()).deleteByUserIdAndSpotId(anyLong(), anyLong());
    }

    @Test
    void toggleSpotLike_ShouldRemoveLike_WhenAlreadyLiked() {

        Long spotId = 100L;
        User user = new User();
        user.setId(1L);
        Spot spot = new Spot();
        spot.setId(spotId);

        when(spotRepository.findById(spotId)).thenReturn(Optional.of(spot));
        when(spotLikeRepository.existsByUserIdAndSpotId(user.getId(), spotId)).thenReturn(true);

        boolean result = likesService.toggleSpotLike(spotId, user);
        assertFalse(result, "Metoda powinna zwrócić false (lajk usunięty)");

        verify(spotLikeRepository, times(1)).deleteByUserIdAndSpotId(user.getId(), spotId);
        verify(spotLikeRepository, never()).save(any(SpotLike.class));
    }
}