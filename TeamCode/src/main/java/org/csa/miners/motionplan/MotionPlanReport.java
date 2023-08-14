package org.csa.miners.motionplan;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MotionPlanReport {
    private OpMode opMode;
    private MotionPlan plan;

    public MotionPlanReport(OpMode opMode, MotionPlan plan) {
        this.opMode = opMode;
        this.plan = plan;
    }

    private Telemetry getTelemetry() {
        return this.opMode.telemetry;
    }

    public void render() {
        Telemetry telemetry = this.getTelemetry();
        telemetry.addLine("Plan Report");

        telemetry.addData("Plan In Progress", "%s", this.plan.isPlanInProgress() ? "TRUE" : "FALSE");
        telemetry.addData("Plan Complete", "%s", this.plan.isPlanComplete() ? "TRUE" : "FALSE");
        telemetry.addData("Plan", "Step %d of %d", this.plan.getCurrentStepIndex(), this.plan.getTotalSteps());
    }
}
