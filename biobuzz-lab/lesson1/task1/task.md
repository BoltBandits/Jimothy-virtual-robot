**Goal:** open the virtual twin of our competition robot, see that it is
*the same robot* as the one you'll drive at a meet.
**You'll learn:** what makes a "digital twin" useful.

## Welcome back

For eleven lessons you drove a generic Mecanum Bot around an empty field.
That was practice. **Jimothy** is our actual competition robot, built on
the goBILDA BIOBUZZ Starter Bot chassis, and from this lesson on, that is
the robot you'll be programming — in the simulator and, eventually, on the
real one.

The simulator's Jimothy is a **digital twin**. Every motor name, every
servo name, every direction, every odometry pod — same as the real
robot's `starter_bot` configuration file. Anything you make work here will
work there, with no code changes.

## Open the project

1. Open the `Jimothy-virtual-robot` folder in IntelliJ IDEA. If this is
   your first time, let IntelliJ import the project and set the SDK to
   JDK 17.
2. In the project tree, find
   `Controller/src/virtual_robot/controller/VirtualRobotApplication.java`.
3. Right-click → **Run 'VirtualRobotApplication.main()'**.
4. A window opens with a field in it.

Look at the field.

- The green rectangle in the middle is Jimothy, lifted off the ground so
  the mecanum wheels show.
- Four pale yellow circles mark the **flowers**, one on each wall.
- The orange circle in the middle is the **hive**.
- The two green circles in opposite corners are the **gardens**.
- In the top-left corner is a small black box — the **scoreboard**.

That is your BIOBUZZ practice field. Press **Next** to look at Jimothy's
hardware.
