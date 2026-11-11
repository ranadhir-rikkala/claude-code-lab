# Step 08 — Subagents

**Goal:** Create a `test-writer` subagent and use it to write a failing test for the planted bug. Then fix the bug.

## What a subagent is

A subagent is a **separate worker** with:

| Has its own | Meaning |
|---|---|
| System prompt | The body of its `.md` file |
| Context window | It starts empty. Its file reads don't fill **your** context. |
| Tool list | Only the tools you give it |
| Model | Optionally a different (cheaper or faster) model |

The main agent sees only its `name` + `description` (listed in the Agent tool). It delegates a task, the subagent works, and **only the final report comes back**.

| | Skill | Subagent |
|---|---|---|
| Runs in | The main conversation | A separate context |
| Brings | Instructions | A whole worker |
| Best for | Procedures | Self-contained tasks with a short result: search, write tests, review |
| Downside | Uses main context when loaded | Starts with no context, so it needs a clear brief; you see only its summary |

**Locations:** `~/.claude/agents/` = just you, all projects. `.claude/agents/` = this repo, the team. These are **not** rule files, and not AGENTS.md.

## Do

1. Create `.claude/agents/test-writer.md` (or use `/agents` to create one interactively, then compare):
   ```markdown
   ---
   name: test-writer
   description: Writes a failing JUnit 5 test that reproduces a described AlertDesk bug, without fixing it. Use when a bug needs test-first coverage.
   tools: Read, Grep, Glob, Write, Bash
   model: sonnet
   ---

   You write tests for AlertDesk (Java 21, JUnit 5, Maven).
   Write the smallest test that reproduces the described bug.
   Put it in src/test/java/lab/alertdesk/.
   Never modify files under src/main/.
   Run `mvn -q test` and confirm the new test fails for the right reason.
   Report: test file, test name, and the failure message.
   ```
2. New session. Run `/context` and note the tokens.
3. Ask: *"In the run output, payments 'DB timeout', 'db timeout' and 'DB Timeout' are separate groups, but they're the same problem. Use the test-writer subagent to write a failing test first."*
4. When it finishes, run `/context` again. Did the subagent's file reads land in your context?
5. Now fix the bug in the main session: *"Make the test pass with the smallest change."* (Hint for you: normalise the message in the fingerprint.)
6. `mvn -q test` and `./scripts/run.sh`. How many groups now? Commit.

## Observe

| Record | Value |
|---|---|
| Tokens before / after delegation | |
| Test name the subagent wrote | |
| Groups before → after the fix | 8 → |

## Check yourself

**1. Where does the body of `test-writer.md` go when it runs?**

<details><summary>Hint</summary>

Into your conversation, or somewhere else?
</details>

<details><summary>Answer</summary>

It becomes the system prompt of a **new, separate** agent. It never enters your main conversation.
</details>

**2. The subagent read 10 files. Did they fill your main context window?**

<details><summary>Hint</summary>

What comes back to the main agent?
</details>

<details><summary>Answer</summary>

No. Only its final report came back. That's the main benefit: context isolation.
</details>

**3. The prompt says "never modify src/main/", but the subagent has the `Write` tool. Which is stronger, and how would you make it impossible?**

<details><summary>Hint</summary>

Requested vs enforced (Step 04).
</details>

<details><summary>Answer</summary>

The prompt is only a request; the tool list is enforced. With `Write` it *could* still overwrite main code. To enforce it: a permission deny rule, or a `PreToolUse` hook that blocks writes outside `src/test/` (Step 09).
</details>

**4. You want `test-writer` in **all** your projects, just for you. Full path?**

<details><summary>Hint</summary>

Just you + all projects → home folder.
</details>

<details><summary>Answer</summary>

`~/.claude/agents/test-writer.md`
</details>
