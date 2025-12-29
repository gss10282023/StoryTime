# StoryTime

[![CI](https://img.shields.io/badge/CI-GitHub%20Actions-2088FF?logo=githubactions&logoColor=white)](https://github.com/gss10282023/StoryTime/actions) [![Version](https://img.shields.io/badge/version-1.0.0-blue)](CHANGELOG.md) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://adoptium.net/) [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot) [![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/) [![Gradle](https://img.shields.io/badge/Gradle-8.10.2-02303A?logo=gradle&logoColor=white)](https://gradle.org/)

StoryTime is a story-driven learning generator for kids, parents, and educational settings. Users choose a theme, age group, characters, and language to generate visual stories and learning content.

中文说明：`README.zh-CN.md`


## Screenshots
![StoryTime preview](docs/assets/home.png)
![Education](docs/assets/education.png)
![Story](docs/assets/story.png)
![Sign in](docs/assets/signin.png)
![Sign up](docs/assets/signup.png)

## Features

- Story generation (fairytale) with images/audio, streamed via WebSocket
- Education content generation (sentence + image + audio + optional video)
- PostgreSQL persistence and a simple dashboard view
- Optional integrations: OpenAI, Stable Diffusion, Luma, and S3-compatible object storage

## Tech Stack

- Backend: Spring Boot 3.3.4 (Spring Web, WebSocket/STOMP, Thymeleaf, WebFlux)
- Data: PostgreSQL + JPA/Hibernate + MyBatis
- API docs: SpringDoc OpenAPI (`/swagger-ui/index.html`, `/v3/api-docs`)
- Storage (optional): AWS SDK S3 (e.g., Vultr Object Storage)
- Build/test: Gradle 8.10.2 (Java 17), JUnit 5, JaCoCo, GitHub Actions CI

## Architecture Notes

- UI is server-side rendered with Thymeleaf templates (`src/main/resources/templates`) + static assets (`src/main/resources/static`).
- Story/education generation is triggered via HTTP POST and runs asynchronously; progress and results are streamed back to the browser via WebSocket.
- WebSocket uses STOMP over SockJS:
  - Endpoint: `/ws`
  - Broker prefix: `/topic`
  - App prefix: `/app`
  - Per-session topics: `/topic/progress/{sessionId}`, `/topic/scene/{sessionId}`, `/topic/result/{sessionId}`
  - Global topics (image endpoint): `/topic/image`, `/topic/error`
- `local` and `demo` profiles initialize the schema from `src/main/resources/db/schema.sql`.

## Quick Start

### Prerequisites

- Java 17
- PostgreSQL 15+ (or Docker + Docker Compose)

### Option C: Demo mode (no external API keys)

This mode uses built-in sample image/audio/video assets and does not call OpenAI / Stable Diffusion / Luma / S3.

```bash
docker compose up -d
./gradlew bootRun --args='--spring.profiles.active=demo'
```

Open `http://localhost:8080/`

### Option A: Docker Postgres + run app locally

1. Start Postgres:

   ```bash
   docker compose up -d
   ```

   Make sure Docker Desktop (daemon) is running.

2. Export environment variables (Spring reads env vars; it does not automatically load `.env`):

   ```bash
   cp .env.example .env
   set -a
   source .env
   set +a
   ```

3. Run the app:

   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=local'
   ```

4. Open `http://localhost:8080/`

### Option B: Local Postgres

1. Create a database named `storytime`.
2. Export `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` (see `.env.example`).
3. Run:

   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=local'
   ```

The `local` profile initializes the schema from `src/main/resources/db/schema.sql`.

## Configuration

Required:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

Optional (feature-dependent):

- `OPENAI_API_KEY`
- `STABLEDIFFUSION_API_KEY`
- `LUMA_API_KEY`
- `VULTR_ACCESS_KEY`, `VULTR_SECRET_KEY`, `VULTR_BUCKET_NAME`, `VULTR_ENDPOINT`

## Development

- Run tests: `./gradlew test`
- Reports:
  - JUnit: `build/reports/tests/test/index.html`
  - JaCoCo: `build/reports/jacoco/test/html/index.html`
- Integration tests that call external APIs are skipped unless `OPENAI_API_KEY` / `STABLEDIFFUSION_API_KEY` are set.

## Data & Privacy

- StoryTime stores generated content and metadata in PostgreSQL (e.g., user records, story text, image/video URLs, generation timestamps).
- When configured, StoryTime may send user input and prompts to third-party services (OpenAI / Stable Diffusion / Luma) and may upload generated assets to S3-compatible object storage.
- The `demo` profile does not make any external AI/S3 requests.

## Security Note

This project currently excludes Spring Security auto-configuration in `src/main/resources/application.properties`. If you deploy StoryTime publicly, enable and configure authentication/authorization appropriately.

## Contributing

See `CONTRIBUTING.md`.

## License

MIT License. See `LICENSE`.
