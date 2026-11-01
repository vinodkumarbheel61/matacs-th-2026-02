# AI Proposal Triage Design: Technical Write-Up

**Candidate:** Vinod Kumar | **Reference:** MATACS-TH-2026-02

---

### 1. AI vs. Rules: Architecture Boundaries and Rationale

The system establishes a strict boundary between **unstructured information extraction** (AI) and **validation, scoring, and ranking** (deterministic code).

* **Where AI is used (Extraction Layer):**
  * **Document Parsing:** Ingesting unstructured PDF proposals across varying contractor layouts.
  * **Attribute Extraction:** Extracting proposal attributes (licence numbers, insurance confirmations, commercial experience counts, pricing, delivery timeline, warranties, and team commitments) into a typed JSON schema.
  * **Verbatim Grounding:** Extracting verbatim source quotes (`exact_quote`) for every attribute to establish audit traceability.

* **Where Rules and Plain Code are used (Core Business & Security Engine):**
  * **Security Screening:** Pre-LLM heuristic scanning for adversarial instructions (e.g., prompt injections like Proposal D's fake pre-approval note).
  * **Syntactic & Groundedness Verification:** Verifying extracted quotes exist as exact substrings in raw source text, alongside regex format checks (e.g., ECRA/ESA licence formatting).
  * **Mandatory Gatekeeping:** Strict Boolean evaluation (Pass/Fail) for required credentials (e.g., rejecting Proposal B for lacking a bidder-held licence).
  * **Mathematical Scoring:** Calculating weighted scores using dynamic linear formulas configured by admins.
  * **Ranking & Tie-Breaking:** Deterministic sorting and assigning explicit triage states (`ranked`, `flagged`, `ineligible`).

* **Why the line is drawn here:**
  LLMs are probabilistic token predictors prone to calculation errors, non-deterministic drift, and instruction injection. Procurement evaluations demand legal defensibility, auditability, and reproducibility. Confining AI strictly to structured extraction while keeping all scoring logic in deterministic code guarantees 100% calculation reliability and insulates the ranking engine from adversarial prompt manipulation.

---

### 2. Consistency and Explainability

* **Repeatability (Deterministic Invariance):**
  * Scoring is calculated entirely by deterministic code. Scoring the same extracted data always yields identical results.
  * AI extraction uses `temperature = 0.0` with structured JSON schema output and greedy token decoding. Extracted JSON payloads are cryptographically hashed (`SHA-256`) and cached. Re-ranking identical proposals reuses the verified cached extraction, guaranteeing byte-for-byte identical output.
  * If re-extracted from scratch, schema validation and substring-verified citations prevent semantic drift.

* **Explainability for Reviewers:**
  * **Itemized Mathematical Scorecard:** Reviewers do not receive black-box ratings. Every proposal displays an exact breakdown: $\text{Total Score} = \sum (\text{Weight}_i \times \text{Normalized Score}_i)$.
  * **Side-by-Side Evidence Citations:** Clicking any score component opens the source document, highlighting the exact excerpt and page citation extracted by the model.
  * **Explicit Triage Badges:** Non-ranked submissions carry transparent diagnostic reasons: Proposal B is marked `ineligible` due to a failed mandatory licence requirement; Proposal D is marked `flagged` due to an uncommitted timeline and adversarial injection markers.

---

### 3. Human in the Loop and Audit Logging

The tool serves as an intelligent decision-support assistant; human experts retain absolute procurement authority.

* **Human Review & Decision Touchpoints:**
  1. **Admin Configuration:** Facilities admins define categories, mandatory pass/fail rules, scoring formulas, and weights.
  2. **Triage Reviewer Checkpoint:** Before ranking finalization, reviewers inspect the triage dashboard to:
     * Confirm legal disqualifications for `ineligible` submissions.
     * Investigate `flagged` proposals to resolve anomalies, missing data, or security alerts.
     * Validate citation snippets for top-ranked submissions.
  3. **Override Authority:** Reviewers can override any extracted attribute, eligibility status, or score, accompanied by a mandatory justification note.
  4. **Final Award Decision:** The human reviewer formally executes and logs the final contract award.

* **Audit Logging:**
  Every state transition is recorded in an append-only, tamper-evident audit ledger:
  * Raw document SHA-256 hash, upload timestamp, and submitting user ID.
  * Exact LLM prompt snapshot, raw response tokens, model version, and extracted JSON payload.
  * Criteria version ID applied during scoring.
  * Complete mathematical calculation traces.
  * Reviewer interaction logs: timestamps of reviews, field overrides, justification notes, and digital sign-offs.

---

### 4. Handling Changing Criteria

Project constraints and weights frequently evolve. The architecture decouples document extraction from criteria evaluation using **Criteria Versioning**:

* **Immutable Criteria Entities:** Admin modifications never overwrite existing criteria in-place. Each update creates a new immutable version (e.g., `v1.0` $\rightarrow$ `v1.1`), storing the author, timestamp, and change delta.
* **Decoupled Re-Evaluation:** Because extraction isolates raw factual attributes (cost, weeks, licence number, installs), changing weights or thresholds does not require slow or costly re-extraction through the LLM.
* **Historical Integrity vs. Re-Scoring:**
  * Historical scoring runs remain permanently pinned to their respective `criteria_version_id`, preserving legal audit integrity.
  * If an admin adjusts criteria mid-evaluation, the system notifies the reviewer and offers a one-click **"Recalculate Run"**.
  * The deterministic scoring engine re-evaluates cached extracted DTOs against the new version in milliseconds, generating a new versioned evaluation run alongside the historical record for comparison.

---

### 5. Risk Analysis and Practical Safeguards

| Risk | Description | System Safeguard |
| :--- | :--- | :--- |
| **Prompt Injection** | Adversarial instructions in PDFs (e.g., Proposal D claiming pre-approval). | Multi-tier defense: (1) Pre-LLM heuristic filter blocks override tokens. (2) Proposal text is isolated within XML delimiters (`<proposal_content>`). (3) System prompt enforces pure data extraction. (4) Plain code handles all ranking—injected prompt commands cannot alter scoring logic. |
| **Hallucinations** | LLM generating non-existent qualifications or dates. | Structured extraction requires a mandatory `verbatim_citation` for each field. A deterministic check confirms every citation is an exact substring of raw source text. Unverified items are flagged for human review. |
| **Extraction Errors** | Misinterpreting subcontracts (Proposal B) or vague schedules (Proposal D). | Strict schema validation with regex checks on licence formats. Subcontractor disclaimers or missing numerical commitments fail validation and automatically transition the proposal to `ineligible` or `flagged`. |
| **Algorithmic Bias** | Subjective scoring favouring particular writing styles or bidder profiles. | Subjective prose evaluation is eliminated. The scoring model computes strictly normalized values across objective quantitative criteria (price, timeline, verified project count, warranty years). |
| **Cost & Latency** | High token usage and slow processing times on bulk uploads. | High-efficiency models (e.g., GPT-4o-mini) with strict token limits; document hashing ensures identical uploads hit extraction cache with zero LLM API cost. |
