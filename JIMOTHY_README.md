# Jimothy Virtual Robot — Student Guide

A **digital twin of Jimothy**, the BoltBandits BIOBUZZ starter bot. Students
write OpModes against the same hardware names and directions as the physical
robot, so code transfers directly between this simulator and
[BoltBandits/Jimothy](https://github.com/BoltBandits/Jimothy).

Built on [Beta8397/virtual_robot](https://github.com/Beta8397/virtual_robot).

---

## 1. One-time setup

### Prerequisites

- **JDK 17** (or newer). JDK 11 also works; see `JDK Options.pdf` for details.
  - On Windows: download the Adoptium/Temurin JDK installer.
- **IntelliJ IDEA Community Edition** (free). JavaFX is bundled.
- **Git**.

### Clone and open

```bash
git clone https://github.com/BoltBandits/Jimothy-virtual-robot.git
cd Jimothy-virtual-robot
```

Open IntelliJ → **File → Open** → select the `Jimothy-virtual-robot` folder.

When IntelliJ asks about Project SDK, point it at your JDK 17.

### Run it

1. In IntelliJ's project tree, find
   `Controller/src/virtual_robot/controller/VirtualRobotApplication.java`.
2. Right-click it → **Run 'VirtualRobotApplication.main()'**.
3. The simulator window opens with the **Jimothy Bot** already selected and
   the BIOBUZZ field loaded.

If the first run fails with a JavaFX error, consult `JDK Options.pdf` in this
repo — it covers common JDK/JavaFX fixes.

---

## 2. What matches the real Jimothy

The virtual Jimothy mirrors `Jimothy/starter_bot.xml` exactly:

| Role | Hardware name in `hardwareMap` | Type | Direction |
| --- | --- | --- | --- |
| Front-left drive | `left_front_drive` | `DcMotorEx` | `REVERSE` |
| Front-right drive | `right_front_drive` | `DcMotorEx` | `FORWARD` |
| Back-left drive | `left_back_drive` | `DcMotorEx` | `REVERSE` |
| Back-right drive | `right_back_drive` | `DcMotorEx` | `FORWARD` |
| Intake motor | `intake_motor` | `DcMotorEx` *(encoder = LEFT odo pod)* | — |
| Shooter | `shooter_motor` | `DcMotorEx` | — |
| Right odometry pod | `odo_right` | `DcMotor` (encoder only) | — |
| Center odometry pod | `odo_center` | `DcMotor` (encoder only) | — |
| Left intake servo | `left_intake_servo` | `CRServo` | `FORWARD` |
| Right intake servo | `right_intake_servo` | `CRServo` | `REVERSE` |
| Windmill | `windmill_servo` | `CRServo` | `REVERSE` |
| IMU | `imu` | `IMU` / `BNO055IMU` | — |

Odometry geometry matches the real robot:

- Parallel pod spacing: **12.263 in**
- Center pod offset: **6.718 in behind robot center**
- Pod wheel diameter: **2.0 in**

The intake-motor-as-left-pod trick works here the same way it does on the
physical robot: `intake_motor.getCurrentPosition()` reports the LEFT pod's
encoder ticks.

Extras from the base simulator you can also use: `color_sensor`,
`front_distance` / `left_distance` / `right_distance` / `back_distance`,
`pinpoint` (goBILDA Pinpoint), `sensor_otos` (SparkFun OTOS), `octoquad`.

---

## 3. Running the ported OpModes

In the simulator window, use the **OpMode** dropdown to pick:

- `Jimothee Field-Centric TeleOp` — field-centric mecanum drive (uses odo pods for heading)
- `Mec BioBuzz StarterBot Teleop v5` — competition teleop with shooter + intake
- `Jimothy Automonous` — the fast-path autonomous
- `BioBuzz Odometry Pose Test` — three-wheel pose readout
- `BioBuzz Odometry Auto Test` — closed-loop autonomous validation
- `BioBuzz Odometry Identifier` — tells you which pod is on which port
- `BioBuzz Intake Hardware Test` — individual mechanism diagnostic
- `DanceBot TeleOp` / `DanceBot Auto` / `DanceBot Motor Test` — the demo
  OpModes that use the legacy `motor0..motor3` names (also wired up on this
  bot via aliases to the drive motors)

Click **INIT** then **START**. If `USE_VIRTUAL_GAMEPAD` is `true`
(default), the on-screen gamepad reacts to keyboard. See the original
`README.md` for the keyboard mapping.

---

## 4. Writing new OpModes — the two-way workflow

**Virtual-first (recommended for new drivers / autonomous tuning):**

1. Write your OpMode in
   `Jimothy-virtual-robot/TeamCode/src/org/firstinspires/ftc/teamcode/`.
2. Run it in the simulator, iterate on logic.
3. Copy the `.java` file to
   `Jimothy/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/`.
4. Build and deploy to the Control Hub.

Because hardware names, directions, and odometry geometry match, step 3 is
usually a straight copy — no code changes.

**Physical-first (when porting existing Jimothy code):**

1. Copy the `.java` file from `Jimothy/TeamCode/.../teamcode/` into the
   virtual repo's `TeamCode/src/org/firstinspires/ftc/teamcode/`.
2. Run it in the simulator. Any crash will usually come from an API the
   simulator doesn't model (e.g. real vision pipelines) — comment out that
   block for the simulator run.

---

## 5. Switching to the BioBuzz practice field

The sim ships with two field configurations:

- `Decode` (default) — the official 2026-2027 BIOBUZZ Decode game field
  with goals and ramps as static obstacles.
- `BioBuzzPractice` — Decode plus three dynamic "tipping buckets" for
  driver training. Bucket positions and physics are tunable in
  `Controller/src/virtual_robot/games/BioBuzzPractice.java`.

To switch, open
`Controller/src/virtual_robot/config/Config.java` and change:

```java
public static final Game GAME = new Decode();
```

to:

```java
public static final Game GAME = new BioBuzzPractice();
```

Then re-run `VirtualRobotApplication`.

---

## 6. Changing the field size for small laptop screens

Open `Config.java`. The default field is 648x648 px. For a smaller screen:

```java
public static final double FIELD_WIDTH = 504;  // or 432, 576
public static final Image BACKGROUND = new Image("/virtual_robot/assets/biobuzz504.bmp");
```

---

## 7. What's NOT simulated (yet)

- Shooter projectile physics — `shooter_motor.setVelocity(...)` works and
  `getVelocity()` responds correctly, but nothing visually launches.
- Intake pickup — `intake_motor.setPower(...)` works at the API level, but
  there's no visual intake or game-element pickup.
- Vision pipelines (OpenCV, AprilTags) — the SDK stubs are present but
  there's no camera feed.

These are places where code tested in sim will need an extra on-robot pass
before competition. Everything else — drivetrain motion, encoder-based
odometry, IMU heading, telemetry, autonomous paths — is high-fidelity.

---

## 8. Where things live

```
Jimothy-virtual-robot/
├── Controller/src/
│   ├── virtual_robot/
│   │   ├── config/Config.java              # bot/game/field selection
│   │   ├── robots/classes/JimothyBot.java  # the twin bot definition
│   │   ├── robots/fxml/jimothy_bot.fxml    # the bot's visual
│   │   └── games/
│   │       ├── Decode.java                 # official field
│   │       └── BioBuzzPractice.java        # practice field + buckets
│   └── com/qualcomm/...                    # FTC SDK stubs (do not edit)
├── TeamCode/src/org/firstinspires/ftc/teamcode/
│   ├── BioBuzz*.java                       # Jimothy OpModes (ported)
│   ├── Jimothee*.java
│   ├── DanceBot*.java
│   └── Mecanum/XDrive/TwoWheel/etc.Demo.java  # framework demos (reference)
└── JIMOTHY_README.md                       # this file
```

---

## 9. Troubleshooting

- **"No DcMotor named motor0 is found"** — you ran a DanceBot OpMode without
  the aliases loaded; make sure Jimothy Bot is selected (it provides the
  aliases). This should not normally happen since JimothyBot defines them.
- **IntelliJ shows red everywhere** — Project SDK not set. File → Project
  Structure → set Project SDK to your JDK 17.
- **"JavaFX runtime components are missing"** — JDK version issue. Open
  `JDK Options.pdf` in this repo for fixes.
- **Simulator starts but robot doesn't move** — hit INIT first, then START.
  The hardware map is inactive until INIT.

---

## 10. Updating the twin when Jimothy's hardware changes

If the physical robot's `starter_bot.xml` gets new motors or servos:

1. Edit `JimothyBot.createHardwareMap()` to register the new hardware under
   the same names used in `starter_bot.xml`.
2. For dead-wheel encoders, use `new DeadWheelEncoder(MOTOR_TYPE,
   motorController1, portNum)`.
3. For regular motors used purely for logic (shooter, intake, lift) use
   `new DcMotorExImpl(MOTOR_TYPE, motorController1, portNum)`.
4. For continuous servos use `new CRServoImpl(720)`.
5. If you add more odometry pods, extend `updateStateAndSensors(...)` to
   update their encoder radians based on robot motion.

Keep the names identical to the physical config — that's what lets code
move between the two repos without edits.
