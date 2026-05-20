# Compile Check

Checked on 2026-05-17 using the bundled JDK at `/Users/dlev2617/Documents/Code/STREET-JS/tools/jdk/jdk-17.0.18+8/Contents/Home/bin/javac`.

Command shape:

```sh
javac -d /Users/dlev2617/Documents/Code/Agents/tmp_2005_11_javac <all staged .java files>
```

Result: source compiled successfully. The compiler reported one deprecation warning for `new Integer(iteration)` in `OutputProcessing.java` and unchecked-operation notes for JAMA `Matrix.java`. No source edits were made.

This compile check does not prove runtime reproduction, because the paper-specific OBA executable/configuration and initial input/output files remain unavailable.
