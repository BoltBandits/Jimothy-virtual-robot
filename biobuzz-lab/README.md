# BioBuzz Lab — Lessons for Jimothy's Virtual Twin

A sub-course that picks up where the main FTC Coding Lessons course leaves
off at **Lesson 11 ("Mecanum: Sideways, Then the Real Robot")**.

Instead of jumping straight from the simulator to the physical robot, these
lessons let students stay in the simulator and experiment with **actual
BIOBUZZ game scenarios** — picking pollen out of a flower, aiming at the
hive, parking in the garden — using the Jimothy virtual twin.

Everything you write here moves to the physical Jimothy by copy-paste,
because the twin's hardware names, directions, and odometry geometry match
`starter_bot.xml` exactly.

## Who it's for

- You've finished Lessons 1–11 (you can write mecanum TeleOp against a
  config block of four motor names + directions + a speed).
- You've opened `Jimothy-virtual-robot` in IntelliJ and watched a bot drive
  around the biobuzz field.
- You want to write a program that **scores**, not just one that moves.

## The scoreboard

When you select **Jimothy Bot** in the sim, a small score HUD appears in
the top-left of the field. It shows:

- **Carried** — pollen the robot is currently holding (0 to 5)
- **Hive** — pollen launched into the central hive
- **Garden** — pollen placed in your alliance's corner garden
- **Score** — total alliance points
- **last event** — most recent scoring action, in words

The HUD is updated by the sim based on where the robot is and what its
mechanisms are doing. Your OpMode reads the same counters:

```java
telemetry.addData("Pollen carried", BioBuzzScoreboard.getPollenCarried());
telemetry.addData("Score",         BioBuzzScoreboard.getAllianceScore());
```

## The lessons (slice 1)

| # | Title | What you learn |
| --- | --- | --- |
| 1 | Meet Jimothy's Twin | the hardware map, the scoreboard, the zones |
| 2 | Drive to a Target | odometry pose and a tiny proportional controller |
| 3 | Face the Flower | heading hold with the three-wheel pose |
| 4 | Pollen Pickup Zone | read the scoreboard, drive-until-collected |

Lessons 5–10 are planned (hive aim, launch timing, auto path, alliance
mirror, state machine, score race) and will land after you work through
these four.

## How a lesson works

Every lesson follows the same loop (same as the main course):

1. **READ** the task page.
2. **TYPE** the code into the file the task points at.
3. **RUN** `VirtualRobotApplication` in IntelliJ. Choose **Jimothy Bot**,
   pick your OpMode, press INIT then START.
4. **SAY** out loud what you saw happen.
5. **CHANGE** one thing — predict first, then run to check.

Each lesson lives in its own folder (`lesson1/`, `lesson2/`, …) with a
`task.md` and a starter `.java` file. Copy the `.java` file into
`TeamCode/src/org/firstinspires/ftc/teamcode/` to run it in the simulator.

## When you're ready for the real Jimothy

Any OpMode you wrote here goes to the physical robot the same way: copy
the file from `Jimothy-virtual-robot/TeamCode/.../teamcode/` to
`Jimothy/TeamCode/src/main/java/.../teamcode/`, open Android Studio, build,
deploy. No code changes — the hardware names and directions already match.

See the top-level `JIMOTHY_README.md` for the full workflow.
