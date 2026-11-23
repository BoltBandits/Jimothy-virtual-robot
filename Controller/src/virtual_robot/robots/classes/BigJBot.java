package virtual_robot.robots.classes;

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
        hardwareMap.put("shooter_motor", shooter);
        hardwareMap.put("feeder_motor", feeder);
        hardwareMap.setActive(true);
        try {
            hardwareMap.put("left_front_drive", hardwareMap.get(DcMotor.class, "front_left_motor"));
            hardwareMap.put("right_front_drive", hardwareMap.get(DcMotor.class, "front_right_motor"));
            hardwareMap.put("left_back_drive", hardwareMap.get(DcMotor.class, "back_left_motor"));
            hardwareMap.put("right_back_drive", hardwareMap.get(DcMotor.class, "back_right_motor"));
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
