package org.firstinspires.ftc.teamcode.bigj;

import org.firstinspires.ftc.teamcode.RobotHookups;

import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;

/** Provisional device names and directions for simulated Big J. */
public final class BigJConstants {
    public static final String FRONT_LEFT = RobotHookups.Names.FRONT_LEFT;
    public static final String FRONT_RIGHT = RobotHookups.Names.FRONT_RIGHT;
    public static final String BACK_LEFT = RobotHookups.Names.BACK_LEFT;
    public static final String BACK_RIGHT = RobotHookups.Names.BACK_RIGHT;
    public static final String SHOOTER = RobotHookups.Names.SHOOTER;
    public static final String FEEDER = RobotHookups.Names.FEEDER;
    public static final Direction LEFT_DIRECTION = Direction.REVERSE;
    public static final Direction RIGHT_DIRECTION = Direction.FORWARD;

    private BigJConstants() {}
}
