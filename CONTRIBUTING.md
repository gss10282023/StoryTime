# Contributing to StoryTime

Thanks for taking the time to contribute.

## Getting Started

### Prerequisites

- Java 17
- PostgreSQL 15+ (or Docker + Docker Compose)

### Run locally

1. Configure environment variables (see `.env.example`).
2. Start Postgres (Docker or local).
3. Run:

   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=local'
   ```

## Development Workflow

- Create a feature branch from `main`
- Keep changes focused and include tests when possible
- Run `./gradlew test` before opening a PR

## Code Style

- Java 17
- Prefer small, readable commits
- Follow `.editorconfig` for whitespace and line endings

## Reporting Issues

- Use GitHub Issues to report bugs or request features
- Include steps to reproduce, expected behavior, and logs/screenshots when relevant
