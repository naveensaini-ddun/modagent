package com.legacyfirst.modagent.llm;

import com.legacyfirst.modagent.domain.Analysis;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "modagent.llm", name = "provider", havingValue = "stub", matchIfMissing = true)
public class StubLlmClient implements LlmClient {

    @Override
    public Analysis analyse(String code, String language, String moduleName) {
        var lower = code.toLowerCase();
        var patterns = new ArrayList<String>();
        if (lower.contains("pwd=") || lower.contains("password=")) patterns.add("HARDCODED_CREDENTIALS");
        if (containsRawSql(lower)) patterns.add("RAW_SQL");
        if (lower.contains("on error resume next")) patterns.add("NO_ERROR_HANDLING");

        int lines = (int) code.lines().count();
        int complexity = Math.max(1, Math.min(10, lines / 5 + patterns.size()));

        var module = (moduleName == null || moduleName.isBlank()) ? "unspecified" : moduleName;
        var summary = "Legacy %s module '%s' (%d line(s)) — %d anti-pattern(s) detected"
                .formatted(language, module, lines, patterns.size());

        return new Analysis(summary, List.copyOf(patterns), complexity);
    }

    @Override
    public String generateModernCode(String code, String language, String targetFramework) {
        return ("""
                // SOURCE: %s — TARGET: %s (stub generation; real LLM adapter wired separately)
                package com.example.modernized;

                import org.springframework.jdbc.core.JdbcTemplate;
                import org.springframework.stereotype.Service;

                @Service
                public class ModernizedService {

                    private final JdbcTemplate jdbc;

                    // CHANGED: dependencies injected; no hardcoded connection strings.
                    public ModernizedService(JdbcTemplate jdbc) {
                        this.jdbc = jdbc;
                    }

                    public void run(int customerId) {
                        // CHANGED: parameterised query — no string concatenation, no raw SELECT *.
                        // CHANGED: exceptions propagate; no "On Error Resume Next" silent suppression.
                        jdbc.update(
                                "UPDATE customers SET status = ? WHERE id = ?",
                                "ACTIVE", customerId);
                    }
                }
                """).formatted(language, targetFramework);
    }

    private boolean containsRawSql(String lower) {
        return lower.contains("select ") || lower.contains("insert ")
                || lower.contains("update ") || lower.contains("delete ");
    }
}
