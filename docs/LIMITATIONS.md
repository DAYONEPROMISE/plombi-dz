# PlombiDZ — Limitations (honest list)

1. **Guidance, not engineering.** DTU 60.11 collective sizing needs
   per-branch Flamant head-loss verification; the app uses the velocity
   criterion + a long-run upsize rule of thumb. The UI states this.
2. **x ≤ 5 simplification.** No simultaneity reduction (y = 1) instead of
   the §2.1.2 coefficient method — conservative, documented.
3. **No hot-water loop / dilatation calc** (DTU 60.11 annex B lyres),
   no evacuation (Ø100/DTU 60.11 §3) — roadmap.
4. **Prices in DA are free text totals**, no TVA/facture numbering.
5. **PDF is basic** (framework PdfDocument, single page, Latin+Arabic
   shaping via the platform text stack) — no logo/photo support yet.
6. **No backup/sync by design** (offline-first promise); device loss =
   data loss. Export-all is roadmap.
7. **minSdk 26** (Android 8.0+) — excludes very old site phones.
8. Local Gradle build not run on the dev machine (no JDK/Android SDK
   there); correctness rests on unit-test math cross-checks + CI build.
