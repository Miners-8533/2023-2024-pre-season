package org.csa.miners;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class AutonTurret extends Turret {

    public static AutonTurret AutonTurretFromMap(HardwareMap hardwareMap) {


        // Define Turret Members
        Servo servo;
        DcMotor extensionArm;
        DcMotor liftLeft;
        DcMotor liftRight;
        DcMotor turretRotationMotor;

        servo = hardwareMap.get(Servo.class, "gripper_servo");
        extensionArm  = hardwareMap.get(DcMotor.class, "extension_motor");
        liftLeft  = hardwareMap.get(DcMotor.class, "lift_left");
        liftRight  = hardwareMap.get(DcMotor.class, "lift_right");
        turretRotationMotor = hardwareMap.get(DcMotor.class, "turret_rotate");

        liftLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        liftRight.setDirection(DcMotorSimple.Direction.FORWARD);
        extensionArm.setDirection(DcMotorSimple.Direction.FORWARD);
        turretRotationMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extensionArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretRotationMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        return new AutonTurret(liftLeft, liftRight, extensionArm, turretRotationMotor, servo);
    }

    public AutonTurret(DcMotor liftLeft, DcMotor liftRight, DcMotor extensionArm,
                       DcMotor turretRotate, Servo gripperServo){
        super(liftLeft, liftRight, extensionArm, turretRotate, gripperServo);
//        this.LIFT_LEFT_POWER_ADJUST  = 0.40;
//        this.LIFT_RIGHT_POWER_ADJUST = 0.40;
//        this.EXTEND_POWER_ADJUST = 0.50;
////        this.MAX_ROTATION_SPEED = 0.50;
//        this.MAX_ROTATION_SPEED = 0.50;
//        this.rotation_delta = 20;

        this.LIFT_LEFT_POWER_ADJUST  = 0.40;
        this.LIFT_RIGHT_POWER_ADJUST = 0.40;
        this.EXTEND_POWER_ADJUST = 0.25;
//        this.MAX_ROTATION_SPEED = 0.50;
        this.MAX_ROTATION_SPEED = 0.25;
        this.rotation_delta = 20;
    }

    public TurretPower getHighPower() {
        return new TurretPower(0.75, 0.60, 0.75, 0);
    }

    @Override
    public int getTransitionHeight() {
        return 325;
    }
}
