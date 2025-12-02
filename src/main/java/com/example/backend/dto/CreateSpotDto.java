package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreateSpotDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String description; // nullable

    @NotNull(message = "Latitude is required")
    private BigDecimal latitude;

    @NotNull(message = "Longitude is required")
    private BigDecimal longitude;

    @NotNull(message = "Category ID is required")
    private Integer categoryId;

    @NotBlank(message = "Address name is required")
    private String addressName;

    @NotBlank(message = "Address country is required")
    private String addressCountry;

    private String addressRegion; // nullable

    private List<String> tagNames; // nullable
}

