# Progress

All 12 steps done on 2026-11-11 with Claude Code 2.1.322 (Opus 5.5). Quiz scores count first tries; "—" means it wasn't scored, and "given" means the answers were shown.

| Step | Lesson | Done | Quiz score | Notes |
|---|---|---|---|---|
| 00 | Baseline | [x] | — | Start tokens: 39.9k. With no AGENTS.md, Claude searched the repo to answer. |
| 01 | AGENTS.md | [x] | — | Loaded natively? Only as a fallback when there is **no** CLAUDE.md. |
| 02 | CLAUDE.md + imports | [x] | — | `@AGENTS.md` needed. Counted once: CLAUDE.md 16 + AGENTS.md 75 tokens. The typo `@AGENETS.md` failed silently. |
| 03 | Scopes | [x] | — | `data/CLAUDE.md` loads lazily when a data file is read, and is counted under **Messages**, not Memory files. |
| 04 | Settings & permissions | [x] | — | `Read(./secrets/**)` blocked the Read tool, but Bash `cat` could still read the file. Rules match **tools**, not files. |
| 05 | First skill | [x] | given | 41k before, 44k after `/run-checks` (mostly tool output). Implicit use worked. |
| 06 | Skill descriptions | [x] | 3/3 | A not used, B used (too broad), C used, D not used. Only the description changed. |
| 07 | Skill with files | [x] | 3/3 | One Bash ran the script and read policy.md. Paged: payments, inventory, search. In a new session, Triage questions didn't load policy.md. |
| 08 | Subagents | [x] | 4/4 | test-writer ran 9 calls on Sonnet, and only its report came back. Test `groupsMessagesDifferingOnlyInCase`. Groups 8 → 6. Main context 30k → 47k because the main agent fixed the bug in the same turn. |
| 09 | Hooks | [x] | 2/4 | The PreToolUse hook missed `echo >>` until the matcher was `Edit\|Write\|Bash`. Your own `!` commands skip hooks. PostToolUse reported test failures. |
| 10 | MCP | [x] | given | MCP tool tokens: ~0 up front (30k → 30k). 16 tools, deferred and loaded via ToolSearch. `../pom.xml` was denied by the server. `append_file` got past the hook, so the 6 write tools are now denied. |
| 11 | Capstone | [x] | see below | `--min-severity` shipped in commit `2d0c6f9`: 23 tests pass, and the default output is unchanged. Plan mode run checked: read-only, no files changed. |

## Capstone checklist

- [x] `--min-severity` works and is tested (AppTest 9, TriageTest +4)
- [x] All tests pass (23), and the case bug is fixed (`toLowerCase(Locale.ROOT)`)
- [x] Edge cases checked: no flag = INFO, the flag before or after the file, lowercase `warn`, a missing value, `bogus` and `--bogus` exit 2 with usage
- [x] Pieces used: test-writer subagent, PostToolUse hook (caught 2 failures mid-change), `/run-checks`, `triage-policy`, `/code-review`, alert-files MCP, plan mode
- [x] Committed locally, not pushed, with no AI attribution
- [ ] Config files (`AGENTS.md`, `CLAUDE.md`, `.claude/`, `.mcp.json`, `data/CLAUDE.md`) are still uncommitted

**Helped most:** the hooks. They blocked sample-data edits every time and caught failing tests without being asked.
**Would change:** run the lab in **default** mode, not auto. Auto mode silently allowed Writes, `echo >>` and MCP `append_file`, which hid the prompts that each lesson is about.

## Every file in `.claude/`, in one line each

| File | Read by | Job |
|---|---|---|
| `settings.json` | Claude Code | Team rules: allow tests and run, deny secrets and the MCP write tools, register both hooks |
| `settings.local.json` | Claude Code | Your rules only (gitignored): allow git status and diff, approve the alert-files MCP server |
| `hooks/protect-sample-data.sh` | Claude Code | PreToolUse: exit 2 if Edit, Write or Bash targets `data/alerts.txt` |
| `hooks/test-after-java-edit.sh` | Claude Code | PostToolUse: run `mvn -q test` after a `.java` edit and report failures |
| `skills/run-checks/SKILL.md` | Model | Pre-commit check: tests, run, table |
| `skills/triage-policy/` | Model | Paging decision: script counts, `policy.md` rules |
| `agents/test-writer.md` | Model (as a subagent) | Writes failing tests in its own context, on Sonnet |

## Final quiz answers

| # | Answer |
|---|---|
| 1 | `~/.claude/CLAUDE.md` |
| 2 | A skill, e.g. `.claude/skills/release/SKILL.md` |
| 3 | The `description`: what it does + when to use it |
| 4 | Subagent definitions, not rules |
| 5 | In AGENTS.md; CLAUDE.md imports it with `@AGENTS.md` |
| 6 | A PreToolUse hook (exit 2) or a deny rule, whose matcher covers **every** tool that can write: Edit, Write, Bash, MCP |
| 7 | No tokens: settings.json, `.mcp.json` text, a skill body before use. Tokens: a skill description, and MCP tools (small when deferred) |
| 8 | None of the reads, only the subagent's final report |

## Weak spots to revisit

- [x] Sometimes-needed procedure → **skill**, not AGENTS.md / CLAUDE.md (answered right in Step 07)
- [x] A skill is used automatically only if its **description** matches (proved in Step 06)
- [x] `.claude/agents/` = **subagents**, not rules (4/4 in Step 08)
- [ ] Rules for all your projects → **`~/.claude/CLAUDE.md`**
- [ ] Hook vs `.md` rule: the hook is **enforced** by the program, the `.md` rule is a **request**
- [ ] Only **PreToolUse** can block. PostToolUse runs after the tool and can only report
- [ ] Every new tool (Bash, MCP) is a new way in: deny rules and hook matchers must cover it
