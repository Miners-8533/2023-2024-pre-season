package org.csa.miners.test.configuration;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.csa.miners.Turret;

/**
 * Autonomous Test to check the Extension Motor direction by trying to drive it forward.
 * The Extension motor must be configured so that Positive rotation drives it Clockwise.
 * If this test doesn't drive the motor Clockwise, you need to set the motor direction using
 * {@link com.qualcomm.robotcore.hardware.DcMotorSimple.Direction}
 */

@Disabled
@Autonomous(name="Motor - Extension Positive Direction", group="Motor Tests")
public class TestExtensionMotorDirection extends TurretMotorTestOpmode {

    @Override
    public DcMotor getMotorUnderTest(Turret turret) {
        return turret.extensionArm;
    }

    @Override
    public void rotateMotor() {
        turret.extensionArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        turret.extensionArm.setPower(turret.EXTEND_POWER_ADJUST);
        sleep(1000);
        turret.extensionArm.setPower(0);
    }
}
