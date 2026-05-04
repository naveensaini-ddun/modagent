# modagent

LLM-assisted REST API that ingests legacy VB6 / Classic ASP snippets, detects anti-patterns, assesses migration risk via a deterministic rule engine, generates modern equivalents (Spring Boot for the MVP), and emits a structured migration report.

## Stack

- Java 21, Spring Boot 4.0.6, Gradle (Groovy)
- Spring Web MVC, Validation, Data JPA, H2 (in-memory)
- Lombok, DevTools, Configuration Processor

## Run

```bash
./gradlew bootRun
```

App on `http://localhost:8080`. H2 console at `/h2` (JDBC URL `jdbc:h2:mem:modagent`, user `sa`, no password).

## Configuration (env vars)

All `modagent.*` config is bound from environment variables with sensible defaults — nothing is required to boot.

| Env var | Default | Notes |
|---|---|---|
| `MODAGENT_LLM_PROVIDER` | `stub` | `stub` for local dev; real adapters wired later |
| `MODAGENT_LLM_API_KEY` | *(empty)* | logged as `<set>` / `<unset>` only, never the value |
| `MODAGENT_LLM_MODEL` | `stub-model` | |
| `MODAGENT_LLM_TIMEOUT_SECONDS` | `30` | |
| `MODAGENT_TARGET_FRAMEWORK` | `spring-boot` | only target wired end-to-end for MVP |

Boot logs `ModAgent ready — llm.provider=… target.framework=…` on startup, so you can confirm what got bound.

## Endpoints (planned)

| Method | Path | Purpose |
|---|---|---|
| `POST` | `/analyze` | submit snippet, get `snippet_id`, summary, patterns, complexity |
| `POST` | `/migrate/{snippet_id}` | generate modernized code + checklist |
| `GET` | `/report/{snippet_id}` | full migration report |
| `GET` | `/patterns` | list detectable anti-patterns |

## Project layout

```
com.legacyfirst.modagent
├── web/        controllers + DTOs + global exception handler (422 / 404)
├── service/    AnalysisService, MigrationService, ReportService
├── analysis/   PatternCatalogue (anti-pattern catalog returned by /patterns)
├── risk/       RuleEngine + 3 deterministic rules (hardcoded creds, raw SQL, On Error Resume Next)
├── codegen/    CodeGenerator strategy (SpringBootCodeGenerator only for MVP)
├── checklist/  ChecklistBuilder (pattern → checklist item mapping)
├── llm/        LlmClient port + StubLlmClient (env-toggled, no real LLM in MVP)
├── persistence/ SnippetEntity (H2) + SnippetRepository + ReportMapper (JSON ↔ MigrationReport)
├── domain/     records + enums (Snippet, MigrationReport, RiskLevel, MigrationStatus, …)
└── config/     ModAgentProperties + StartupLogger
```
