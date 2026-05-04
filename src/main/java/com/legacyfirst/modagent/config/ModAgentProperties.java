package com.legacyfirst.modagent.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "modagent")
public record ModAgentProperties(
        @NotNull @Valid Llm llm,
        @NotNull @Valid Target target
) {
    public record Llm(
            @NotBlank String provider,
            String apiKey,
            @NotBlank String model,
            @Positive int timeoutSeconds
    ) {}

    public record Target(
            @NotBlank String framework
    ) {}
}
