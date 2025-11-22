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
public class ReportDto {
    private Long id;
    private String reason;
    private Integer status;
    private LocalDateTime createdAt;
    private Long reporterId;
    private Long spotId;
    private Long photoId;
    private Long commentId;
}
