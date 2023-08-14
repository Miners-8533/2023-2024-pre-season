package org.csa.miners;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class TurretMotorRotationTesting extends Turret {

    /**
     * Create a {@link TurretMotorRotationTesting} object from an existing {@link Turret} Object
     * @param turret The turret to test on
     * @return A {@link TurretMotorRotationTesting} object based on turret.
     */
    public static TurretMotorRotationTesting turretFromTurret(Turret turret) {
        return new TurretMotorRotationTesting(turret.liftLeft, turret.liftRight,
                turret.extensionArm, turret.turretRotate, turret.gripper);
    }

    public TurretMotorRotationTesting(DcMotor liftLeft, DcMotor liftRight, DcMotor extensionArm,
                                                          DcMotor turretRotate, Servo gripperServo){
        super(liftLeft, liftRight, extensionArm, turretRotate, gripperServo);
    }

    public void rotateLiftLeftClockwise() {
        this.liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.liftLeft.setPower(this.LIFT_LEFT_POWER_ADJUST);
    }

    public void rotateLiftRightClockwise() {
        this.liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.liftRight.setPower(0-this.LIFT_RIGHT_POWER_ADJUST);
    }

    public void rotateExtensionClockwise() {
        this.extensionArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.extensionArm.setPower(this.EXTEND_POWER_ADJUST);
    }

    public void rotateRotationClockwise() {
        this.turretRotate.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.turretRotate.setPower(this.MAX_ROTATION_SPEED);
    }

}
