# AI-Powered Enterprise RPA Bridge

An educational prototype demonstrating an enterprise-style automation workflow: receive a webhook, validate incoming records, route uncertain cases for human review, automate data entry into a mock legacy ERP, and keep an auditable processing history.

> **Status:** Initial project scaffold. Features will be implemented incrementally and tested as the project develops.

## Planned architecture

1. **Webhook API (Java + Spring Boot):** accept and validate order or change events.
2. **Reliability layer:** enforce idempotency, retries, and structured audit records.
3. **AI-assisted review:** flag suspicious or incomplete records for human approval; AI output will not silently override validation rules.
4. **RPA worker (Playwright for Java):** enter approved records into a mock legacy ERP interface.
5. **Persistence (MySQL):** store event status, processing attempts, and audit history.
6. **Reporting (Power BI):** visualize throughput, failures, review queues, and processing duration.

## Planned technology stack

- Java 21
- Spring Boot
- Playwright for Java
- MySQL 8.4
- An LLM API behind a configurable service interface
- Power BI for reporting

## Development principles

- Never commit API keys, passwords, tokens, or production data.
- Keep secrets in environment variables; use `.env.example` only for placeholder names.
- Validate data deterministically before invoking AI or browser automation.
- Make processing idempotent and record important state transitions.
- Test with a mock ERP and synthetic data only.

## Local prerequisites

- Java 21
- Maven 3.9+
- MySQL 8.4 (or Docker for a local database)
- Git

## Current status

Only the initial documentation and repository safety files are present. Implementation and test instructions will be added in later milestones.

## Disclaimer

This is an independent learning prototype inspired by common enterprise integration and RPA patterns. It is not a Calance project and does not represent any employer's internal systems.
