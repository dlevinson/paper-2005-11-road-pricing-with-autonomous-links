# Source Boundary

## Included

The package includes selected Java source from `/Users/dlev2617/Documents/Software/ARC - Zhang/Network-Policy.zip`. The included files are the paper-relevant model source files plus the JAMA source dependency required by `Pricing.java`.

## Excluded

Compiled `.class` files are excluded because source files are included. Generated JAMA HTML documentation, JAMA examples, and JAMA timing/test material are excluded because they are dependency side material rather than paper-specific data or code.

Related ARC archives were checked but not staged as paper-specific assets. `ARC1-2.zip` and `Network-Regulation.zip` may support adjacent papers or model history, but they did not contain the missing `NetworkPolicy.java` run inputs.

## Missing Runtime Assets

The exact paper run package remains incomplete. The missing assets are the `network_growth` data/OBA/results folder equivalents, including `arcs.txt`, `zones.txt`, `arc_flow.txt`, `od_cost.txt`, `bsclnk.txt`, `vehtrp.txt`, `sf_tap_ob.tui`, and the external OBA assignment executable `tap_ob`.

The absence of these runtime dependencies should be treated as a documented limitation, not a reason to discard the staged source package. If a later source is found, add it as a separate runtime/input supplement rather than overwriting the historical source package.
