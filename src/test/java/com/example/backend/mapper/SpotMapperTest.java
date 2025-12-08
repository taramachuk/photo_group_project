package com.example.backend.mapper;

import com.example.backend.dto.SpotDto;
import com.example.backend.model.*;
import com.example.backend.repository.SpotTagRepository;
import com.example.backend.service.LikesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpotMapperTest {

    @Mock
    private SpotTagRepository spotTagRepository;

    @Mock
    private LikesService likesService;

    @InjectMocks
    private SpotMapper spotMapper;

    @Test
    void toDto_ShouldMapAllFieldsCorrectly_WhenUserIsAuthenticated() {
        User author = new User();
        author.setId(99L);
        author.setDisplayName("Jan Kowalski");
        author.setAvatarUrl("avatar.jpg");

        Address address = new Address();
        address.setId(10L);
        address.setName("Centrum");
        address.setCountry("Polska");

        Spot spot = Spot.builder()
                .id(1L)
                .title("Fajna Miejscówka")
                .description("Opis")
                .latitude(new BigDecimal("52.2"))
                .longitude(new BigDecimal("21.0"))
                .createdAt(LocalDateTime.now())
                .categoryId(1)
                .author(author)
                .address(address)
                .build();

        Tag tag = new Tag(5L, "Nature");
        SpotTag spotTag = new SpotTag(new SpotTagId(1L, 5L), spot, tag);
        when(spotTagRepository.findById_SpotId(1L)).thenReturn(List.of(spotTag));

        User currentUser = new User();
        currentUser.setId(5L);
        when(likesService.getSpotLikeCount(1L)).thenReturn(10L);
        when(likesService.isSpotLiked(1L, currentUser)).thenReturn(true);

        SpotDto result = spotMapper.toDto(spot, currentUser);

        assertNotNull(result);
        assertEquals("Fajna Miejscówka", result.getTitle());

        assertNotNull(result.getAuthor());
        assertEquals("Jan Kowalski", result.getAuthor().getDisplayName());

        assertEquals(10L, result.getLikeCount());
        assertTrue(result.getIsLiked());

        assertEquals(1, result.getTagNames().size());
        assertEquals("Nature", result.getTagNames().get(0));
    }
}