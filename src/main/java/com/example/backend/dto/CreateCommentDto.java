package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentDto {

    @NotBlank(message = "Content is required")
    private String content;

    private Long spotId;
    private Long photoId;
}