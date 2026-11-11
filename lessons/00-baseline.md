# Step 00 — Baseline: Claude with zero project context

**Goal:** See what Claude knows when the repo has no `AGENTS.md`, no `CLAUDE.md` and no `.claude/` folder, so every later step has a "before" to compare against.

## Why this step exists

Every file you add in later steps changes **what is in Claude's context window at session start**. You can only see that effect if you measure the starting point first.

## Do

1. Run the project yourself:
   ```bash
   cd ~/claude-code-lab
   mvn -q test
   ./scripts/run.sh
   ```
2. Look closely at the `payments` rows in the output. Is anything wrong? Write it down. (This is the planted bug.)
3. Start `claude` in this folder.
4. Run `/context`. Write down the total tokens used and the categories you see (system prompt, tools, memory files…).
5. Run `/memory`. Which memory or instruction files are listed?
6. Ask: *"How do I run the tests and the app in this repo, and what are the coding rules?"*
7. Watch what Claude does. Count the tool calls (`ls`, reading `pom.xml`, …) it needs before it can answer.
8. `/exit`

## Observe

| Record | Value |
|---|---|
| Tokens used at start (`/context`) | |
| Files listed by `/memory` | |
| Tool calls needed to answer step 6 | |
| Coding rules Claude reported | |
| The bug you saw in step 2 | |

## Check yourself

**1. Why did Claude have to run tools to answer a simple "how do I run the tests?"**

<details><summary>Hint</summary>

What was in its context window about this repo before you asked?
</details>

<details><summary>Answer</summary>

Nothing about this repo was in context. The model is stateless and knows only what is in the current request, so it had to explore with tools. That costs time and tokens on every new session. Coding rules that exist only in your head can't be found by exploring at all. Steps 01–02 fix this.
</details>

**2. Is your personal auto memory (the one that knows your profile) loaded in this repo? Check `/memory`.**

<details><summary>Hint</summary>

Look at the path of your memory folder: `~/.claude/projects/<something>/memory/`. What is `<something>`?
</details>

<details><summary>Answer</summary>

Auto memory is stored **per project folder**. Your existing memory lives under `~/.claude/projects/-Users-ranadhir/` (your home folder), and this repo gets its own folder, so Claude starts here without knowing you. That's why rules you want **everywhere** belong in `~/.claude/CLAUDE.md` (Step 03). If `/memory` shows otherwise in your version, write down what you saw.
</details>
