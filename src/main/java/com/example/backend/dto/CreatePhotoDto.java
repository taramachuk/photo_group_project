package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePhotoDto {

    @NotBlank(message = "URL is required")
    private String url;

    @NotNull(message = "Spot ID is required")
    private Long spotId;

    private String caption; // nullable

    private String thumbnailUrl; // nullable
}

