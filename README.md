# PlombiDZ — Boîte à outils du plombier (Algérie)

Offline-first Android app (Kotlin, Jetpack Compose, Material 3, Room) for Algerian
plumbers. 100 % offline, no account, French/Arabic with RTL.

## Features

- **Pipe-diameter guidance** — fixture counts → probable flow (NF DTU 60.11:
  base flows, y = 0.8/√(x−1)) → recommended PER / multicouche / copper size,
  velocity check, long-run upsizing flag.
- **Water-tank notes** — volume from persons × autonomy days × L/person/day,
  gravity pressure from height.
- **Pressure notes** — static pressure ≈ 0.0981 bar/m, 1 bar minimum guidance,
  reducer guidance.
- **Client register** — stored on-device (Room).
- **Intervention fiches** — stored on-device, shared as PDF (framework
  PdfDocument, FileProvider) — no account, no network.

## Build

CI (`.github/workflows/ci.yml`) runs **unit tests → lint → assembleDebug**,
uploads the APK artifact and publishes a `debug-latest` release.

Local build needs JDK 17 + Android SDK (compileSdk 34):

```sh
./gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleDebug
```

## Project layout

```
app/src/main/java/dz/plombidz/app/
  MainActivity.kt            theme + bottom-tab navigation + home
  sizing/Fixtures.kt         DTU 60.11 base flows + standard tube sizes
  sizing/SizingEngine.kt     simultaneity → probable flow → diameter
  sizing/TankPressure.kt     tank volume + static pressure
  data/Store.kt              Room entities + DAO
  data/PlombiDatabase.kt     Room database
  data/PlombiViewModel.kt    UI state
  pdf/FichePdf.kt            offline PDF generation
  util/LocaleHelper.kt       FR/AR switch
  ui/                        Compose screens
docs/  RESEARCH, TESTS, LIMITATIONS, COSTS, FIRST10
```

## License

MIT — clean-room implementation. Normative values (DTU 60.11 flows) are
cited facts, see `docs/RESEARCH.md`.
