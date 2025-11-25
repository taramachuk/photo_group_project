package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class UpdateSpotDto {

    private String title; // nullable

    private String description; // nullable

    private BigDecimal latitude; // nullable

    private BigDecimal longitude; // nullable

    private Integer categoryId; // nullable

    private String addressName; // nullable

    private String addressCountry; // nullable

    private String addressRegion; // nullable

    private List<String> tagNames; // nullable
}

