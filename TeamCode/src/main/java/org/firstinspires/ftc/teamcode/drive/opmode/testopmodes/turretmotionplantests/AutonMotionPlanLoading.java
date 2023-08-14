package org.firstinspires.ftc.teamcode.drive.opmode.testopmodes.turretmotionplantests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.csa.miners.motionplan.MotionPlan;
import org.csa.miners.Turret;
import org.csa.miners.TurretPosition;
import org.csa.miners.motionplan.WaitElement;

@Disabled
@Autonomous(name="MotionPlan - Load Plan", group="MotionPlan Tests")
public class AutonMotionPlanLoading extends MotionPlanTestOpmode {

    @Override
    public MotionPlan getMotionPlan(Turret turret) {
        int load1 = 130;
        int load2 = 100;
        int load3 = 70;
        int load4 = 40;
        int load5 = 10;

        int pauseOverJunction = 2000;

        plan = new MotionPlan(turret);
        plan.add(new TurretPosition(0, load1, 0, 0));
        plan.add(new TurretPosition(0, load1, 620, 0));

//        plan.add(new WaitElement(1000));

        plan.add(new TurretPosition(0, load1, 620, 1));
        // Leadout
        plan.add(new TurretPosition(0, 275, 600, 1));

//        plan.add(new TurretPosition(0, 200, 200, 1));
        plan.add(new TurretPosition(-800, 700, 570, 1));
        plan.add(new WaitElement(pauseOverJunction));
        plan.add(new TurretPosition(-800, 700, 570, 0));
        plan.add(new TurretPosition(-300, 700, 570, 0));
        plan.add(new TurretPosition(0, 200, 200, 0));
//        plan.add(new TurretPosition(0, 0, 0, 0));





        plan.add(new TurretPosition(0, load2, 0, 0));
        plan.add(new TurretPosition(0, load2, 620, 0));

//        plan.add(new WaitElement(1000));

        plan.add(new TurretPosition(0, load2, 620, 1));
        // Leadout
        plan.add(new TurretPosition(0, 275, 600, 1));

//        plan.add(new TurretPosition(0, 200, 200, 1));
        plan.add(new TurretPosition(-800, 700, 570, 1));
        plan.add(new WaitElement(pauseOverJunction));
        plan.add(new TurretPosition(-800, 700, 570, 0));
        plan.add(new TurretPosition(-300, 700, 570, 0));
        plan.add(new TurretPosition(0, 200, 200, 0));
//        plan.add(new TurretPosition(0, 0, 0, 0));





        plan.add(new TurretPosition(0, load3, 0, 0));
        plan.add(new TurretPosition(0, load3, 620, 0));

//        plan.add(new WaitElement(1000));

        plan.add(new TurretPosition(0, load3, 620, 1));
        // Leadout
        plan.add(new TurretPosition(0, 275, 600, 1));

//        plan.add(new TurretPosition(0, 200, 200, 1));
        plan.add(new TurretPosition(-800, 700, 570, 1));
        plan.add(new WaitElement(pauseOverJunction));
        plan.add(new TurretPosition(-800, 700, 570, 0));
        plan.add(new TurretPosition(-300, 700, 570, 0));
        plan.add(new TurretPosition(0, 200, 200, 0));
//        plan.add(new TurretPosition(0, 0, 0, 0));





        plan.add(new TurretPosition(0, load4, 0, 0));
        plan.add(new TurretPosition(0, load4, 620, 0));

//        plan.add(new WaitElement(1000));

        plan.add(new TurretPosition(0, load4, 620, 1));
        // Leadout
        plan.add(new TurretPosition(0, 275, 600, 1));

//        plan.add(new TurretPosition(0, 200, 200, 1));
        plan.add(new TurretPosition(-800, 700, 570, 1));
        plan.add(new WaitElement(pauseOverJunction));
        plan.add(new TurretPosition(-800, 700, 570, 0));
        plan.add(new TurretPosition(-300, 700, 570, 0));
        plan.add(new TurretPosition(0, 200, 200, 0));
//        plan.add(new TurretPosition(0, 0, 0, 0));





        plan.add(new TurretPosition(0, load5, 0, 0));
        plan.add(new TurretPosition(0, load5, 620, 0));

//        plan.add(new WaitElement(1000));

        plan.add(new TurretPosition(0, load5, 620, 1));
        // Leadout
        plan.add(new TurretPosition(0, 275, 600, 1));

        plan.add(new TurretPosition(0, 200, 200, 1));
        plan.add(new TurretPosition(-800, 700, 570, 1));
        plan.add(new WaitElement(pauseOverJunction));
        plan.add(new TurretPosition(-800, 700, 570, 0));
        plan.add(new TurretPosition(-300, 700, 570, 0));
        plan.add(new TurretPosition(0, 200, 200, 0));
        plan.add(new TurretPosition(0, 0, 0, 0));






//        plan.add(this.getDeliveryPlan(turret));


        return plan;
    }

    public MotionPlan getDeliveryPlan(Turret turret) {
        plan = new MotionPlan(turret);
        plan.add(new TurretPosition(0, 200, 200, 1));
        plan.add(new TurretPosition(-800, 700, 570, 1));
        plan.add(new WaitElement(3000));
        plan.add(new TurretPosition(-800, 700, 570, 0));
        plan.add(new TurretPosition(-300, 700, 570, 0));
        plan.add(new TurretPosition(0, 200, 200, 0));
        plan.add(new TurretPosition(0, 0, 0, 0));
        return plan;
    }

}
