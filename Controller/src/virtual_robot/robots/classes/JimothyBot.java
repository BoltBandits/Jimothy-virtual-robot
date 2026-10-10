package virtual_robot.robots.classes;

import com.qualcomm.robotcore.hardware.CRServoImpl;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorExImpl;
import com.qualcomm.robotcore.hardware.DeadWheelEncoder;
import virtual_robot.controller.BotConfig;
import virtual_robot.util.AngleUtils;

/**
 * Virtual twin of the BoltBandits BIOBUZZ "Jimothy" starter bot.
 *
 * Hardware names and directions mirror Jimothy/starter_bot.xml so OpModes
 * written for either robot run unchanged on the other.
 *
 * Drive motors (goBILDA 5202 class, mecanum):
 *   left_front_drive, right_front_drive, left_back_drive, right_back_drive
 * Mechanism motors (goBILDA 5202):
 *   intake_motor   -- encoder is wired to the LEFT parallel odometry pod
 *                     (same hardware trick as the real robot)
 *   shooter_motor
 * Odometry (plain motor-encoder cables, no motor attached):
 *   odo_right   -- RIGHT parallel pod
 *   odo_center  -- perpendicular pod, 6.718" behind the robot center
 * Continuous servos:
 *   left_intake_servo, right_intake_servo (reversed), windmill_servo (reversed)
 * IMU:
 *   imu (inherited from MecanumPhysicsBase)
 *
 * The inherited sensor suite (color_sensor, {front,left,right,back}_distance,
 * pinpoint, sensor_otos, octoquad) is also available for extra challenges, but
 * is not wired into any physical Jimothy OpMode yet.
 */
@BotConfig(name = "Jimothy Bot", filename = "jimothy_bot")
public class JimothyBot extends MecanumPhysicsBase {

    private static final double POD_WHEEL_DIAMETER_INCHES = 2.0;
    private static final double POD_SPACING_INCHES = 12.263;
    private static final double LEFT_POD_X_INCHES  = -POD_SPACING_INCHES / 2.0;
    private static final double RIGHT_POD_X_INCHES =  POD_SPACING_INCHES / 2.0;
    private static final double CENTER_POD_Y_INCHES = -6.718;

    private DeadWheelEncoder leftPod;
    private DeadWheelEncoder rightPod;
    private DeadWheelEncoder centerPod;

    private double podWheelRadius;
    private double leftPodX;
    private double rightPodX;
    private double centerPodY;

    public JimothyBot() {
        super();
    }

    @Override
    protected void createHardwareMap() {
        super.createHardwareMap();

        // hardwareMap.get() guards against lookups when inactive; toggle around our pulls.
        hardwareMap.setActive(true);
        DcMotor fl = hardwareMap.get(DcMotorEx.class, "front_left_motor");
        DcMotor fr = hardwareMap.get(DcMotorEx.class, "front_right_motor");
        DcMotor bl = hardwareMap.get(DcMotorEx.class, "back_left_motor");
        DcMotor br = hardwareMap.get(DcMotorEx.class, "back_right_motor");
        hardwareMap.setActive(false);

        hardwareMap.put("left_front_drive",  fl);
        hardwareMap.put("right_front_drive", fr);
        hardwareMap.put("left_back_drive",   bl);
        hardwareMap.put("right_back_drive",  br);

        // Legacy "dancebot" aliases so DanceBot OpModes also run on this bot
        // (motor0=FR, motor1=BR, motor2=BL, motor3=FL, per dancebot.xml convention)
        hardwareMap.put("motor0", fr);
        hardwareMap.put("motor1", br);
        hardwareMap.put("motor2", bl);
        hardwareMap.put("motor3", fl);

        hardwareMap.put("intake_motor", new DeadWheelEncoder(MOTOR_TYPE, motorController1, 0));
        hardwareMap.put("odo_right",    new DeadWheelEncoder(MOTOR_TYPE, motorController1, 1));
        hardwareMap.put("odo_center",   new DeadWheelEncoder(MOTOR_TYPE, motorController1, 2));

        hardwareMap.put("shooter_motor", new DcMotorExImpl(MOTOR_TYPE, motorController1, 3));

        hardwareMap.put("left_intake_servo",  new CRServoImpl(720));
        hardwareMap.put("right_intake_servo", new CRServoImpl(720));
        hardwareMap.put("windmill_servo",     new CRServoImpl(720));
    }

    @Override
    public void initialize() {
        super.initialize();
        hardwareMap.setActive(true);

        leftPod   = hardwareMap.get(DeadWheelEncoder.class, "intake_motor");
        rightPod  = hardwareMap.get(DeadWheelEncoder.class, "odo_right");
        centerPod = hardwareMap.get(DeadWheelEncoder.class, "odo_center");

        podWheelRadius = 0.5 * POD_WHEEL_DIAMETER_INCHES * botWidth / 18.0;
        leftPodX   = LEFT_POD_X_INCHES   * botWidth / 18.0;
        rightPodX  = RIGHT_POD_X_INCHES  * botWidth / 18.0;
        centerPodY = CENTER_POD_Y_INCHES * botWidth / 18.0;

        hardwareMap.setActive(false);
    }

    @Override
    public synchronized void updateStateAndSensors(double millis) {
        double xOld = x;
        double yOld = y;
        double headingOld = headingRadians;

        super.updateStateAndSensors(millis);

        double deltaX = x - xOld;
        double deltaY = y - yOld;
        double headingChange = AngleUtils.normalizeRadians(headingRadians - headingOld);
        double avg = AngleUtils.normalizeRadians(headingOld + 0.5 * headingChange);
        double sin = Math.sin(avg);
        double cos = Math.cos(avg);

        double dxR = deltaX * cos + deltaY * sin;
        double dyR = -deltaX * sin + deltaY * cos;

        double rightEncoderRadians  =  (dyR + rightPodX  * headingChange) / podWheelRadius;
        double leftEncoderRadians   = -(dyR + leftPodX   * headingChange) / podWheelRadius;
        double centerEncoderRadians = -(dxR - centerPodY * headingChange) / podWheelRadius;

        rightPod.update(rightEncoderRadians, millis);
        leftPod.update(leftEncoderRadians, millis);
        centerPod.update(centerEncoderRadians, millis);
    }

    @Override
    public void powerDownAndReset() {
        super.powerDownAndReset();
        if (leftPod != null)   leftPod.stopAndReset();
        if (rightPod != null)  rightPod.stopAndReset();
        if (centerPod != null) centerPod.stopAndReset();
    }
}
