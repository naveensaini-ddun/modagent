package com.legacyfirst.modagent.domain;

import java.util.List;

public record RiskAssessment(
        RiskLevel level,
        List<String> reasons
) {}
