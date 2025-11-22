package com.example.backend.controller;

import com.example.backend.dto.ReportDto;
import com.example.backend.requests.ReportRequest;
import com.example.backend.service.ReportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/reports")
@RestController
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/create")
    public ReportDto createReport(ReportRequest reportRequest) {
        return reportService.createReport(reportRequest);

    }


}
