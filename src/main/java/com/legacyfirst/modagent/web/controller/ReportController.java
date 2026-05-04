package com.legacyfirst.modagent.web.controller;

import com.legacyfirst.modagent.domain.MigrationReport;
import com.legacyfirst.modagent.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public MigrationReport report(@PathVariable UUID id) {
        return service.getReport(id);
    }
}
