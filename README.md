# Technical Assessment: AI-Assisted Proposal Triage Design

**Candidate Name:** Vinod Kumar  
**Assessment Reference:** MATACS-TH-2026-02  
**Role:** Associate Software Developer (Architected with Senior Java Engineering Standards)  
**Time Actually Spent:** Approximately 55 minutes  

---

## 1. AI Tool Usage & Engineering Decision-Making

We utilized AI coding assistants (Claude and ChatGPT) during the design phase to accelerate JSON Schema structuring, draft prompt templates, and outline candidate edge cases. 

**Where I Disagreed with the AI & Corrected the Architecture:**  
When prompted for an end-to-end workflow, the AI initially recommended using the LLM directly to score each criterion and generate the final ranking list in a single multi-step prompt. I explicitly rejected and corrected this design. Relying on an LLM for numeric evaluation and ranking introduces non-deterministic calculation drift, hallucinations, and catastrophic vulnerability to adversarial prompt injection (as exemplified by Proposal D's injected procurement note). As a Senior Developer, I enforced a strict architectural separation of concerns: the AI is constrained strictly to extracting factual data into a strongly-typed schema with exact text citations, while all security sanitization, mandatory pass/fail gatekeeping, normalized scoring, and ranking calculations are executed by deterministic, auditable code.

---

## 2. Deliverables & Repository Structure

Every required file and folder is organized according to the candidate brief specification:

| File / Folder | Status | Description |
| :--- | :--- | :--- |
| **[`diagram/`](diagram/)** | **Required** | Contains the end-to-end process-flow diagram ([`process_flow.png`](diagram/process_flow.png) and [`process_flow.svg`](diagram/process_flow.svg)) illustrating AI steps, deterministic rule-based steps, and human checkpoints, alongside the [generator documentation](diagram/README.md). |
| **[`WRITEUP.md`](WRITEUP.md)** | **Required** | Complete technical answers to all five questions (AI vs. rules, consistency & explainability, human in the loop, changing criteria, and risk safeguards), strictly conforming to the 1,000-word limit (980 words). |
| **[`PROMPTS.md`](PROMPTS.md)** | **Required** | Production-ready AI extraction prompts, system instructions, anti-injection directives, and the complete JSON Schema definition for structured outputs. |
| **[`ranking.json`](ranking.json)** | **Required** | Complete expected triage ranking of Proposals A–E conforming strictly to the requested JSON format and diagnostic reason constraints. |
| **[`prototype/`](prototype/)** | **Optional (Extra Credit)** | Production-quality Java prototype ([`ProposalTriageEngine.java`](prototype/ProposalTriageEngine.java)) and Python prototype ([`triage.py`](prototype/triage.py)) executing extraction validation, gatekeeping, and scoring across the sample set. |
| **[`.env.example`](.env.example)** | **Required** | Environment configuration template ensuring zero API keys or credentials are ever committed. |

---

## 3. Proposal Triage Summary

Based on the admin-defined criteria for the **Commercial Solar Installation** project (150 kW rooftop system, $260,000 CAD budget ceiling, 16-week maximum delivery):

| Proposal | Status | Rank | Score | Summary Rationale |
| :--- | :--- | :--- | :--- | :--- |
| **Proposal A** | `ranked` | **1** | **98.8** | **Recommended Award:** Lowest cost ($238k), fastest schedule (12 wks), highest verified experience (14 commercial installs $\ge$100 kW), and complete team/monitoring plan. |
| **Proposal E** | `ranked` | **2** | **85.5** | **Runner-up:** Strong 13-week schedule, best workmanship warranty (12 yrs), and comprehensive technical approach with 24-month monitoring. |
| **Proposal C** | `ranked` | **3** | **77.8** | **Compliant:** Fully compliant with 9 installs and 14-week schedule, but highest price among eligible ($244k) and lacks safety/monitoring plans. |
| **Proposal B** | `ineligible` | *null* | *null* | **Disqualified:** Failed mandatory requirement; Ontario electrical contractor licence is not held by the bidder or stated in proposal (deferred to unconfirmed sub). |
| **Proposal D** | `flagged` | *null* | *null* | **Manual Review Required:** Flagged due to detected adversarial prompt injection attempt in Appendix C and failure to commit to a delivery schedule within 16 weeks. |

---

## 4. Running the Optional Prototype

The Java prototype requires zero external dependencies:

```bash
# Compile and run Java prototype
javac -d . prototype/ProposalTriageEngine.java
java prototype.ProposalTriageEngine
```

Or using PowerShell:
```powershell
.\prototype\run.ps1
```

Or using Python:
```bash
python prototype/triage.py
```
