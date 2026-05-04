package com.legacyfirst.modagent.service;

import java.util.UUID;

public class SnippetNotFoundException extends RuntimeException {
    public SnippetNotFoundException(UUID id) {
        super("Snippet not found: " + id);
    }
}
