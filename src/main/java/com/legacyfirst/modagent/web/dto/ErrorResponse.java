package com.legacyfirst.modagent.web.dto;

import java.util.List;

public record ErrorResponse(
        int status,
        String error,
        String message,
        List<FieldError> fieldErrors
) {
    public record FieldError(String field, String message) {}
}
