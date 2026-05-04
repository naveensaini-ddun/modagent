package com.legacyfirst.modagent.risk.rules;

import com.legacyfirst.modagent.domain.RiskLevel;
import com.legacyfirst.modagent.risk.RiskRule;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class HardcodedCredentialsRule implements RiskRule {

    // Match PWD=..., PASSWORD=..., PASS=... where the value is non-empty and not a placeholder.
    private static final Pattern PATTERN = Pattern.compile(
            "(?i)\\b(pwd|password|pass)\\s*=\\s*[\"']?[^\"';\\s?][^\"';\\s]*"
    );

    @Override
    public String evaluate(String code) {
        return PATTERN.matcher(code).find() ? "HARDCODED_CREDENTIALS" : null;
    }

    @Override
    public RiskLevel level() {
        return RiskLevel.CRITICAL;
    }
}
