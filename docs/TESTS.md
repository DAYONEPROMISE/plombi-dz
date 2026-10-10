# PlombiDZ — Test table

Unit tests: `app/src/test/.../sizing/` (JUnit 4, JVM-only, run in CI as
`:app:testDebugUnitTest`). Engine expectations were hand-computed from the
formulae in `docs/RESEARCH.md` and cross-checked with an independent script.

| # | Test | Input | Expected | Status |
|---|---|---|---|---|
| 1 | simultaneity_noReductionUpToFive | x = 1, 5 | y = 1.0 | CI |
| 2 | simultaneity_formulaBeyondFive | x = 6, 10, 128 | 0.8/√(x−1) | CI |
| 3 | singleBasinPer_picks20mm | 1 lavabo, PER, 8 m | Qp 0.20 L/s, Ø20, v≈0.97 m/s | CI |
| 4 | singleBasinMultilayer_picks20mm | 1 lavabo, multi, 5 m | Ø20 / 16.0 int | CI |
| 5 | singleWcCopper_picks14mm | 1 WC, cuivre, 4 m | Qp 0.12, Ø14 | CI |
| 6 | tenMixedMultilayer_picks32mm | 5 lav.+5 douches | Qp 0.533, Ø32, v≈1.0 | CI |
| 7 | longRun_upsizesOneStep | 1 lavabo PER 8 m vs 20 m | Ø20 → Ø25 + flag | CI |
| 8 | emptyCounts_returnsZeroedResult | ∅ | 0 fixtures, 0 flow | CI |
| 9 | velocity_directComputation | 1 L/s, Ø10 int | ≈12.73 m/s; theo(0)=0 | CI |
| 10 | minBore_canDominateChoice | 1 baignoire PER | req ≥ 13 mm → Ø25 | CI |
| 11 | tankVolume_familyCase | 5 pers, 2 j, 150 L | 1500 L | CI |
| 12 | tankVolume_singlePerson | 1, 1, 200 | 200 L | CI |
| 13 | tankVolume_invalidInputsGiveZero | 0-pers / 0-day / 0-L | 0 L | CI |
| 14 | staticPressure_tenMetres | h = 10 m | ≈0.981 bar | CI |
| 15 | staticPressure_zeroOrNegative | h ≤ 0 | 0 bar | CI |

Manual (device) checklist for release: FR/AR switch + RTL, client CRUD,
fiche CRUD, PDF share opens (WhatsApp/Bluetooth), airplane-mode full tour.
