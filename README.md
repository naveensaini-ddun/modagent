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

## Status

Scaffolding only — controllers, services, rule engine, and LLM client are still being built. See `HANDOFF.md` for the implementation plan.
