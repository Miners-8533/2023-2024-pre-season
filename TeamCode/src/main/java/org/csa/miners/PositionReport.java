package org.csa.miners;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.csa.miners.motionplan.MotionPlan;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class PositionReport {
    private OpMode opMode;
    private Turret turret;

    public PositionReport(OpMode opMode, Turret turret) {
        this.opMode = opMode;
        this.turret = turret;
    }

    private Telemetry getTelemetry() {
        return this.opMode.telemetry;
    }

    public void render() {
        Telemetry telemetry = this.getTelemetry();


        turret.reportTelemetry(telemetry);
        
    }
}
