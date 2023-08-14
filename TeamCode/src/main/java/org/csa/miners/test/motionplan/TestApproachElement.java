package org.csa.miners.test.motionplan;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.csa.miners.Turret;
import org.csa.miners.TurretPosition;
import org.csa.miners.TurretPower;
import org.csa.miners.motionplan.MotionPlan;
import org.firstinspires.ftc.teamcode.drive.opmode.testopmodes.turretmotionplantests.MotionPlanTestOpmode;

@Disabled
@Autonomous(name="MotionPlan - ApproachElement", group="MotionPlan Tests")
public class TestApproachElement extends MotionPlanTestOpmode {

    @Override
    public MotionPlan getMotionPlan(Turret turret) {
        TurretPosition delta = new TurretPosition(50, 50, 50, 1);
        TurretPower testPower = new TurretPower(0.05,0.05,0.05, 0.05);

        plan = new MotionPlan(turret, testPower);
        plan.add(new TurretPosition(0, 0, 0, 0));
        plan.add(new TurretPosition(100, 100, 100, 0), delta);
        plan.add(new TurretPosition(100, 500, 500, 0), delta);
        return plan;
    }
}
