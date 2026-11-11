# Claude Code Lab

Learn how Claude Code gets project knowledge and rules — `AGENTS.md`, `CLAUDE.md`, settings, skills, subagents, hooks and MCP — by adding them **one at a time** to a small real project and watching what changes.

## The use case: AlertDesk

A small Java tool for an on-call engineer. It reads ops alerts from `data/alerts.txt`, groups duplicates, and ranks the groups by severity and count.

```
SERVICE    SEVERITY  COUNT  MESSAGE
payments   CRITICAL      2  DB timeout
inventory  CRITICAL      1  Disk 95% full
search     WARN          3  High latency on /query
...
12 alerts -> 8 groups
```

Why this use case:

| Reason | What it gives you |
|---|---|
| Small: 6 classes, 9 tests | You can hold the whole project in your head |
| Has a build, tests, a CLI and sample data | Every Claude Code feature gets a real job, not a toy one |
| Has a **planted bug** | Subagents, hooks and skills fix something real (find it in Step 00) |
| Ops domain | Close to agent remediation work |

## Roadmap

| Step | Lesson | You create | Its real job in AlertDesk |
|---|---|---|---|
| 00 | [Baseline](lessons/00-baseline.md) | nothing | See what Claude does with zero project context |
| 01 | [AGENTS.md](lessons/01-agents-md.md) | `AGENTS.md` | Commands, layout and rules for every tool and human |
| 02 | [CLAUDE.md + imports](lessons/02-claude-md.md) | `CLAUDE.md` | Claude-only rules; imports AGENTS.md |
| 03 | [Scopes](lessons/03-scopes.md) | `~/.claude/CLAUDE.md`, `data/CLAUDE.md` | Personal rules everywhere; data rules only near the data |
| 04 | [Settings & permissions](lessons/04-settings.md) | `.claude/settings.json`, `.claude/settings.local.json` | Run tests without prompts; block reading secrets |
| 05 | [First skill](lessons/05-first-skill.md) | `.claude/skills/run-checks/` | One procedure: test + run + report |
| 06 | [Skill descriptions](lessons/06-skill-descriptions.md) | edit the description | Prove the description decides auto-use |
| 07 | [Skill with files](lessons/07-skill-with-files.md) | `.claude/skills/triage-policy/` | Reference doc + script, loaded only when needed |
| 08 | [Subagents](lessons/08-subagents.md) | `.claude/agents/test-writer.md` | Write a failing test for the planted bug in a separate context |
| 09 | [Hooks](lessons/09-hooks.md) | hooks in `.claude/settings.json` | Tests run after every Java edit; sample data protected |
| 10 | [MCP](lessons/10-mcp.md) | `.mcp.json` | Give Claude a scoped tool server and measure its token cost |
| 11 | [Capstone](lessons/11-capstone.md) | a feature | Ship a change using everything, then the final quiz |

Track your progress in [PROGRESS.md](PROGRESS.md).

## How to work through each step

1. Read the lesson in `lessons/`.
2. **Type the file yourself.** Don't copy from `solutions/`. Writing it is how it sticks.
3. Start a **fresh** Claude session after any config change (`/exit`, then `claude`). Most files load only at session start.
4. Do the **Observe** steps and fill in the table.
5. Answer **Check yourself**. Open the *Hint* first, then the *Answer*.
6. Compare with `solutions/`, tick `PROGRESS.md`, and commit.

Tip: keep two terminals open — one for your editor and shell, one running `claude`.

## Why the solution files have odd names

Claude Code auto-loads files named exactly `CLAUDE.md`, and discovers `.claude/skills/` and `.claude/agents/`. If the solutions used those names and folders, they would load during your experiments and spoil them. So they are named `02-CLAUDE.md`, `05-run-checks.SKILL.md`, and so on. That is itself the first lesson: **for these files, the name and location are the API.**

## Run the project

```bash
mvn -q test
./scripts/run.sh
```

Needs Java 21+ and Maven. Step 10 also needs Node (`npx`).
