package com.legacyfirst.modagent.risk;

import com.legacyfirst.modagent.domain.RiskLevel;
import com.legacyfirst.modagent.risk.rules.OnErrorResumeNextRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OnErrorResumeNextRuleTest {

    private final OnErrorResumeNextRule rule = new OnErrorResumeNextRule();

    @Test
    void detects_on_error_resume_next_literal() {
        assertEquals("NO_ERROR_HANDLING", rule.evaluate("Sub Foo()\n    On Error Resume Next\nEnd Sub"));
        assertEquals(RiskLevel.MEDIUM, rule.level());
    }

    @Test
    void case_insensitive() {
        assertEquals("NO_ERROR_HANDLING", rule.evaluate("on error resume next"));
    }

    @Test
    void no_match_for_clean_code() {
        assertNull(rule.evaluate("Try\n    Foo()\nCatch ex As Exception\n    Log(ex)\nEnd Try"));
    }
}
