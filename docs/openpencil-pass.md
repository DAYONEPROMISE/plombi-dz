# OpenPencil pass — plombi-dz

## Design (Shared/OpenPencil/plombi-dz/)
- `design.html` (job card + PDF share + sizing note + 4-tab nav, DZ tokens) → `design.fig`
- `lint --preset accessibility`: 0 errors, 0 warnings

## Code changes
- `ui/ClientsScreen.kt`: FAB add + row delete now use `clients_add`/`delete` strings.
- `ui/JobsScreen.kt`: FAB add + share + delete now use `jobs_add`/`job_share_pdf`/`delete`.
  (All keys verified in `values/strings.xml`; full files verified on read-back.)
- Home nav already described; FR/AR locale helper untouched.

## APKs
`ci.yml` `build` job adds `assembleRelease` (unsigned) + `app-release` artifact + a versioned
`release-v*-b*` GitHub Release (test/lint/debug flow unchanged).
