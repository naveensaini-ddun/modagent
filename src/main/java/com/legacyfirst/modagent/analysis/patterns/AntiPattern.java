package com.legacyfirst.modagent.analysis.patterns;

import com.legacyfirst.modagent.domain.RiskLevel;

public record AntiPattern(
        String id,
        String name,
        String description,
        RiskLevel defaultRiskLevel
) {}
