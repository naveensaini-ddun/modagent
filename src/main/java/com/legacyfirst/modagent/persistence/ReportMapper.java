package com.legacyfirst.modagent.persistence;

import com.legacyfirst.modagent.domain.MigrationReport;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
public class ReportMapper {

    private final ObjectMapper mapper;

    public ReportMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String toJson(MigrationReport report) {
        try {
            return mapper.writeValueAsString(report);
        } catch (JacksonException e) {
            throw new IllegalStateException("Failed to serialise migration report", e);
        }
    }

    public MigrationReport fromJson(String json) {
        if (json == null || json.isBlank()) return null;
        try {
            return mapper.readValue(json, MigrationReport.class);
        } catch (JacksonException e) {
            throw new IllegalStateException("Failed to deserialise migration report", e);
        }
    }
}
