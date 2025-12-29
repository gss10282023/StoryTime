# Releasing StoryTime

This document describes the recommended process for cutting a release.

## Checklist

1. Ensure your working tree is clean and all changes are merged.
2. Update `CHANGELOG.md`:
   - Move items from **Unreleased** into a new version section.
   - Double-check links/screenshots referenced in release notes (see `docs/assets/`).
3. Update the project version:
   - `build.gradle` → `version = 'X.Y.Z'`
4. Run tests locally:
   - `./gradlew test`
5. Commit the release changes:
   - `git add CHANGELOG.md build.gradle`
   - `git commit -m "chore(release): vX.Y.Z"`
6. Create an annotated tag:
   - `git tag -a vX.Y.Z -m "vX.Y.Z"`
7. Push:
   - `git push origin main`
   - `git push origin vX.Y.Z`
8. Create a GitHub Release:
   - Title: `vX.Y.Z`
   - Include: Demo GIF, screenshots, Quick Start snippet (see `README.md`).

## Optional: Public demo deployment (P2)

If you deploy a public instance:

- Prefer deploying the `demo` profile to avoid external AI costs and to prevent sending user prompts to third parties.
- Add a visible disclaimer: do not enter sensitive information.
- Use a disposable database and define a data retention/cleanup policy.
