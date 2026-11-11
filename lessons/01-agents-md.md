# Step 01 — AGENTS.md: one onboarding file for every tool

**Goal:** Write `AGENTS.md`, the tool-neutral onboarding file, and find out whether your Claude Code version reads it on its own.

## Why it exists

Each coding agent used to want its own file (`CLAUDE.md`, `.cursorrules`, `GEMINI.md`…). `AGENTS.md` (2025) is an open convention that Codex, Cursor, Copilot, Amp, Jules and many others read. You write it once, and every tool and every new human gets the same day-one knowledge.

**What belongs in it:** knowledge an agent **can't guess from the code**, or would waste tool calls finding.

| Put in AGENTS.md | Keep out |
|---|---|
| Exact commands: build, test, run | Things obvious from reading the code |
| Layout in 3–6 lines | Long procedures used rarely → a **skill** (Step 05) |
| Hard rules (test-first, no new dependencies) | Secrets, tokens, passwords |
| Gotchas that cost you time once | Your personal preferences → `~/.claude/CLAUDE.md` (Step 03) |

**Keep it short.** It's loaded on every request, so each line costs tokens and attention every time.

## Do

1. Create `AGENTS.md` at the repo root. Start from this skeleton and fill it in **yourself**:
   ```markdown
   # AGENTS.md

   ## Project

   ## Commands

   ## Layout

   ## Rules
   ```
   Ideas: the two commands from Step 00; the 6 classes and what each does; rules like test-first, Java 21, no new dependencies without asking, explicit lambdas (no method references).
2. Start a **new** `claude` session.
3. Run `/memory` and `/context`. Is `AGENTS.md` listed?
4. Ask the same question as Step 00: *"How do I run the tests and the app, and what are the coding rules?"* Count the tool calls again.

## Observe

| Record | Value |
|---|---|
| Is AGENTS.md listed in `/memory`? | |
| Tokens at start vs Step 00 | |
| Tool calls to answer vs Step 00 | |

**If AGENTS.md was not loaded:** your Claude Code version only auto-loads `CLAUDE.md`. Step 02 fixes that with one import line. **If it was loaded:** your version reads it natively. Step 02 still matters for Claude-only rules.

## Check yourself

**1. A teammate uses Cursor and you use Claude Code. Why put the test command in AGENTS.md rather than CLAUDE.md?**

<details><summary>Hint</summary>

Which of the two files does Cursor read?
</details>

<details><summary>Answer</summary>

Cursor (and most other agents) read AGENTS.md, not CLAUDE.md. Facts every tool needs go in AGENTS.md, and Claude Code gets them through an import (Step 02).
</details>

**2. You want to add a 2-page "how to cut a release" procedure that's used once a month. Does it go in AGENTS.md?**

<details><summary>Hint</summary>

When is AGENTS.md loaded, and how often is this procedure needed?
</details>

<details><summary>Answer</summary>

No. AGENTS.md loads on **every** request, so 2 pages used once a month would waste tokens and attention all month. Make it a **skill** (Step 05), which loads only when needed. *Always needed → AGENTS.md. Sometimes needed → skill.*
</details>

**3. "Keep it short": name the two costs of a long AGENTS.md.**

<details><summary>Hint</summary>

One is about money and speed. The other is about answer quality.
</details>

<details><summary>Answer</summary>

(1) **Tokens** on every request: cost and latency. (2) **Attention**: long context lowers quality ("lost in the middle", "context rot"), so important rules get followed less reliably.
</details>
