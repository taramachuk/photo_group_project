package com.example.backend.service;

import com.example.backend.model.ForLater;
import com.example.backend.model.ForLaterId;
import com.example.backend.model.Spot;
import com.example.backend.model.User;
import com.example.backend.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpotServiceTest {

    @Mock
    private ForLaterRepository forLaterRepository;
    @Mock
    private SpotRepository spotRepository;

    @Mock private AddressRepository addressRepository;
    @Mock private TagRepository tagRepository;
    @Mock private SpotTagRepository spotTagRepository;
    @Mock private SpotLikeRepository spotLikeRepository;

    @InjectMocks
    private SpotService spotService;

    @Test
    void toggleForLater_ShouldSave_WhenNotSavedYet() {
        Long spotId = 50L;
        User user = new User();
        user.setId(1L);
        Spot spot = new Spot();
        spot.setId(spotId);

        when(forLaterRepository.existsById(any(ForLaterId.class))).thenReturn(false);
        when(spotRepository.findById(spotId)).thenReturn(Optional.of(spot));

        spotService.toggleForLater(spotId, user);

        verify(forLaterRepository, times(1)).save(any(ForLater.class));
        verify(forLaterRepository, never()).deleteById(any(ForLaterId.class));
    }

    @Test
    void toggleForLater_ShouldDelete_WhenAlreadySaved() {
        Long spotId = 50L;
        User user = new User();
        user.setId(1L);

        when(forLaterRepository.existsById(any(ForLaterId.class))).thenReturn(true);

        spotService.toggleForLater(spotId, user);

        verify(forLaterRepository, times(1)).deleteById(any(ForLaterId.class));
        verify(forLaterRepository, never()).save(any(ForLater.class));
    }
}