package org.firstinspires.ftc.teamcode.drive.opmode.testopmodes.turretmotionplantests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.csa.miners.motionplan.AutonMotionPlans;
import org.csa.miners.motionplan.MotionPlan;
import org.csa.miners.Turret;
import org.csa.miners.TurretFactory;
import org.csa.miners.TurretPosition;
import org.csa.miners.motionplan.ReplayElement;
import org.csa.miners.motionplan.WaitElement;
import org.csa.miners.powerplay.AutonPositions;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * OpMode tests the saved position for the MidHeight Junction by delivering the preloaded cone
 */

@Disabled
@Autonomous(name="Motion Plan - Mid Junction Auton", group="MotionPlan Tests")
public class TurretMidJunctionAutonPlan extends MotionPlanTestOpmode {

//    Turret turret;
//    MotionPlan plan;
//
//    TurretPosition loadPosition;
//    TurretPosition unloadPosition;

//    public void reportPosition(String caption, TurretPosition position) {
//        telemetry.addData(caption, "%4.2f |%05d |      |%05d |%05d",
//                position.grip(),
//                position.elevation(),
//                position.extension(),
//                position.rotation()
//        );
//    }

    public MotionPlan getMotionPlan(Turret turret) {
        return AutonMotionPlans.getJunctionPlan(turret, AutonPositions.getMiddleJunction());
    }

}
