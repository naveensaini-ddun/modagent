package com.legacyfirst.modagent.service;

import com.legacyfirst.modagent.checklist.ChecklistBuilder;
import com.legacyfirst.modagent.codegen.CodeGenerator;
import com.legacyfirst.modagent.config.ModAgentProperties;
import com.legacyfirst.modagent.domain.MigrationChecklistItem;
import com.legacyfirst.modagent.domain.MigrationReport;
import com.legacyfirst.modagent.domain.MigrationStatus;
import com.legacyfirst.modagent.domain.RiskLevel;
import com.legacyfirst.modagent.persistence.ReportMapper;
import com.legacyfirst.modagent.persistence.SnippetEntity;
import com.legacyfirst.modagent.persistence.SnippetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MigrationService {

    private static final Logger log = LoggerFactory.getLogger(MigrationService.class);

    private final SnippetRepository repository;
    private final ReportMapper reportMapper;
    private final List<CodeGenerator> generators;
    private final ChecklistBuilder checklistBuilder;
    private final ModAgentProperties props;

    public MigrationService(SnippetRepository repository,
                            ReportMapper reportMapper,
                            List<CodeGenerator> generators,
                            ChecklistBuilder checklistBuilder,
                            ModAgentProperties props) {
        this.repository = repository;
        this.reportMapper = reportMapper;
        this.generators = generators;
        this.checklistBuilder = checklistBuilder;
        this.props = props;
    }

    public MigrationReport migrate(UUID id) {
        MDC.put("snippetId", id.toString());
        try {
            SnippetEntity entity = repository.findById(id)
                    .orElseThrow(() -> new SnippetNotFoundException(id));

            MigrationReport existing = reportMapper.fromJson(entity.getReportJson());
            if (existing.modernizedCode() != null) {
                log.info("Snippet already migrated; returning cached report");
                return existing;
            }

            String target = props.target().framework();
            CodeGenerator generator = generators.stream()
                    .filter(g -> g.targetFramework().equals(target))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No CodeGenerator for target " + target));

            log.info("Migrating to target={} generator={}", target, generator.getClass().getSimpleName());
            String modernCode = generator.generate(entity.getCode(), entity.getLanguage());
            List<MigrationChecklistItem> checklist = checklistBuilder.build(
                    existing.identifiedPatterns(), entity.getModuleName());

            MigrationStatus status = statusFor(existing.riskAssessment().level());

            MigrationReport updated = new MigrationReport(
                    existing.snippetId(),
                    existing.summary(),
                    existing.identifiedPatterns(),
                    existing.complexityScore(),
                    existing.riskAssessment(),
                    modernCode,
                    checklist,
                    target,
                    status
            );
            entity.setReportJson(reportMapper.toJson(updated));
            repository.save(entity);
            log.info("Migration complete: status={} checklist_items={}", status, checklist.size());
            return updated;
        } finally {
            MDC.remove("snippetId");
        }
    }

    private MigrationStatus statusFor(RiskLevel level) {
        return switch (level) {
            case CRITICAL -> MigrationStatus.BLOCKED;
            case HIGH, MEDIUM -> MigrationStatus.NEEDS_REVIEW;
            case LOW -> MigrationStatus.READY;
        };
    }
}
