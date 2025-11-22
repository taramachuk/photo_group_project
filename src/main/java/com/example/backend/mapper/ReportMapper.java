package com.example.backend.mapper;

import com.example.backend.dto.ReportDto;
import com.example.backend.model.Report;
import com.example.backend.model.User;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.PhotoRepository;
import com.example.backend.repository.SpotRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.requests.ReportRequest;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {
    private final UserRepository userRepository;
    private final SpotRepository spotRepository;
    private final PhotoRepository photoRepository;
    private final CommentRepository commentRepository;

    public ReportMapper(UserRepository userRepository, SpotRepository spotRepository, PhotoRepository photoRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.spotRepository = spotRepository;
        this.photoRepository = photoRepository;
        this.commentRepository = commentRepository;
    }

    public ReportDto toDto(Report report) {
        if (report == null) {
            return null;
        }

        return ReportDto.builder()
                .id(report.getId())
                .reason(report.getReason())
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .reporterId(report.getReporter().getId())
                .spotId(report.getSpot().getId())
                .photoId(report.getPhoto().getId())
                .commentId(report.getComment().getId())
                .build();
    }

    public Report toEntity(ReportDto reportDto) {
        if (reportDto == null) {
            return null;
        }

        Report report = new Report();
//        report.setId(reportDto.getId());
        report.setReason(reportDto.getReason());
        report.setStatus(reportDto.getStatus());
        report.setCreatedAt(reportDto.getCreatedAt());
        report.setReporter(userRepository.findById(reportDto.getReporterId()).orElse(null));
        report.setSpot(spotRepository.findById(reportDto.getSpotId()).orElse(null));
        report.setPhoto(photoRepository.findById(reportDto.getPhotoId()).orElse(null));
        report.setComment(commentRepository.findById(reportDto.getCommentId()).orElse(null));
        return report;
    }

    public Report fromReqToReport(ReportRequest reportRequest) {
        if (reportRequest == null) {
            return null;
        }

        Report report = new Report();
        report.setReason(reportRequest.getReason());
        report.setSpot(spotRepository.findById(reportRequest.getSpotId()).orElse(null));
        report.setPhoto(photoRepository.findById(reportRequest.getPhotoId()).orElse(null));
        report.setComment(commentRepository.findById(reportRequest.getCommentId()).orElse(null));
        return report;
    }

}
