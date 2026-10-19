# AI-Powered Enterprise RPA Bridge

An independent learning project that demonstrates how an enterprise-style integration service could receive business events, validate records, route uncertain cases for human review, automate entry into a mock legacy ERP, and keep an auditable processing history.

> **Project status:** Early development. The Spring Boot application has been generated, its initial context test passed locally, and the Actuator health endpoint returned HTTP 200 with status `UP`. A custom status endpoint is being added. The complete webhook, database, AI-review, browser-automation, and reporting workflow is **not implemented yet**.

## Table of contents

- [Problem statement](#problem-statement)
- [Project goals](#project-goals)
- [Planned workflow](#planned-workflow)
- [Architecture](#architecture)
- [Technology stack](#technology-stack)
- [Current implementation](#current-implementation)
- [Repository structure](#repository-structure)
- [Prerequisites](#prerequisites)
- [Run locally](#run-locally)
- [API checks](#api-checks)
- [Testing](#testing)
- [Configuration and secrets](#configuration-and-secrets)
- [Roadmap](#roadmap)
- [Learning outcomes](#learning-outcomes)
- [Limitations and disclaimer](#limitations-and-disclaimer)

## Problem statement

Business teams often move order or change data between modern applications and older systems that do not expose convenient APIs. Manual re-entry is slow and error-prone. This prototype explores a safer automation bridge using an API, deterministic validation, optional AI-assisted review, browser automation against a mock ERP, and an audit trail.

## Project goals

- Receive structured business events over HTTP.
- Validate incoming data before processing.
- Prevent duplicate processing with idempotency controls.
- Persist event status, attempts, and audit details.
- Flag suspicious or incomplete records for human review.
- Automate approved data entry into a mock legacy ERP using Playwright for Java.
- Expose operational information for a Power BI dashboard.
- Keep the design testable, observable, and safe for local development.

## Planned workflow

1. An upstream system sends an authenticated webhook containing an order or change event.
2. The Spring Boot API validates the request shape and business rules.
3. An idempotency check detects repeated event identifiers.
4. The event and its processing status are recorded in MySQL.
5. Optional AI-assisted analysis flags unusual or ambiguous records; deterministic validation remains authoritative.
6. Records requiring judgement are held for human review.
7. Approved records are entered into a local mock ERP through Playwright for Java.
8. Successes, failures, retries, and state transitions are stored for auditing.
9. Power BI reads reporting data to display volumes, processing duration, failure rates, and review queues.

The steps above describe the **target design**, not a claim that all components already exist.

## Architecture

```text
Upstream system
      |
      | HTTP webhook
      v
Spring Boot REST API
      |
      +--> Request and business validation
      |
      +--> Idempotency and event state
      |
      +--> AI-assisted anomaly review (planned)
      |          |
      |          +--> Human approval queue (planned)
      |
      +--> Playwright Java worker (planned)
      |          |
      |          v
      |     Mock legacy ERP (planned)
      |
      v
MySQL event and audit data (planned)
      |
      v
Power BI operational dashboard (planned)
```

## Technology stack

| Technology | Intended use | Status |
|---|---|---|
| Java 21 | Main application language | Installed and selected |
| Spring Boot 4.1.1 | REST API and application runtime | Initial app starts successfully |
| Maven Wrapper | Repeatable build and test commands | Included |
| Spring Web MVC | HTTP endpoints | Dependency configured |
| Jakarta Bean Validation | Request validation | Dependency configured |
| Spring Boot Actuator | Health and operational checks | Health endpoint verified |
| MySQL 8.4 | Event persistence and audit records | Planned |
| Playwright for Java | Browser automation for the mock ERP | Planned |
| LLM API | Optional anomaly/ambiguity assistance | Planned; provider not selected |
| Power BI | Reporting dashboard | Planned |

## Current implementation

- [x] GitHub repository and initial documentation
- [x] Spring Boot project generated with Java 21
- [x] Initial Spring application-context test passed locally (1 test, 0 failures)
- [x] Actuator health endpoint checked locally: `GET /actuator/health` returned HTTP 200 and `status: UP`
- [ ] Custom `GET /api/v1/status` endpoint — being added and must be verified after restarting the app
- [ ] Webhook request model and validation
- [ ] Event persistence and idempotency in MySQL
- [ ] Audit trail and retry strategy
- [ ] Human-review workflow
- [ ] Playwright automation against a mock ERP
- [ ] Automated integration tests
- [ ] Power BI reporting
- [ ] CI workflow and complete setup documentation

## Repository structure

```text
.
├── .env.example
├── .gitignore
├── .mvn/                    # Maven Wrapper configuration
├── mvnw                     # Unix Maven Wrapper
├── mvnw.cmd                 # Windows Maven Wrapper
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/krishna/rpabridge/
    │   └── resources/
    └── test/
        └── java/com/krishna/rpabridge/
```

As features are implemented, the package structure and this tree will be updated.

## Prerequisites

- Java Development Kit (JDK) 21
- Git
- Internet access for the first Maven dependency download
- Maven is optional when using the included wrapper (`./mvnw))

MySQL, an LLM API key, Playwright browsers, and Power BI are not needed for the current health-check milestone.

## Run locally

Clone the repository:

```bash
git clone https://github.com/krishnasharma0329/ai-powered-enterprise-rpa-bridge.git
cd ai-powered-enterprise-rpa-bridge
```

Check Java:

```bash
java -version
```

Run the tests:

```bash
./mvnw test
```

Start the application:

```bash
./mvnw spring-boot:run
```

Keep that terminal running while you make HTTP requests from a second terminal. The default Spring Boot HTTP port is `8080`.

## API checks

### Actuator health

```bash
curl -i http://localhost:8080/actuator/health
```

Expected key fields include HTTP `200` and `"status":"UP"`. Actuator may also return liveness/readiness groups depending on configuration.

### Custom status endpoint

The intended endpoint is:

```http
GET /api/v1/status
```

Test it with:

```bash
curl -i http://localhost:8080/api/v1/status
```

Expected response shape after the controller is present and the app has been restarted:

```json
{
  "application": "AI-Powered Enterprise RPA Bridge",
  "status": "UP",
  "message": "Application is running successfully"
}
```

This custom endpoint must be verified in the local environment; the health check alone does not verify it.

If startup reports that port 8080 is already in use, check the existing process before starting another instance:

```bash
sudo ss -ltnp 'sport = :8080'
```

Stop the earlier application with `Ctrl+C` in the terminal where it is running, then start it once.

## Testing

Run the current automated tests:

```bash
./mvnw test
```

The initial test confirms the Spring application context can load. As the project grows, add unit tests for validation and idempotency, controller tests for HTTP responses, and integration tests for persistence and the mock ERP. Do not describe the whole RPA workflow as tested until those tests exist and pass.

## Configuration and secrets

- Never commit API keys, access tokens, passwords, private keys, or real customer data.
- Keep real credentials in local environment variables or a local, ignored `.env` file.
- `.env.example` contains placeholder values only.
- Do not use production systems or real business records for tests.
- Webhook authentication, request-size limits, safe logging, and authorization must be implemented before exposing this application beyond a trusted local environment.

## Roadmap

1. Confirm and test the custom status endpoint.
2. Define an order-event DTO with validation constraints.
3. Implement a webhook endpoint and clear HTTP error responses.
4. Add MySQL persistence, migrations, event status, and idempotency.
5. Add audit events and bounded retry handling.
6. Build a local mock ERP and automate it with Playwright for Java.
7. Add optional AI-assisted anomaly flags and a human-review path.
8. Add integration tests and operational reporting.
9. Document deployment and add CI checks.

## Learning outcomes

This project is intended to build practical understanding of Java, REST APIs, Spring Boot, validation, HTTP status codes, database design, reliable event processing, browser automation, testing, observability, and safe AI integration.

## Limitations and disclaimer

This is an independent educational prototype inspired by common enterprise integration and RPA patterns. It is **not a Calance project**, does not use Calance internal systems, and is not affiliated with or endorsed by Calance. The planned architecture is industry-style, but it is not production-ready. Do not claim unimplemented components as completed work.
