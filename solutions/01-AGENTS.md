# AGENTS.md

## Project
AlertDesk: reads ops alerts from a text file, groups duplicates, ranks groups by severity then count.

## Commands
- Tests: `mvn -q test`
- Run the CLI: `./scripts/run.sh [alerts-file]` (default `data/alerts.txt`)

## Layout
- `Alert`, `Severity`: one parsed alert line
- `AlertParser`: `service|SEVERITY|message` lines → alerts; skips `#` comments and blank lines
- `Deduper`: groups alerts by fingerprint (service + message)
- `AlertGroup`: first alert, count, highest severity
- `Triage`: ranks groups (severity desc, then count desc)
- `App`: CLI entry point

## Rules
- Test-first: write the failing test before the fix.
- Java 21. No new dependencies without asking.
- Use explicit lambdas, not method references.
- Declare variables with interface types (`List`, `Map`).
