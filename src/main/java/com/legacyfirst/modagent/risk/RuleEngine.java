package com.legacyfirst.modagent.risk;

import com.legacyfirst.modagent.domain.RiskAssessment;
import com.legacyfirst.modagent.domain.RiskLevel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RuleEngine {

    private final List<RiskRule> rules;

    public RuleEngine(List<RiskRule> rules) {
        this.rules = rules;
    }

    public RiskAssessment assess(String code) {
        var reasons = new ArrayList<String>();
        var highest = RiskLevel.LOW;
        for (RiskRule rule : rules) {
            String reason = rule.evaluate(code);
            if (reason != null) {
                reasons.add(reason);
                if (rule.level().ordinal() > highest.ordinal()) {
                    highest = rule.level();
                }
            }
        }
        return new RiskAssessment(highest, List.copyOf(reasons));
    }
}
