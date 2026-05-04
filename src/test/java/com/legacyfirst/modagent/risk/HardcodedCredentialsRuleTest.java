package com.legacyfirst.modagent.risk;

import com.legacyfirst.modagent.domain.RiskLevel;
import com.legacyfirst.modagent.risk.rules.HardcodedCredentialsRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HardcodedCredentialsRuleTest {

    private final HardcodedCredentialsRule rule = new HardcodedCredentialsRule();

    @Test
    void detects_pwd_in_connection_string() {
        String code = "conn = \"Driver={SQL Server};Server=10.0.0.5;UID=admin;PWD=Pa$$w0rd!\"";
        assertEquals("HARDCODED_CREDENTIALS", rule.evaluate(code));
        assertEquals(RiskLevel.CRITICAL, rule.level());
    }

    @Test
    void detects_password_with_quoted_value() {
        assertEquals("HARDCODED_CREDENTIALS", rule.evaluate("PASSWORD='secret123'"));
    }

    @Test
    void no_match_for_placeholder() {
        assertNull(rule.evaluate("password = ?"));
    }

    @Test
    void no_match_for_clean_code() {
        assertNull(rule.evaluate("Dim x As Integer\nx = 1 + 2"));
    }
}
