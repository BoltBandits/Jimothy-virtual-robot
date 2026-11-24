package virtual_robot.robots.classes;

import org.firstinspires.ftc.teamcode.RobotHookups;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorExImpl;
import virtual_robot.controller.BotConfig;

/** Big J's provisional drivetrain uses the existing mecanum physics and chassis artwork. */
@BotConfig(name = "Big J Bot", filename = "big_j_bot")
public class BigJBot extends MecanumPhysicsBase {
    private DcMotorExImpl shooter;
    private DcMotorExImpl feeder;

    @Override
    protected void createHardwareMap() {
        super.createHardwareMap();
        shooter = new DcMotorExImpl(MOTOR_TYPE, motorController1, 1);
        feeder = new DcMotorExImpl(MOTOR_TYPE, motorController1, 2);
        hardwareMap.put(RobotHookups.Names.SHOOTER, shooter);
        hardwareMap.put(RobotHookups.Names.FEEDER, feeder);
        hardwareMap.setActive(true);
        try {
            hardwareMap.put(RobotHookups.Names.FRONT_LEFT, hardwareMap.get(DcMotor.class, "front_left_motor"));
            hardwareMap.put(RobotHookups.Names.FRONT_RIGHT, hardwareMap.get(DcMotor.class, "front_right_motor"));
            hardwareMap.put(RobotHookups.Names.BACK_LEFT, hardwareMap.get(DcMotor.class, "back_left_motor"));
            hardwareMap.put(RobotHookups.Names.BACK_RIGHT, hardwareMap.get(DcMotor.class, "back_right_motor"));
        } finally {
            hardwareMap.setActive(false);
        }
    }

    @Override
    public synchronized void updateStateAndSensors(double millis) {
        super.updateStateAndSensors(millis);
        shooter.update(millis);
        feeder.update(millis);
    }

    @Override
    public void powerDownAndReset() {
        super.powerDownAndReset();
        shooter.stopAndReset();
        feeder.stopAndReset();
    }
}
