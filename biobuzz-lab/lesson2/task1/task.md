**Goal:** make Jimothy drive to a point on the field without you touching
the sticks.
**You'll learn:** that a robot needs to **know where it is** before it can
go somewhere specific.

## The problem

Open the sim, pick **Jimothy Bot**, and in the Op Mode dropdown pick
**Jimothee Field-Centric TeleOp**. INIT, START, then move the robot a bit
and read the HUD:

| What it says | What it means |
| --- | --- |
| `Heading` | which direction Jimothy is facing, in degrees |
| `Odometry L / R ticks` | numbers from Jimothy's two parallel pods |

Those numbers come from the three odometry pod wheels under the robot:

- **Left pod** (`intake_motor` encoder) — a free-spinning 2 in. wheel on
  the left side, 6.1315 in. left of center. Rolls forward as the robot
  moves forward.
- **Right pod** (`odo_right`) — same, 6.1315 in. right of center.
- **Center pod** (`odo_center`) — a free wheel oriented sideways, 6.718
  in. behind center. Measures sideways (strafe) motion.

By watching how much each pod has rotated since the last tick, we can
compute **three numbers**:

- `x` — how far forward Jimothy has moved, in inches
- `y` — how far left Jimothy has moved, in inches
- `heading` — how much Jimothy has turned, in radians

Those three numbers together are called the **pose**. If a robot knows
its pose, you can tell it "go to pose (24, 0, 0)" and it will.

Press **Next** to start writing the pose code.
