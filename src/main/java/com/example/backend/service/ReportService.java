package com.example.backend.service;

import com.example.backend.dto.ReportDto;
import com.example.backend.mapper.ReportMapper;
import com.example.backend.model.Report;
import com.example.backend.model.User;
import com.example.backend.repository.*;
import com.example.backend.requests.ReportRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReportService {
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository,
                         SpotRepository spotRepository,
                         PhotoRepository photoRepository,
                         CommentRepository commentRepository) {
        this.reportRepository = reportRepository;
        this.reportMapper = new ReportMapper(userRepository, spotRepository, photoRepository, commentRepository);
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
        this.commentRepository = commentRepository;
    }

    public ReportDto createReport(ReportRequest reportRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOpt = userRepository.findByEmail(auth.getName());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with email: " + auth.getName());
        }
        User currentUser = userOpt.get();

        Report report = reportMapper.fromReqToReport(reportRequest);
        report.setReporter(currentUser);

        reportRepository.save(report);

        return reportMapper.toDto(report);
    }
}
