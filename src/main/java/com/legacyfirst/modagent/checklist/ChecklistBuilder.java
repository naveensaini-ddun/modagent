package com.legacyfirst.modagent.checklist;

import com.legacyfirst.modagent.domain.MigrationChecklistItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChecklistBuilder {

    public List<MigrationChecklistItem> build(List<String> identifiedPatterns, String moduleName) {
        var items = new ArrayList<MigrationChecklistItem>();
        var module = (moduleName == null || moduleName.isBlank()) ? "this module" : moduleName;

        for (String pattern : identifiedPatterns) {
            switch (pattern) {
                case "HARDCODED_CREDENTIALS" -> items.add(new MigrationChecklistItem(
                        "Replace hardcoded DB connection string with environment variable"));
                case "RAW_SQL" -> items.add(new MigrationChecklistItem(
                        "Replace raw SQL SELECT * with ORM-based parameterised query"));
                case "NO_ERROR_HANDLING" -> items.add(new MigrationChecklistItem(
                        "Add try-catch / exception handling around DB operations"));
                default -> { /* unknown pattern — no checklist item */ }
            }
        }
        items.add(new MigrationChecklistItem(
                "Add unit test for %s covering edge cases".formatted(module)));
        return List.copyOf(items);
    }
}
