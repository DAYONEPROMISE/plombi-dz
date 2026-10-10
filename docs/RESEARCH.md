# PlombiDZ — Research notes (verified 2026-10-10)

## 1. Normative basis: NF DTU 60.11 (P 40-202, Oct 1988)

Still the reference calculation rule for sanitary plumbing in France and,
in practice, for francophone North-African work. Sources consulted:

- DTU 60.11 excerpt (plombiers-reunis.com PDF): §2.1 base flows + minimum
  bores; §2.1.2 individual installations; §2.1.3 Flamant for collectives;
  §2.2 simultaneity incl. flush-valve counting rules.
- Ginger/CSTB-style worked example (over-view.fr): Table 3 base flows,
  y-formula, velocity targets **V = 2 m/s in basements, 1.5 m/s in columns**,
  D from Qp and V.
- Delabie technical support page: confirms y = 0.8/√(x−1) presentation.

### Verified Table 1 values used in the app

| Fixture | Qbase (L/s) | Min bore (mm) |
|---|---|---|
| Évier | 0.20 | 12 |
| Lavabo | 0.20 | 10 |
| Douche | 0.20 | 12 |
| Baignoire | 0.33 | 13 |
| WC avec réservoir | 0.12 | 10 |
| Machine à laver | 0.20 | (10, app default) |
| Poste d'eau 1/2 | 0.33 | 12 |
| Poste d'eau 3/4 | 0.42 | 13 |

### Simultaneity

- y = 0.8 / √(x−1), valid x > 5 (collective). Hotels ×1.25; schools/casernes:
  assume simultaneous; hospitals/offices: no factor.
- App simplification (documented in UI): y = 1 for x ≤ 5 (conservative).

## 2. Tube sizes (verified via merchant catalogues)

- Multicouche: 16×2, 20×2, 26×3, 32×3 (Comap Multiskin, PUM, Richardson).
- PER: 12, 16, 20, 25 (couronnes). Interiors derived arithmetically
  (outer − 2×wall).
- Copper: 10/12/14/16/18/22/28 × 1 mm sanitary range.

## 3. Algerian market context

- Frequent ADE network cuts → rooftop/ground tanks quasi-systematic;
  gravity feed or surpresseur; plumber called for bâche + terrace tank sets.
- PER and multicouche dominate new/renovation work (no welding, fast);
  copper in repair of existing stock.
- Plumbers are artisans, often solo; phone = work tool; French + Arabic
  (Darija) both needed; paper fiches still the norm → PDF share by WhatsApp
  is the realistic channel.
- Offline is a hard requirement (basements, new builds, rural sites).

## 4. Stack selection

Kotlin + Jetpack Compose (Material 3) + Room + Navigation Compose,
minSdk 26 / compileSdk 34, AGP 8.6.1, Kotlin 1.9.25 — same proven
coordinates as the sibling ARL-survey build (CI green there). No network
permission. PDF via framework `PdfDocument` (zero dependency).
