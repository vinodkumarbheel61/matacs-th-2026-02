# Prototype: Proposal Triage Rules & Scoring Engine

**Reference:** MATACS-TH-2026-02  
**Candidate:** Vinod Kumar  
**Scope:** Optional Prototype / Extra Credit

---

## 1. Overview

This prototype demonstrates the enterprise separation of concerns implemented for the MATACS Proposal Triage Tool:
1. **Security Pre-Scanner:** Scans document text for adversarial prompt injection attempts (flags Proposal D's injected pre-approval claim).
2. **Schema-Validated Extraction DTO:** Strongly-typed representation of factual proposal data with verifiable source citations.
3. **Mandatory Criteria Gatekeeper:** Enforces pass/fail rules (Ontario licence, WSIB, insurance) before any scoring occurs. Correctly marks Proposal B as `ineligible` because the bidder does not hold the required electrical licence.
4. **Deterministic Scoring Engine:** Calculates normalized weighted scores across dynamic criteria (Experience 30%, Price 25%, Timeline 15%, Warranty 15%, Team 15%).
5. **Explainable Triage Output:** Emits the exact schema and diagnostic reasons required for `ranking.json`.

---

## 2. Running the Java Prototype

The Java prototype requires **zero external dependencies** and is compatible with Java 8 through 21.

### Direct Execution:
```bash
# Compile and run
javac -d . prototype/ProposalTriageEngine.java
java prototype.ProposalTriageEngine
```

### PowerShell Helper:
```powershell
.\prototype\run.ps1
```

---

## 3. Running the Python Prototype

```bash
python prototype/triage.py
```

---

## 4. Expected Output

```json
[
  {
    "proposal": "A",
    "rank": 1,
    "status": "ranked",
    "score": 98.8,
    "reason": "Top rank: lowest price ($238k), fastest schedule (12 wks), highest verified experience (14 installs), and full team/monitoring plan."
  },
  {
    "proposal": "E",
    "rank": 2,
    "status": "ranked",
    "score": 85.5,
    "reason": "Rank 2: Strong 13-week schedule, best workmanship warranty (12 yrs), and comprehensive technical approach with 24-month monitoring."
  },
  {
    "proposal": "C",
    "rank": 3,
    "status": "ranked",
    "score": 77.8,
    "reason": "Rank 3: Fully compliant with 9 installs and 14-week schedule, but highest price ($244k) and lacks safety and monitoring plans."
  },
  {
    "proposal": "B",
    "rank": null,
    "status": "ineligible",
    "score": null,
    "reason": "Ineligible: Failed mandatory requirement; Ontario electrical contractor licence not held by bidder or stated in proposal."
  },
  {
    "proposal": "D",
    "rank": null,
    "status": "flagged",
    "score": null,
    "reason": "Flagged: Adversarial prompt injection detected in text and failed to commit to a completion timeline within 16 weeks."
  }
]
```
