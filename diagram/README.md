# Architecture Process Flow Diagram

**Reference:** MATACS-TH-2026-02  
**Artifact:** `diagram/process_flow.png`

---

## 1. Visual Legend & Demarcation

The process-flow diagram (`diagram/process_flow.png`) explicitly demarcates the operational boundaries across four distinct architectural pillars:

| Color / Badge | Operational Domain | Role in Triage Pipeline |
| :--- | :--- | :--- |
| **Purple / Violet** | **AI Step (Probabilistic)** | Confined exclusively to document parsing and factual attribute extraction into JSON Schema. The AI is forbidden from calculating scores or deciding ranks. |
| **Blue** | **Rule-Based (Deterministic Code)** | Input sanitization, injection screening, citation substring verification, mandatory pass/fail gatekeeping, normalized mathematical scoring, and ranking. |
| **Amber / Orange** | **Human Checkpoint (Governance)** | Admin dynamic criteria configuration, reviewer triage inspection, evidence snippet validation, exception overrides, and final contract award sign-off. |
| **Emerald Green** | **System of Record** | Tamper-evident, append-only procurement audit ledger preserving complete cryptographic and mathematical traces. |

---

## 2. End-to-End Workflow Stages

1. **Stage 1: Ingestion & Pre-Processing**
   - Reviewer uploads contractor PDF proposals and selects category (*Commercial Solar Installation*).
   - System computes SHA-256 cryptographic document hash.
   - Text and metadata extracted using Apache PDFBox / Tika.
   - Rule-based regex/heuristic security filter scans for adversarial override instructions (e.g. flagging Proposal D's fake pre-approval note).
   - Facilities Admin defines criteria, weights, and thresholds via admin screens (never hard-coded; stored in PostgreSQL with version ID).

2. **Stage 2: Prompting & AI Extraction**
   - Dynamic prompt synthesizer loads active `CriteriaVersion` (v1.0) and builds an extraction prompt.
   - Untrusted PDF text is strictly enclosed within `<proposal_document>` XML tags.
   - LLM extraction runs at `temperature: 0.0` with strict JSON schema enforcement.
   - Rule-based groundedness verifier verifies that extracted quotes exist as exact substrings of the raw source text.

3. **Stage 3: Deterministic Rules Engine**
   - **Mandatory Gatekeeper:** Checks Ontario electrical licence (held by bidder) and WSIB/$5M insurance.
     - *Proposal B:* Disqualified (`ineligible`) because electrical work is deferred to an unconfirmed subcontractor.
     - *Proposal D:* `flagged` due to prompt injection attempt and failure to commit to a delivery schedule within 16 weeks.
   - **Scoring Engine:** Deterministically computes weighted scores (Experience 30%, Price 25%, Timeline 15%, Warranty 15%, Team 15%).
   - **Triage Classifier:** Ranks compliant proposals (A: 98.8, E: 80.5, C: 77.8) and outputs explainable single-line diagnostic reasons.

4. **Stage 4: Human Review & Final Award**
   - Reviewer inspects triage dashboard with highlighted side-by-side PDF citations.
   - Reviewer confirms disqualifications, investigates flagged items, and may override scores with mandatory logged justification.
   - Reviewer formally executes the contract award.
   - Complete trace logged to immutable audit ledger.

---

## 3. Regenerating the Diagram

To re-compile and generate the diagram PNG:
```bash
javac -d . diagram/GenerateDiagram.java
java diagram.GenerateDiagram
```
