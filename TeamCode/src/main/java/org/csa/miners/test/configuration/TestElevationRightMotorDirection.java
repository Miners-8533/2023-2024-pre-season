package org.csa.miners.test.configuration;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.csa.miners.Turret;

/**
 * Autonomous Test to check the Right Lift Motor direction by trying to drive it forward.
 * The Lift Right motor must be configured so that Positive rotation drives it Counter Clockwise.
 *
 * If this test doesn't drive the motor Clockwise, you need to set the motor direction using
 * {@link com.qualcomm.robotcore.hardware.DcMotorSimple.Direction}
 */

@Disabled
@Autonomous(name="Rotate Lift Right Motor Clockwise", group="Motor Tests")
public class TestElevationRightMotorDirection extends TurretMotorTestOpmode {

    @Override
    public DcMotor getMotorUnderTest(Turret turret) {
        return turret.liftRight;
    }

    @Override
    public void rotateMotor() {
        turret.rotateLiftRightClockwise();
    }
}
