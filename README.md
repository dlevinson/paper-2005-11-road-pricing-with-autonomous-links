# Road Pricing With Autonomous Links

## Bibliographic Information

- Row ID: `paper-2005-11`
- Year: 2005
- Authors: Zhang, Levinson
- Venue: Transportation Research Record 1932:147-155 (2005)
- DOI/URL: 10.3141/1932-17
- Citation: Zhang, Levinson. (2005). Road Pricing With Autonomous Links. Transportation Research Record 1932:147-155 (2005). 10.3141/1932-17

## Package Status

This is ready for public upload review as a historical source-code package. It is not a complete runtime reproduction package.

The package contains a strong local Java source-code match from `/Users/dlev2617/Documents/Software/ARC - Zhang/Network-Policy.zip`. The source matches the paper's autonomous-link pricing model structure, including trip distribution, OBA assignment input generation, pricing policies, investment policies, measures of effectiveness, and output writing. A compile check using the bundled JDK succeeded without source edits.

The exact runtime input files and the external OBA assignment executable/configuration were not found after targeted filesystem and archive searches. These missing pieces are documented in `metadata/RUNTIME_DEPENDENCY_GAP.csv`; they should not keep this source package in open-ended search.

Expected pipeline after tracker refresh: `READY-TO-UPLOAD/PUBLIC`.

## Contents

- `paper/AutonomousPricing.pdf`: local audit reference copy of the paper.
- `code/network_policy_source_original/`: selected Java source files from `Network-Policy.zip`, including the model code and JAMA Java source dependency.
- `documentation/PAPER_FIRST_VALIDATION.md`: paper-to-source validation notes.
- `documentation/SOURCE_BOUNDARY.md`: included, excluded, and missing asset boundaries.
- `documentation/COMPILE_CHECK.md`: javac compile-check note.
- `metadata/SOURCE_MANIFEST.csv`: staged source file manifest with hashes.
- `metadata/ZIP_CONTENTS_REVIEW.csv`: review of included/excluded archive groups.
- `metadata/RUNTIME_DEPENDENCY_GAP.csv`: missing runtime dependency/input list.
- `metadata/TARGETED_SEARCH_FINDINGS.csv`: targeted search summary.
- `metadata/EXCLUDED_LOCAL_FILES.csv`: explicit exclusions.

## Public Archive Or Source Pointers

- https://doi.org/10.3141/1932-17
- https://hdl.handle.net/11299/179922
- https://conservancy.umn.edu/items/fa4396af-9eb6-4faf-8ad6-563a706b080c

## Reproduction Boundary

The staged Java source is a credible historical source package for the published model. Runtime reproduction would still require reconstructing or recovering `arcs.txt`, `zones.txt`, initial OBA outputs, `sf_tap_ob.tui`, and `tap_ob`, or replacing OBA with a modern open assignment implementation and documenting that modernization separately.

<!-- package-hardening-status:start -->
## Package Hardening Status

Generated: 2026-05-22 07:46:52 AEST

- Pipeline: `READY-TO-UPLOAD/PUBLIC`
- Sidecars added/updated: `PACKAGE_STATUS.md`, `PACKAGE_MANIFEST.csv`, `LICENSE_STATUS.md`.
- Public paper-package repositories include `paper/` PDF reference copies by owner decision; publisher takedown requests can be handled later if they arise.
- Final GitHub upload should use the manifest include statuses and the license-status note.
<!-- package-hardening-status:end -->
