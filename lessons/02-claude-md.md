# Step 02 — CLAUDE.md and imports

**Goal:** Create `CLAUDE.md` that imports `AGENTS.md` and adds rules only Claude needs.

## How Claude Code loads it

At session start, Claude Code:

1. Reads `CLAUDE.md` in the current folder **and every parent folder**, plus `~/.claude/CLAUDE.md`.
2. Replaces every `@path` line with that file's contents (an **import**; imports can nest a few levels).
3. Adds the result to the conversation as a **context message**. It is *next to* the system prompt, not inside it. The system prompt is built inside the Claude Code program.

So `@AGENTS.md` gives you **one source of truth**: other tools read AGENTS.md directly, and Claude reads it through the import.

## Do

1. Create `CLAUDE.md` at the repo root:
   ```markdown
   # CLAUDE.md

   @AGENTS.md

   ## Claude-specific
   ```
   Under *Claude-specific*, add 2–3 rules of your own. Ideas:
   - Before editing code, state the plan in at most 3 bullets.
   - After any Java change, run `mvn -q test` and show the result.
   - Commit locally only. Never push.
2. Start a new session. Run `/memory`: are both CLAUDE.md and AGENTS.md there? Run `/context` and compare tokens with Steps 00 and 01.
3. Test a rule: ask *"Rename `Triage.rank` to `Triage.prioritize` everywhere."* Does Claude state a plan first and run the tests after?
4. **Experiment:** delete the `@AGENTS.md` line, start a new session and ask *"What is the test command here?"* Does it still know without exploring? Put the line back afterwards.
5. Undo the rename if you don't want to keep it (`git checkout src`).

## Observe

| Record | Value |
|---|---|
| Files in `/memory` | |
| Tokens at start (00 → 01 → 02) | |
| Did Claude follow your "plan first" rule? | |
| Without `@AGENTS.md`, did it know the test command? | |

## Check yourself

**1. Is the content of CLAUDE.md part of the system prompt?**

<details><summary>Hint</summary>

Where does the system prompt come from, and is it a file you can edit?
</details>

<details><summary>Answer</summary>

No. The system prompt is built inside the Claude Code program. CLAUDE.md content is added as a context message at the start of the conversation. The effect is similar (it's always there), but you can only change the system prompt with flags like `--append-system-prompt` or with output styles.
</details>

**2. Why import with `@AGENTS.md` instead of copying AGENTS.md's text into CLAUDE.md?**

<details><summary>Hint</summary>

Think about what happens 3 months later when the test command changes.
</details>

<details><summary>Answer</summary>

With a copy you have two sources of truth that drift apart: someone updates one and the other goes stale. The import keeps one file that every tool reads.
</details>

**3. You edit CLAUDE.md while a session is running. Does that session see the change?**

<details><summary>Hint</summary>

When is CLAUDE.md read?
</details>

<details><summary>Answer</summary>

It's read at session start, so a running session may keep the old copy. Start a new session to be sure. That's why every lesson says "start a fresh session".
</details>
