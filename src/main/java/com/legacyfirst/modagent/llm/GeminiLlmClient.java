package com.legacyfirst.modagent.llm;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.legacyfirst.modagent.config.ModAgentProperties;
import com.legacyfirst.modagent.domain.Analysis;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
@ConditionalOnProperty(prefix = "modagent.llm", name = "provider", havingValue = "gemini")
public class GeminiLlmClient implements LlmClient {

    private final RestClient restClient;
    private final ModAgentProperties props;
    private final ObjectMapper objectMapper;

    @Value("classpath:prompts/migration-prompt.st")
    private Resource promptTemplate;

    public GeminiLlmClient(ModAgentProperties props, ObjectMapper objectMapper) {
        this.props = props;
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder().build(); // Base URL moved to callLlm
    }

    @Override
    public Analysis analyse(String code, String language, String moduleName) {
        JsonNode response = callLlm(code, language, moduleName);
        JsonNode analysisNode = response.get("analysis");

        List<String> patterns = objectMapper.convertValue(
                analysisNode.get("patterns"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));

        return new Analysis(
                analysisNode.get("summary").asText(),
                patterns,
                analysisNode.get("complexity").asInt());
    }

    @Override
    public String generateModernCode(String code, String language, String targetFramework) {
        JsonNode response = callLlm(code, language, targetFramework);
        return response.get("modernized_code").asText();
    }

    private JsonNode callLlm(String code, String language, String context) {
        try {
            String model = props.llm().model();
            String apiVersion = model.contains("preview") ? "v1beta" : "v1";
            String url = "https://generativelanguage.googleapis.com/%s/models/%s:generateContent"
                    .formatted(apiVersion, model);

            String systemPrompt = promptTemplate.getContentAsString(StandardCharsets.UTF_8);
            String userPrompt = "Language: %s\nContext: %s\nCode:\n%s".formatted(language, context, code);

            Map<String, Object> requestBody = Map.of(
                    "system_instruction", Map.of("parts", List.of(Map.of("text", systemPrompt))),
                    "contents", List.of(Map.of("parts", List.of(Map.of("text", userPrompt)))),
                    "generationConfig", Map.of(
                            "responseMimeType", "application/json",
                            "temperature", 0.1 // Keep it deterministic for JSON
                    ));

            String rawResponse = restClient.post()
                    .uri(url + "?key=" + props.llm().apiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(JsonNode.class)
                    .path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

            // --- NEW: CLEANING LOGIC ---
            String cleanedJson = cleanJson(rawResponse);
            return objectMapper.readTree(cleanedJson);
            // ---------------------------

        } catch (Exception e) {
            throw new RuntimeException("Failed to communicate with Gemini: " + e.getMessage(), e);
        }
    }

    /**
     * Strips Markdown code blocks and leading/trailing whitespace
     * that often trip up the Jackson parser.
     */
    private String cleanJson(String raw) {
        String cleaned = raw.trim();
        if (cleaned.startsWith("```")) {
            // Remove opening ```json or ```
            cleaned = cleaned.replaceAll("^```(?:json)?\\n?", "");
            // Remove closing ```
            cleaned = cleaned.replaceAll("\\n?```$", "");
        }
        return cleaned.trim();
    }
}