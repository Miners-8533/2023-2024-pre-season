package org.csa.miners.test.configuration;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.csa.miners.Turret;

/**
 * Autonomous Test to check the Left Lift Motor direction by trying to drive it forward.
 * The Lift Left motor must be configured so that Positive rotation drives it Clockwise.
 * If this test doesn't drive the motor Clockwise, you need to set the motor direction using
 * {@link com.qualcomm.robotcore.hardware.DcMotorSimple.Direction}
 */

@Disabled
@Autonomous(name="Rotate Lift Left Motor Clockwise", group="Motor Tests")
public class TestElevationLeftMotorDirection extends TurretMotorTestOpmode {

    @Override
    public DcMotor getMotorUnderTest(Turret turret) {
        return turret.liftLeft;
    }

    @Override
    public void rotateMotor() {
        turret.rotateLiftLeftClockwise();
    }
}
