package com.legacyfirst.modagent.risk;

import com.legacyfirst.modagent.domain.RiskLevel;

public interface RiskRule {

    /** @return reason ID (e.g. "HARDCODED_CREDENTIALS") if the rule matches the snippet, else null. */
    String evaluate(String code);

    RiskLevel level();
}
