---
name: run-checks
description: Run AlertDesk's full pre-commit check (unit tests, then the CLI on the sample data) and report the results as a table. Use before committing, or when asked to verify, check or validate the project.
---

# Run checks

1. Run `mvn -q test`. If it fails, stop and show the failing tests.
2. Run `./scripts/run.sh`.
3. Report a table: Check | Result | Notes.
4. Point out anything suspicious in the CLI output.
