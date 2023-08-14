package org.csa.miners.test.configuration;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.csa.miners.Turret;

/**
 * Autonomous Test to check the Base Rotation Motor direction by trying to drive it forward.
 * The Base Rotation motor must be configured so that Positive rotation drives it Clockwise.
 * If this test doesn't drive the motor Clockwise, you need to set the motor direction using
 * {@link DcMotorSimple.Direction}
 */

@Disabled
@Autonomous(name="Rotate Turret Base Motor To Position", group="Motor Tests")
public class TestRotationRunToPosition extends TurretMotorTestOpmode {

    @Override
    public DcMotor getMotorUnderTest(Turret turret) {
        return turret.turretRotate;
    }

    @Override
    public void rotateMotor() {
        turret.turretRotate.setTargetPosition(100);
        turret.turretRotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.turretRotate.setPower(0.1);
    }
}
