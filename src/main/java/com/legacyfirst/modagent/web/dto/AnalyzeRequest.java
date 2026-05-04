package com.legacyfirst.modagent.web.dto;

import jakarta.validation.constraints.NotBlank;

public record AnalyzeRequest(
        @NotBlank(message = "code_snippet must not be blank") String codeSnippet,
        @NotBlank(message = "language must not be blank") String language,
        String moduleName
) {}
