package com.legacyfirst.modagent.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SnippetRepository extends JpaRepository<SnippetEntity, UUID> {
}
