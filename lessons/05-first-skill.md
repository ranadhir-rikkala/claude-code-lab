# Step 05 — Your first skill

**Goal:** Package a procedure as a skill (`run-checks`), and see that its body loads only when it's used.

## How a skill loads: progressive disclosure

| Level | What | When it enters context | Rough cost |
|---|---|---|---|
| 1 | `name` + `description` (frontmatter) | Always, at session start | Tens of tokens |
| 2 | The SKILL.md body | When Claude decides to use the skill, or you type `/name` | The body's size |
| 3 | Extra files in the skill folder | Only if the body points to them **and** the task needs them | Only what's read |

That's why 20 skills cost almost nothing until used, while 20 procedures pasted into CLAUDE.md cost tokens on every request.

**Rule:** *Needed on almost every task → AGENTS.md / CLAUDE.md. Needed sometimes → skill.*

## Do

1. Create `.claude/skills/run-checks/SKILL.md`:
   ```markdown
   ---
   name: run-checks
   description: Run AlertDesk's full pre-commit check (unit tests, then the CLI on the sample data) and report the results as a table. Use before committing, or when asked to verify, check or validate the project.
   ---

   # Run checks

   1. Run `mvn -q test`. If it fails, stop and show the failing tests.
   2. Run `./scripts/run.sh`.
   3. Report a table: Check | Result | Notes.
   4. Point out anything suspicious in the CLI output.
   ```
2. New session. Ask *"What skills do you have?"* Is `run-checks` listed?
3. Run `/context` and note the token count.
4. Use it **explicitly**: type `/run-checks`.
5. Run `/context` again. What changed?
6. New session. Use it **implicitly**: *"I'm about to commit — make sure everything is fine."* Did Claude pick the skill by itself?

## Observe

| Record | Value |
|---|---|
| Tokens before using the skill | |
| Tokens after `/run-checks` | |
| Did implicit use work? | |
| What did step 4 of the skill flag in the CLI output? | |

## Check yourself

**1. Before you use the skill, which part of SKILL.md is in Claude's context?**

<details><summary>Hint</summary>

The two fields between the `---` lines.
</details>

<details><summary>Answer</summary>

Only `name` and `description`. The body (level 2) and any extra files (level 3) are not loaded yet.
</details>

**2. Why not put these 4 steps in AGENTS.md instead?**

<details><summary>Hint</summary>

How often is a pre-commit check needed, compared with how often AGENTS.md is loaded?
</details>

<details><summary>Answer</summary>

AGENTS.md loads on every request. The check is needed only before commits. As a skill it costs a one-line description until used.
</details>

**3. What did the skill flag as suspicious in the CLI output?**

<details><summary>Hint</summary>

Look at the `payments` rows.
</details>

<details><summary>Answer</summary>

"DB timeout", "db timeout" and "DB Timeout" are the same problem but show up as **3 groups**. The deduper compares messages case-sensitively. That's the planted bug, and you'll fix it test-first in Step 08.
</details>
