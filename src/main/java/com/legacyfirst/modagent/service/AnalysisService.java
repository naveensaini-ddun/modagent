package com.legacyfirst.modagent.service;

import com.legacyfirst.modagent.config.ModAgentProperties;
import com.legacyfirst.modagent.domain.Analysis;
import com.legacyfirst.modagent.domain.MigrationReport;
import com.legacyfirst.modagent.domain.MigrationStatus;
import com.legacyfirst.modagent.domain.RiskAssessment;
import com.legacyfirst.modagent.llm.LlmClient;
import com.legacyfirst.modagent.persistence.ReportMapper;
import com.legacyfirst.modagent.persistence.SnippetEntity;
import com.legacyfirst.modagent.persistence.SnippetRepository;
import com.legacyfirst.modagent.risk.RuleEngine;
import com.legacyfirst.modagent.web.dto.AnalyzeRequest;
import com.legacyfirst.modagent.web.dto.AnalyzeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class AnalysisService {

    private static final Logger log = LoggerFactory.getLogger(AnalysisService.class);

    private final RuleEngine ruleEngine;
    private final LlmClient llmClient;
    private final SnippetRepository repository;
    private final ReportMapper reportMapper;
    private final ModAgentProperties props;

    public AnalysisService(RuleEngine ruleEngine,
                           LlmClient llmClient,
                           SnippetRepository repository,
                           ReportMapper reportMapper,
                           ModAgentProperties props) {
        this.ruleEngine = ruleEngine;
        this.llmClient = llmClient;
        this.repository = repository;
        this.reportMapper = reportMapper;
        this.props = props;
    }

    public AnalyzeResponse analyze(AnalyzeRequest request) {
        UUID id = UUID.randomUUID();
        MDC.put("snippetId", id.toString());
        try {
            log.info("Analysing snippet language={} module={}", request.language(), request.moduleName());

            RiskAssessment risk = ruleEngine.assess(request.codeSnippet());
            Analysis analysis = llmClient.analyse(request.codeSnippet(), request.language(), request.moduleName());

            MigrationReport report = new MigrationReport(
                    id,
                    analysis.summary(),
                    analysis.identifiedPatterns(),
                    analysis.complexityScore(),
                    risk,
                    null,
                    null,
                    props.target().framework(),
                    MigrationStatus.NEEDS_REVIEW
            );

            SnippetEntity entity = new SnippetEntity();
            entity.setId(id);
            entity.setCode(request.codeSnippet());
            entity.setLanguage(request.language());
            entity.setModuleName(request.moduleName());
            entity.setCreatedAt(Instant.now());
            entity.setReportJson(reportMapper.toJson(report));
            repository.save(entity);

            log.info("Analysed: risk={} patterns={} complexity={}",
                    risk.level(), analysis.identifiedPatterns().size(), analysis.complexityScore());

            return new AnalyzeResponse(
                    id,
                    analysis.summary(),
                    analysis.identifiedPatterns(),
                    analysis.complexityScore(),
                    risk.level()
            );
        } finally {
            MDC.remove("snippetId");
        }
    }
}
