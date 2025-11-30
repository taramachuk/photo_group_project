package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoDto {
    private Long id;
    private String url;
    private String thumbnailUrl;
    private String caption;
    private LocalDateTime createdAt;
    private AuthorDto author;
    private Long spotId;
}

