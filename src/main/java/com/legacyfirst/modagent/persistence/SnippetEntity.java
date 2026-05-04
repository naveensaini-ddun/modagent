package com.legacyfirst.modagent.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "snippets")
@Getter
@Setter
@NoArgsConstructor
public class SnippetEntity {

    @Id
    private UUID id;

    @Lob
    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String language;

    @Column(name = "module_name")
    private String moduleName;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Lob
    @Column(name = "report_json")
    private String reportJson;
}
