package com.legacyfirst.modagent.risk;

import com.legacyfirst.modagent.domain.RiskLevel;
import com.legacyfirst.modagent.risk.rules.RawSqlRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RawSqlRuleTest {

    private final RawSqlRule rule = new RawSqlRule();

    @Test
    void detects_select_star() {
        assertEquals("RAW_SQL", rule.evaluate("sql = \"SELECT * FROM customers WHERE id = \" & id"));
        assertEquals(RiskLevel.HIGH, rule.level());
    }

    @Test
    void detects_insert_into() {
        assertEquals("RAW_SQL", rule.evaluate("INSERT INTO orders (id, total) VALUES (1, 9.99)"));
    }

    @Test
    void detects_update_set() {
        assertEquals("RAW_SQL", rule.evaluate("UPDATE customers SET status = 'ACTIVE'"));
    }

    @Test
    void detects_delete_from() {
        assertEquals("RAW_SQL", rule.evaluate("DELETE FROM logs WHERE created_at < '2020-01-01'"));
    }

    @Test
    void no_match_for_clean_code() {
        assertNull(rule.evaluate("Dim total As Double\ntotal = principal * rate"));
    }
}
