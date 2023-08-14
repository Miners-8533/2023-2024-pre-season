package org.csa.miners.test.configuration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.csa.miners.Turret;
import org.csa.miners.TurretFactory;
import org.csa.miners.TurretMotorRotationTesting;
import org.firstinspires.ftc.robotcore.external.Telemetry;

abstract public class TurretMotorTestOpmode extends LinearOpMode {


    TurretMotorRotationTesting turret;

    @Override
    public void runOpMode() {

        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);

        turret = TurretMotorRotationTesting.turretFromTurret(
                TurretFactory.getTurretFromHardwareMap(hardwareMap));

        // Wait for the start button
        telemetry.addData(">", "Press Start to Rotate the Left Elevation motor CW." );
        telemetry.update();

        waitForStart();


        // Scan servo till stop pressed.
        if(opModeIsActive()) {

            DcMotor motorUnderTest = this.getMotorUnderTest(turret);
            int initPosition = motorUnderTest.getCurrentPosition();
            telemetry.addData("INITIAL", initPosition);

            this.rotateMotor();

            sleep(1000);
            int positiveResult = motorUnderTest.getCurrentPosition();
            telemetry.addData("FORWARD", positiveResult);

            if(positiveResult < 0) {
                telemetry.addLine("FORWARD Rotated in NEGATIVE direction");
            } else {
                telemetry.addLine("FORWARD Rotated in POSITIVE direction");
            }

            turret.reportTelemetry(telemetry);

            telemetry.addData(">", "Press Stop to end test." );
            telemetry.update();

            idle();
        }

        sleep(10000);


        // Signal done;
        telemetry.addData(">", "Done");
        telemetry.update();
    }

    public abstract DcMotor getMotorUnderTest(Turret turret);
    public abstract void rotateMotor();


    public static final void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
