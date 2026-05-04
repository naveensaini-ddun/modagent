package com.legacyfirst.modagent.domain;

import java.util.List;
import java.util.UUID;

public record MigrationReport(
        UUID snippetId,
        String summary,
        List<String> identifiedPatterns,
        int complexityScore,
        RiskAssessment riskAssessment,
        String modernizedCode,
        List<MigrationChecklistItem> checklist,
        String targetFramework,
        MigrationStatus status
) {}
