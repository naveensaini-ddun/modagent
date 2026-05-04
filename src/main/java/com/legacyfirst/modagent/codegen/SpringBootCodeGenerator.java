package com.legacyfirst.modagent.codegen;

import com.legacyfirst.modagent.llm.LlmClient;
import org.springframework.stereotype.Component;

@Component
public class SpringBootCodeGenerator implements CodeGenerator {

    private final LlmClient llmClient;

    public SpringBootCodeGenerator(LlmClient llmClient) {
        this.llmClient = llmClient;
    }

    @Override
    public String targetFramework() {
        return "spring-boot";
    }

    @Override
    public String generate(String legacyCode, String language) {
        return llmClient.generateModernCode(legacyCode, language, targetFramework());
    }
}
