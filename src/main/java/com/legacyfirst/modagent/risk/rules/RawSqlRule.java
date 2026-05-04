package com.legacyfirst.modagent.risk.rules;

import com.legacyfirst.modagent.domain.RiskLevel;
import com.legacyfirst.modagent.risk.RiskRule;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class RawSqlRule implements RiskRule {

    private static final Pattern PATTERN = Pattern.compile(
            "(?i)\\b(select\\b|insert\\s+into\\b|update\\s+\\w+\\s+set\\b|delete\\s+from\\b)"
    );

    @Override
    public String evaluate(String code) {
        return PATTERN.matcher(code).find() ? "RAW_SQL" : null;
    }

    @Override
    public RiskLevel level() {
        return RiskLevel.HIGH;
    }
}
