---
name: triage-policy
description: Apply AlertDesk's on-call paging policy to decide which alert groups must page someone. Use when asked who to page, whether alerts are urgent, or to review severity rules.
---

# Triage policy

1. Get counts by running `bash .claude/skills/triage-policy/count-by-service.sh data/alerts.txt`. Never count by reading the file.
2. Read `policy.md` in this folder before deciding who to page.
3. Answer with a table: Service | Alerts | Highest severity | Page? | Rule applied
