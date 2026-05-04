package com.legacyfirst.modagent.domain;

import java.time.Instant;
import java.util.UUID;

public record Snippet(
        UUID id,
        String code,
        String language,
        String moduleName,
        Instant createdAt
) {}
