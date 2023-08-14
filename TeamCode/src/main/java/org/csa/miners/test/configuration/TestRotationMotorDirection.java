package org.csa.miners.test.configuration;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.csa.miners.Turret;
import org.csa.miners.TurretFactory;
import org.csa.miners.TurretMotorRotationTesting;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Autonomous Test to check the Base Rotation Motor direction by trying to drive it forward.
 * The Base Rotation motor must be configured so that Positive rotation drives it Clockwise.
 * If this test doesn't drive the motor Clockwise, you need to set the motor direction using
 * {@link com.qualcomm.robotcore.hardware.DcMotorSimple.Direction}
 */

@Disabled
@Autonomous(name="Rotate Turret Base Motor Clockwise", group="Motor Tests")
public class TestRotationMotorDirection extends TurretMotorTestOpmode {

    @Override
    public DcMotor getMotorUnderTest(Turret turret) {
        return turret.turretRotate;
    }

    @Override
    public void rotateMotor() {
        turret.turretRotate.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        turret.turretRotate.setPower(turret.MAX_ROTATION_SPEED);
        sleep(1000);
        turret.turretRotate.setPower(0);
    }
}
