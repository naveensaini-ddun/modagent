package com.legacyfirst.modagent;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ModagentApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private tools.jackson.databind.ObjectMapper objectMapper;

    private static String sharedSnippetId;

    @Test
    @Order(1)
    void shouldReturnAllPatterns() throws Exception {
        // Request 1: Deterministic list from your PatternCatalogue
        mockMvc.perform(get("/patterns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(empty()))));
    }

    @Test
    @Order(2)
    void shouldAnalyzeVb6Snippet() throws Exception {
        // Request 2: Analysis
        Map<String, String> request = Map.of(
                "code_snippet", "Sub Test() \n Dim p As String: p = \"password\" \n End Sub",
                "language", "VB6"
        );

        MvcResult result = mockMvc.perform(post("/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.snippet_id").exists())
                .andExpect(jsonPath("$.risk_level").exists()) // Just check it's there
                .andExpect(jsonPath("$.identified_patterns").isArray())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        sharedSnippetId = objectMapper.readTree(responseBody).get("snippet_id").asText();
    }

    @Test
    @Order(3)
    void shouldMigrateSnippet() throws Exception {
        // Request 3: Migration logic
        // We check if code was generated, not the specific wording of the comments
        mockMvc.perform(post("/migrate/" + sharedSnippetId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modernized_code", not(emptyString())))
                .andExpect(jsonPath("$.checklist", is(not(nullValue()))));
    }

    @Test
    @Order(4)
    void shouldRetrievePersistedReport() throws Exception {
        // Request 4: Persistence
        mockMvc.perform(get("/report/" + sharedSnippetId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.snippet_id").value(sharedSnippetId));
    }

    @Test
    @Order(5)
    void shouldReturn422OnEmptyPayload() throws Exception {
        // Request 5: Validation (This remains strict as it's non-LLM logic)
        mockMvc.perform(post("/analyze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code_snippet\": \"\", \"language\": \"\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.field_errors").exists());
    }
}