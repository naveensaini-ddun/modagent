package com.legacyfirst.modagent.domain;

import java.util.List;

public record Analysis(
        String summary,
        List<String> identifiedPatterns,
        int complexityScore
) {}
