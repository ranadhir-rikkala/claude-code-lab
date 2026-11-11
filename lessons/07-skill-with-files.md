# Step 07 — A skill with supporting files

**Goal:** Build a skill that uses all three levels of progressive disclosure: description → body → a reference doc and a script that load only when needed.

## The use case

On-call engineers ask *"should anyone be paged?"* Answering needs:
- **counts per service**: exact arithmetic, so a **script** does it. LLMs miscount; tokens are not characters.
- **the paging policy**: a reference doc that is only needed for this question.

| Work type | Give it to | Why |
|---|---|---|
| Deterministic (count, sort, validate) | A script | Exact, cheap, repeatable |
| Judgement (apply a policy, explain) | The model | That's what it's good at |

## Do

1. Create the folder `.claude/skills/triage-policy/` with three files.

   `SKILL.md`:
   ```markdown
   ---
   name: triage-policy
   description: Apply AlertDesk's on-call paging policy to decide which alert groups must page someone. Use when asked who to page, whether alerts are urgent, or to review severity rules.
   ---

   # Triage policy

   1. Get counts by running `bash .claude/skills/triage-policy/count-by-service.sh data/alerts.txt`. Never count by reading the file.
   2. Read `policy.md` in this folder before deciding who to page.
   3. Answer with a table: Service | Alerts | Highest severity | Page? | Rule applied
   ```

   `policy.md`:
   ```markdown
   # Paging policy

   1. Any CRITICAL alert pages the service owner.
   2. WARN pages only if the same service has 3 or more WARN alerts.
   3. INFO never pages.
   ```

   `count-by-service.sh`:
   ```bash
   #!/usr/bin/env bash
   grep -v '^#' "$1" | grep -v '^$' | awk -F'|' '{ print $1 "|" $2 }' | sort | uniq -c | sort -rn
   ```
2. Test the script yourself: `bash .claude/skills/triage-policy/count-by-service.sh data/alerts.txt`
3. New session. Ask *"Should anyone be paged right now?"* Watch the order: skill loads → script runs → `policy.md` is read.
4. New session. Ask *"Explain what the Triage class does."* Was `policy.md` read? Was the skill used?

## Observe

| Record | Value |
|---|---|
| Order of actions in step 3 | |
| Who should be paged, per Claude? | |
| Was policy.md read in step 4? | |

## Check yourself

**1. Name the three levels of this skill, and when each one loads.**

<details><summary>Hint</summary>

Frontmatter, body, …
</details>

<details><summary>Answer</summary>

(1) `name` + `description`: always. (2) The SKILL.md body: when the paging question matches. (3) `policy.md` and the script: only when the body's steps call for them.
</details>

**2. Why use a script for counting instead of letting Claude count lines?**

<details><summary>Hint</summary>

Lesson 1: the model sees tokens, not characters or lines.
</details>

<details><summary>Answer</summary>

Counting is deterministic work. A script is exact and cheap, while the model can miscount and costs more tokens. Use the model for judgement, like applying the policy.
</details>

**3. Payments has CRITICAL alerts *and* the case bug. Can the bug change a paging decision?**

<details><summary>Hint</summary>

Rule 2 counts WARN alerts per service. What if WARN alerts are split across groups by case?
</details>

<details><summary>Answer</summary>

Yes. Any logic that counts *groups* would undercount, because one real problem looks like several small ones. Duplicates that split or merge wrongly change decisions. Step 08 fixes it.
</details>
