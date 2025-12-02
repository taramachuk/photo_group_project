package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UploadPhotoDto {
    @NotNull(message = "Spot ID is required")
    @JsonProperty("spot_id")
    private Long spotId;

    @JsonProperty("caption")
    private String caption; // nullable

}

