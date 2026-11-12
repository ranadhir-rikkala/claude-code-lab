## Project
Java CLI that groups and ranks ops alerts.

## Commands
Tests: mvn -q test
Run: ./scripts/run.sh

## Layout
Flow: AlertParser → Deduper → Triage → App


## Rules
- 4-space indentation; match existing code.
- Before editing code, state the plan in at most 3 bullets.
- After any Java change, run mvn -q test and show the result.