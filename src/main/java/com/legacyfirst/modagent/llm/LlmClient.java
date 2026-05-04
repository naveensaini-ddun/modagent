package com.legacyfirst.modagent.llm;

import com.legacyfirst.modagent.domain.Analysis;

public interface LlmClient {
    Analysis analyse(String code, String language, String moduleName);

    String generateModernCode(String code, String language, String targetFramework);
}
