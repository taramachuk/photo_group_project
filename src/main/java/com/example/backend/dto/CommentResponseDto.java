package com.example.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;

    private String content;

    private String authorName;

    private LocalDateTime createdAt;
}