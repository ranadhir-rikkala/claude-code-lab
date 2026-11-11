# Step 11 — Capstone: ship a feature with everything

**Goal:** Add a real feature using every piece you built, then take the final quiz.

## The feature

Add a `--min-severity` option to the CLI:

```bash
./scripts/run.sh data/alerts.txt --min-severity WARN
```

This hides groups below WARN. Without the option, the output is unchanged.

## Workflow

| # | Do | Piece used |
|---|---|---|
| 1 | New session. **Shift+Tab** into plan mode. Ask for a plan. | Plan mode + AGENTS.md / CLAUDE.md rules |
| 2 | *"Use test-writer to write failing tests for --min-severity."* | Subagent |
| 3 | Leave plan mode and ask Claude to implement it | Hooks run tests after each Java edit |
| 4 | `/run-checks` | Skill |
| 5 | *"With only WARN and above, should anyone be paged?"* | Skill with files |
| 6 | `/code-review` | Built-in review |
| 7 | Commit locally (and don't push) | Your home CLAUDE.md rule |

Afterwards, write down which piece helped most and which one you'd change.

## Final quiz

Answer all 8 first, then open the hints and answers.

**1.** A rule must hold in every repo you own. File and path?
<details><summary>Answer</summary>`~/.claude/CLAUDE.md`</details>

**2.** A 3-page release procedure, used monthly. Where does it go?
<details><summary>Answer</summary>A skill, e.g. `.claude/skills/release/SKILL.md`. Not AGENTS.md or CLAUDE.md, which load on every request.</details>

**3.** Your skill never triggers automatically. What do you fix?
<details><summary>Answer</summary>The `description`: say what it does and when to use it, in the words users actually say.</details>

**4.** What is in `.claude/agents/`?
<details><summary>Answer</summary>Subagent definitions (a system prompt + tools + model per worker). Not rules.</details>

**5.** Cursor and Claude Code must share one rule. Where does it live, and how does Claude see it?
<details><summary>Answer</summary>In AGENTS.md. CLAUDE.md imports it with `@AGENTS.md` (unless your version loads it natively).</details>

**6.** "Never edit the sample data" must be impossible to break. What do you use?
<details><summary>Answer</summary>A `PreToolUse` hook that exits 2 (or a permission deny rule). Not a CLAUDE.md line, which is only a request.</details>

**7.** Which use context tokens: settings.json? `.mcp.json` text? MCP tools? A skill description? A skill body before use?
<details><summary>Answer</summary>settings.json: no. `.mcp.json` text: no. MCP tools: yes (unless deferred). Skill description: yes (small). Skill body before use: no.</details>

**8.** A subagent read 30 files. How much of that is in your main context?
<details><summary>Answer</summary>None of the reads. Only its final report comes back.</details>

## You're done when

- [ ] `--min-severity` works and is tested
- [ ] All tests pass and the case bug is fixed
- [ ] You can explain every file in `.claude/` without looking
- [ ] PROGRESS.md is filled in
