package com.legacyfirst.modagent.web.dto;

import com.legacyfirst.modagent.domain.RiskLevel;

import java.util.List;
import java.util.UUID;

public record AnalyzeResponse(
        UUID snippetId,
        String summary,
        List<String> identifiedPatterns,
        int complexityScore,
        RiskLevel riskLevel
) {}
