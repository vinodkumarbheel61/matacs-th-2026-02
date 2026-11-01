package diagram;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class GenerateDiagram {

    public static void main(String[] args) {
        int width = 2400;
        int height = 1500;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        // Enable high quality anti-aliasing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        // Canvas Background
        g.setColor(new Color(248, 250, 252));
        g.fillRect(0, 0, width, height);

        // Header Background
        g.setColor(new Color(15, 23, 42)); // Slate 900
        g.fillRect(0, 0, width, 120);

        // Fonts
        Font titleFont = new Font("SansSerif", Font.BOLD, 30);
        Font subTitleFont = new Font("SansSerif", Font.PLAIN, 15);
        Font sectionFont = new Font("SansSerif", Font.BOLD, 17);
        Font boxTitleFont = new Font("SansSerif", Font.BOLD, 14);
        Font boxBodyFont = new Font("SansSerif", Font.PLAIN, 12);
        Font badgeFont = new Font("SansSerif", Font.BOLD, 10);
        Font summaryTitleFont = new Font("SansSerif", Font.BOLD, 16);
        Font summaryBodyFont = new Font("SansSerif", Font.PLAIN, 12);

        // Header Text
        g.setFont(titleFont);
        g.setColor(Color.WHITE);
        g.drawString("AI-Assisted Proposal Triage Architecture", 60, 48);

        g.setFont(subTitleFont);
        g.setColor(new Color(148, 163, 184)); // Slate 400
        g.drawString("End-to-End Workflow: Document Ingestion, Guarded Extraction, Deterministic Rules & Human Audit Trail", 62, 78);
        g.setColor(new Color(203, 213, 225));
        g.drawString("Ref: MATACS-TH-2026-02  |  Candidate: Vinod Kumar", 62, 102);

        // Color Palettes
        // AI: Purple
        Color aiBg = new Color(250, 245, 255);
        Color aiBorder = new Color(168, 85, 247);
        Color aiBadge = new Color(126, 34, 206);
        Color aiText = new Color(88, 28, 135);

        // Rules: Blue
        Color ruleBg = new Color(239, 246, 255);
        Color ruleBorder = new Color(59, 130, 246);
        Color ruleBadge = new Color(29, 78, 216);
        Color ruleText = new Color(30, 58, 138);

        // Human: Amber/Orange
        Color humanBg = new Color(254, 243, 199);
        Color humanBorder = new Color(245, 158, 11);
        Color humanBadge = new Color(180, 83, 9);
        Color humanText = new Color(120, 53, 15);

        // Output/Storage: Emerald
        Color outBg = new Color(236, 253, 245);
        Color outBorder = new Color(16, 185, 129);
        Color outBadge = new Color(4, 120, 87);
        Color outText = new Color(6, 95, 70);

        // Legend on Top Right
        int legY = 48;
        drawBadge(g, 1500, legY, "AI STEP (Extraction Only)", aiBadge, Color.WHITE, badgeFont);
        drawBadge(g, 1750, legY, "RULE-BASED (Deterministic Code)", ruleBadge, Color.WHITE, badgeFont);
        drawBadge(g, 2040, legY, "HUMAN CHECKPOINT (Governance)", humanBadge, Color.WHITE, badgeFont);

        // Stage Column Titles
        g.setFont(sectionFont);
        g.setColor(new Color(30, 41, 59));
        int stageY = 160;
        g.drawString("STAGE 1: Ingestion & Pre-Processing", 60, stageY);
        g.drawString("STAGE 2: Prompting & AI Extraction", 620, stageY);
        g.drawString("STAGE 3: Deterministic Rules Engine", 1180, stageY);
        g.drawString("STAGE 4: Human Review & Final Award", 1740, stageY);

        // Stage 1 Boxes
        // Box 1.1: Upload (Human)
        drawBox(g, 60, 185, 500, 140, humanBg, humanBorder, "1. Proposal Upload & Category Select",
                new String[]{
                    "- Reviewer submits contractor PDF proposals (A-E)",
                    "- Selects Category: 'Commercial Solar Installation'",
                    "- System calculates SHA-256 hash of each document"
                }, humanText, "HUMAN ENTRY POINT", humanBadge, boxTitleFont, boxBodyFont, badgeFont);

        // Box 1.2: PDF Extraction (Rule)
        drawBox(g, 60, 350, 500, 140, ruleBg, ruleBorder, "2. Text Parsing & Normalization",
                new String[]{
                    "- Apache PDFBox / Tika extracts raw text & metadata",
                    "- Cleans whitespace & encodes character offsets",
                    "- Extracts document structural page boundaries"
                }, ruleText, "RULE-BASED", ruleBadge, boxTitleFont, boxBodyFont, badgeFont);

        // Box 1.3: Injection Filter (Rule)
        drawBox(g, 60, 515, 500, 155, ruleBg, ruleBorder, "3. Adversarial Prompt Injection Filter",
                new String[]{
                    "- Heuristic scanner checks for override tokens",
                    "- Detects: 'pre-approved', 'assign full marks'",
                    "- Proposal D triggered -> early risk flag set",
                    "- Wraps untrusted text in strict XML delimiters"
                }, ruleText, "RULE-BASED (SECURITY)", ruleBadge, boxTitleFont, boxBodyFont, badgeFont);

        // Box 1.4: Dynamic Criteria Admin (Human)
        drawBox(g, 60, 695, 500, 155, humanBg, humanBorder, "0. Dynamic Criteria Admin Screen",
                new String[]{
                    "- Admin configures Pass/Fail rules & weights via UI",
                    "- Exp: 30%, Price: 25%, Timeline: 15%, Warranty: 15%, Team: 15%",
                    "- Never hard-coded; stored in PostgreSQL",
                    "- Generates immutable CriteriaVersion (e.g. v1.0)"
                }, humanText, "HUMAN CONFIGURATION", humanBadge, boxTitleFont, boxBodyFont, badgeFont);

        // Stage 2 Boxes
        // Box 2.1: Dynamic Prompt Assembly (Rule)
        drawBox(g, 620, 185, 500, 155, ruleBg, ruleBorder, "4. Dynamic Prompt & Schema Synthesizer",
                new String[]{
                    "- Injects active CriteriaSet (v1.0) into extraction prompt",
                    "- Untrusted PDF text isolated in <proposal_document>",
                    "- Enforces system prompt: data extraction only, no scoring",
                    "- Configures strict JSON Schema for structured output"
                }, ruleText, "RULE-BASED", ruleBadge, boxTitleFont, boxBodyFont, badgeFont);

        // Box 2.2: LLM Extraction (AI)
        drawBox(g, 620, 365, 500, 195, aiBg, aiBorder, "5. LLM Factual Extraction (temp = 0.0)",
                new String[]{
                    "- Enterprise LLM (GPT-4o-mini / Gemini Flash)",
                    "- Extracts: Licence, WSIB, Insurance, Cost, Weeks, Warranties",
                    "- Extracts exact verbatim text quotes for every fact",
                    "- Refuses instruction overrides from proposal body",
                    "- AI NEVER calculates scores or decides rankings"
                }, aiText, "AI STEP (PROBABILISTIC)", aiBadge, boxTitleFont, boxBodyFont, badgeFont);

        // Box 2.3: Citation Substring Verifier (Rule)
        drawBox(g, 620, 585, 500, 160, ruleBg, ruleBorder, "6. Groundedness & Substring Verifier",
                new String[]{
                    "- Deterministic check: asserts rawText.contains(exact_quote)",
                    "- Verifies data types (price > 0, numeric weeks)",
                    "- Hallucinated / unverified claims flagged immediately",
                    "- Caches verified extraction JSON by doc hash"
                }, ruleText, "RULE-BASED (INTEGRITY)", ruleBadge, boxTitleFont, boxBodyFont, badgeFont);

        // Stage 3 Boxes
        // Box 3.1: Mandatory Gatekeeper (Rule)
        drawBox(g, 1180, 185, 500, 190, ruleBg, ruleBorder, "7. Mandatory Pass/Fail Gatekeeper",
                new String[]{
                    "- Checks legal prerequisites:",
                    "  * Ontario Electrical Contractor Licence (held by bidder)",
                    "  * WSIB clearance & $5M liability insurance",
                    "- Proposal B: Licence not held by bidder -> INELIGIBLE",
                    "- Proposal D: Adversarial injection & uncommitted timeline -> FLAGGED",
                    "- Eligible proposals (A, E, C) advance to scoring"
                }, ruleText, "RULE-BASED GATEKEEPER", ruleBadge, boxTitleFont, boxBodyFont, badgeFont);

        // Box 3.2: Deterministic Scoring (Rule)
        drawBox(g, 1180, 400, 500, 205, ruleBg, ruleBorder, "8. Deterministic Scoring & Normalization",
                new String[]{
                    "- Pure Java math engine (100% reproducible):",
                    "  * Experience (30%): Linear scale vs benchmark",
                    "  * Price (25%): Normalized within $260k budget ceiling",
                    "  * Timeline (15%): Delivery duration within 16-wk ceiling",
                    "  * Warranty (15%): Panel (7.5%) + Workmanship (7.5%)",
                    "  * Team & Approach (15%): Lead, Crew, Safety & Monitoring"
                }, ruleText, "RULE-BASED ENGINE", ruleBadge, boxTitleFont, boxBodyFont, badgeFont);

        // Box 3.3: Triage Classifier & Explanation Generator (Rule)
        drawBox(g, 1180, 630, 500, 175, ruleBg, ruleBorder, "9. Triage Classifier & Explanations",
                new String[]{
                    "- Sorts eligible proposals by total weighted score",
                    "- Proposal A (98.8) -> Rank 1 | Proposal E (85.5) -> Rank 2 | Proposal C (77.8) -> Rank 3",
                    "- Generates single-line explainable diagnostic reasons",
                    "- Emits structured ranking.json format"
                }, ruleText, "RULE-BASED", ruleBadge, boxTitleFont, boxBodyFont, badgeFont);

        // Stage 4 Boxes
        // Box 4.1: Reviewer Inspection (Human)
        drawBox(g, 1740, 185, 580, 210, humanBg, humanBorder, "10. Reviewer Triage Inspection Dashboard",
                new String[]{
                    "- Displays interactive leaderboard with diagnostic reasons",
                    "- Highlights non-compliant proposals:",
                    "  * Proposal B (Ineligible): Failed mandatory licence check",
                    "  * Proposal D (Flagged): Injected pre-approval note & uncommitted schedule",
                    "- Side-by-side view displays PDF with highlighted quotes",
                    "- Reviewer verifies evidence before proceeding"
                }, humanText, "HUMAN REVIEW POINT", humanBadge, boxTitleFont, boxBodyFont, badgeFont);

        // Box 4.2: Human Override & Award (Human)
        drawBox(g, 1740, 420, 580, 180, humanBg, humanBorder, "11. Human Override & Award Decision",
                new String[]{
                    "- Reviewer may override any extracted field, score or status",
                    "- System enforces mandatory text justification for any override",
                    "- System displays: 'Recommended: Award Proposal A'",
                    "- Human makes the final binding contract award decision",
                    "- AI is decision support; HUMAN retains authority"
                }, humanText, "FINAL DECISION MAKER", humanBadge, boxTitleFont, boxBodyFont, badgeFont);

        // Box 4.3: Immutable Audit Ledger (Storage)
        drawBox(g, 1740, 625, 580, 180, outBg, outBorder, "12. Immutable Procurement Audit Ledger",
                new String[]{
                    "- Append-only relational audit table (PostgreSQL)",
                    "- Records: SHA-256 hash, prompt snapshots, raw LLM tokens,",
                    "  criteria_version_id, score breakdowns, override notes, timestamps",
                    "- Provides 100% legal defensibility and audit compliance"
                }, outText, "SYSTEM OF RECORD", outBadge, boxTitleFont, boxBodyFont, badgeFont);

        // Connectors / Arrows
        g.setColor(new Color(100, 116, 139));
        g.setStroke(new BasicStroke(2.0f));

        // 1.1 -> 1.2
        drawArrow(g, 310, 325, 310, 350);
        // 1.2 -> 1.3
        drawArrow(g, 310, 490, 310, 515);
        // 1.3 -> 2.1
        drawHorizontalRouting(g, 560, 592, 590, 262, 620);
        // 1.4 -> 2.1
        drawHorizontalRouting(g, 560, 772, 600, 275, 620);
        // 2.1 -> 2.2
        drawArrow(g, 870, 340, 870, 365);
        // 2.2 -> 2.3
        drawArrow(g, 870, 560, 870, 585);
        // 2.3 -> 3.1
        drawHorizontalRouting(g, 1120, 665, 1150, 280, 1180);
        // 3.1 -> 3.2
        drawArrow(g, 1430, 375, 1430, 400);
        // 3.2 -> 3.3
        drawArrow(g, 1430, 605, 1430, 630);
        // 3.3 -> 4.1
        drawHorizontalRouting(g, 1680, 717, 1710, 290, 1740);
        // 4.1 -> 4.2
        drawArrow(g, 2030, 395, 2030, 420);
        // 4.2 -> 4.3
        drawArrow(g, 2030, 600, 2030, 625);

        // Bottom Summary Panel
        int summaryY = 890;
        int summaryH = 550;
        drawRoundedRect(g, 60, summaryY, 2260, summaryH, 16, Color.WHITE, new Color(203, 213, 225), 2.0f);

        g.setFont(summaryTitleFont);
        g.setColor(new Color(15, 23, 42));
        g.drawString("System Evaluation & Ranking Breakdown for Sample Proposal Set (Commercial Solar Installation)", 90, summaryY + 40);

        g.setFont(summaryBodyFont);
        g.setColor(new Color(71, 85, 105));

        int textY = summaryY + 75;
        int lineGap = 22;

        String[] summaryLines = new String[]{
            "• SEPARATION OF CONCERNS: The AI strictly extracts raw facts and citations into JSON. Deterministic Java code calculates all mathematical scores,",
            "  verifies mandatory thresholds, and establishes ranking order. This guarantees 100% repeatability, zero calculation drift, and injection resilience.",
            "",
            "• PROPOSAL EVALUATION RESULTS:",
            "  1. Proposal A (Northshore Solar Ltd.) - [STATUS: ranked | RANK: 1 | SCORE: 98.8]",
            "     - Pass mandatory licence (ECRA/ESA #7001234) & WSIB/$5M insurance. Lowest price ($238k), fastest schedule (12 wks), top verified experience",
            "       (14 commercial installs >=100kW in 5 yrs), 25y/10y warranties, full team (PM, 6 certified crew, safety plan, 12m monitoring).",
            "  2. Proposal E (SunGrid Contractors Inc.) - [STATUS: ranked | RANK: 2 | SCORE: 85.5]",
            "     - Pass mandatory licence (ECRA/ESA #7003456) & WSIB/$5M insurance. Price: $242.5k (better than C), timeline: 13 wks, 8 installs. Best workmanship",
            "       warranty (12 yrs vs 10 yrs), full approach with roof load assessment and 24-month monitoring.",
            "  3. Proposal C (Lakeview Renewables Corp.) - [STATUS: ranked | RANK: 3 | SCORE: 77.8]",
            "     - Pass mandatory licence (ECRA/ESA #7005678) & WSIB/$5M insurance. 9 installs, 14-wk schedule. Highest price among eligible ($244k), lacks site",
            "       safety plan and monitoring plan (weekly progress reports only).",
            "  4. Proposal B (BrightPath Energy Inc.) - [STATUS: ineligible | RANK: null | SCORE: null]",
            "     - FAILS MANDATORY CRITERION: Ontario electrical contractor licence is NOT held by bidder or stated in proposal (deferred to unconfirmed subcontractor).",
            "       Despite lowest headline price ($189.5k), the rules engine automatically disqualifies non-compliant bidders before scoring.",
            "  5. Proposal D (Essex Green Power Inc.) - [STATUS: flagged | RANK: null | SCORE: null]",
            "     - FLAGGED FOR MANUAL REVIEW: Adversarial prompt injection detected in Appendix C claiming pre-approval. Additionally failed to commit to a",
            "       delivery schedule within the required 16-week ceiling ('schedule will be determined after detailed site assessment').",
            "",
            "• DYNAMIC CRITERIA EVOLUTION: When an admin updates criteria weights or rules, a new immutable version (v1.1) is committed. Cached extraction DTOs",
            "  are recalculated deterministically in milliseconds without re-invoking the LLM, preserving historical audit integrity while enabling rapid agile iteration."
        };

        for (String line : summaryLines) {
            if (line.startsWith("  1.") || line.startsWith("  2.") || line.startsWith("  3.")) {
                g.setFont(new Font("SansSerif", Font.BOLD, 12));
                g.setColor(new Color(6, 95, 70));
            } else if (line.startsWith("  4.")) {
                g.setFont(new Font("SansSerif", Font.BOLD, 12));
                g.setColor(new Color(185, 28, 28));
            } else if (line.startsWith("  5.")) {
                g.setFont(new Font("SansSerif", Font.BOLD, 12));
                g.setColor(new Color(180, 83, 9));
            } else if (line.startsWith("• ")) {
                g.setFont(new Font("SansSerif", Font.BOLD, 13));
                g.setColor(new Color(15, 23, 42));
            } else {
                g.setFont(summaryBodyFont);
                g.setColor(new Color(71, 85, 105));
            }
            g.drawString(line, 90, textY);
            textY += lineGap;
        }

        g.dispose();

        // Save Image to file
        try {
            File outDir = new File("D:/MATACS/diagram");
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            File outFile = new File(outDir, "process_flow.png");
            ImageIO.write(img, "png", outFile);
            System.out.println("Process flow diagram successfully generated at: " + outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void drawBox(Graphics2D g, int x, int y, int w, int h, Color bg, Color border,
                                String title, String[] bullets, Color titleColor,
                                String badgeText, Color badgeBg, Font titleFont, Font bodyFont, Font badgeFont) {
        drawRoundedRect(g, x, y, w, h, 14, bg, border, 1.8f);

        drawBadge(g, x + 16, y + 14, badgeText, badgeBg, Color.WHITE, badgeFont);

        g.setFont(titleFont);
        g.setColor(titleColor);
        g.drawString(title, x + 16, y + 54);

        g.setFont(bodyFont);
        g.setColor(new Color(51, 65, 85));
        int by = y + 78;
        for (String bullet : bullets) {
            g.drawString(bullet, x + 16, by);
            by += 20;
        }
    }

    private static void drawBadge(Graphics2D g, int x, int y, String text, Color bg, Color textCol, Font font) {
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int tw = fm.stringWidth(text);
        int bw = tw + 18;
        int bh = 22;

        drawRoundedRect(g, x, y, bw, bh, 8, bg, bg, 1.0f);

        g.setColor(textCol);
        g.drawString(text, x + 9, y + 15);
    }

    private static void drawRoundedRect(Graphics2D g, int x, int y, int w, int h, int r, Color bg, Color border, float stroke) {
        RoundRectangle2D rect = new RoundRectangle2D.Float(x, y, w, h, r, r);
        g.setColor(bg);
        g.fill(rect);
        g.setColor(border);
        g.setStroke(new BasicStroke(stroke));
        g.draw(rect);
    }

    private static void drawArrow(Graphics2D g, int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
        int arrowSize = 6;
        if (x1 == x2) {
            int sign = (y2 >= y1) ? 1 : -1;
            int[] xp = new int[]{x2 - arrowSize, x2 + arrowSize, x2};
            int[] yp = new int[]{y2 - (sign * arrowSize), y2 - (sign * arrowSize), y2};
            g.fillPolygon(xp, yp, 3);
        } else if (y1 == y2) {
            int sign = (x2 >= x1) ? 1 : -1;
            int[] xp = new int[]{x2 - (sign * arrowSize), x2 - (sign * arrowSize), x2};
            int[] yp = new int[]{y2 - arrowSize, y2 + arrowSize, y2};
            g.fillPolygon(xp, yp, 3);
        }
    }

    private static void drawHorizontalRouting(Graphics2D g, int x1, int y1, int midX, int y2, int x2) {
        g.drawLine(x1, y1, midX, y1);
        g.drawLine(midX, y1, midX, y2);
        g.drawLine(midX, y2, x2, y2);
        int arrowSize = 6;
        int[] xp = new int[]{x2 - arrowSize, x2 - arrowSize, x2};
        int[] yp = new int[]{y2 - arrowSize, y2 + arrowSize, y2};
        g.fillPolygon(xp, yp, 3);
    }
}
