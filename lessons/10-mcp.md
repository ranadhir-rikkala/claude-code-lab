# Step 10 — MCP: connect a tool server

**Goal:** Connect an MCP server, see the tools it adds, and measure their token cost.

## What MCP is

**Model Context Protocol** (Anthropic, Nov 2024) is an open protocol for connecting agents to tools and data. An **MCP server** exposes tools (plus resources and prompts). An **MCP client** like Claude Code, Claude Desktop or Cursor starts it (stdio) or connects to it (HTTP) and offers its tools to the model.

One server works in every MCP client. That's the point: write the integration once.

| Question | Answer |
|---|---|
| Does `.mcp.json`'s **text** use tokens? | No. It's config. |
| Do the server's **tools** use tokens? | **Yes.** Each tool's name, description and input schema go into context, unless your version defers them (tool search). |
| Team-shared servers | `.mcp.json` at the repo root, committed |
| Just-you servers | `claude mcp add …` (user or local scope) |

## Do

1. Run `/context` in a fresh session and note the tokens (baseline for this step).
2. Create `.mcp.json` at the repo root. It gives Claude a file server that can see **only** `data/`:
   ```json
   {
     "mcpServers": {
       "alert-files": {
         "command": "npx",
         "args": ["-y", "@modelcontextprotocol/server-filesystem", "./data"]
       }
     }
   }
   ```
3. New session. Claude Code asks you to **approve** the project MCP server. Read the prompt, then approve.
4. Run `/mcp`: status and tool list. Run `/context`: how many tokens do the MCP tools add?
5. Ask *"Using only the alert-files tools, list the files you can access."* It should see only `data/`.
6. Ask *"Using the alert-files tools, read ../pom.xml."* What happens, and why?

## Observe

| Record | Value |
|---|---|
| Tokens before / after adding the server | |
| Number of tools the server added | |
| Result of step 6 | |

## Check yourself

**1. Does a big `.mcp.json` use lots of tokens?**

<details><summary>Hint</summary>

Split it into the file text and the tools it brings.
</details>

<details><summary>Answer</summary>

The file text doesn't; it's config. The tools each server exposes do: names, descriptions and schemas go into context unless they're deferred. Many servers can add thousands of tokens.
</details>

**2. Why does Claude Code ask you to approve servers from a project's `.mcp.json`?**

<details><summary>Hint</summary>

What does `"command": "npx"` do on your machine, and who wrote this file in a repo you just cloned?
</details>

<details><summary>Answer</summary>

An MCP server is a program that runs on your machine. A cloned repo could ship a `.mcp.json` that runs anything, so you must trust it explicitly. It's the same supply-chain risk as running a stranger's script.
</details>

**3. Your teammates need the same server. Which file?**

<details><summary>Hint</summary>

Committed and at the repo root.
</details>

<details><summary>Answer</summary>

`.mcp.json` at the repo root, committed to git.
</details>
