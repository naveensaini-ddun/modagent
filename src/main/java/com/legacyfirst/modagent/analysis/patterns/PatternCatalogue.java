package com.legacyfirst.modagent.analysis.patterns;

import com.legacyfirst.modagent.domain.RiskLevel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PatternCatalogue {

    private static final List<AntiPattern> PATTERNS = List.of(
            new AntiPattern(
                    "HARDCODED_CREDENTIALS",
                    "Hardcoded credentials",
                    "Connection string or password literal embedded in source code",
                    RiskLevel.CRITICAL),
            new AntiPattern(
                    "RAW_SQL",
                    "Raw SQL execution",
                    "Direct SQL passed to driver without ORM or parameterised binding",
                    RiskLevel.HIGH),
            new AntiPattern(
                    "NO_ERROR_HANDLING",
                    "Missing error handling",
                    "On Error Resume Next or absent error handling around risky operations",
                    RiskLevel.MEDIUM)
    );

    public List<AntiPattern> all() {
        return PATTERNS;
    }
}
