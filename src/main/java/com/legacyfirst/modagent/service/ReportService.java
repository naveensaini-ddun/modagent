package com.legacyfirst.modagent.service;

import com.legacyfirst.modagent.domain.MigrationReport;
import com.legacyfirst.modagent.persistence.ReportMapper;
import com.legacyfirst.modagent.persistence.SnippetEntity;
import com.legacyfirst.modagent.persistence.SnippetRepository;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ReportService {

    private final SnippetRepository repository;
    private final ReportMapper reportMapper;

    public ReportService(SnippetRepository repository, ReportMapper reportMapper) {
        this.repository = repository;
        this.reportMapper = reportMapper;
    }

    public MigrationReport getReport(UUID id) {
        MDC.put("snippetId", id.toString());
        try {
            SnippetEntity entity = repository.findById(id)
                    .orElseThrow(() -> new SnippetNotFoundException(id));
            return reportMapper.fromJson(entity.getReportJson());
        } finally {
            MDC.remove("snippetId");
        }
    }
}
