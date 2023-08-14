package org.csa.miners.motionplan;

import org.csa.miners.Turret;
import org.csa.miners.TurretPosition;
import org.csa.miners.powerplay.AutonPositions;

/** MotionPlan factories for the 2022 Auton */
public class AutonMotionPlans {

    /** Create the MotionPlan for the Left-side starting positions */
    public static MotionPlan getAutonMotionPlanLeftFirstQualifier(Turret turret) {

        MotionPlan plan = new MotionPlan(turret);

        plan.add(new TurretPosition(0, -200, 0, 1));
        plan.add(new TurretPosition(0, -200, 200, 1));
        plan.add(new TurretPosition(1150, -950, 600, 1));
        plan.add(new TurretPosition(1150, -950, 600, 0));
        plan.add(new TurretPosition(300, -950, 600, 0));
        plan.add(new TurretPosition(0, -200, 200, 0));
        plan.add(new TurretPosition(0, 0, 0, 0));

        return plan;
    }


    /** Create the MotionPlan for the Right-side starting positions */
    public static MotionPlan getAutonMotionPlanRight(Turret turret) {

        MotionPlan plan = new MotionPlan(turret);

        plan.add(new TurretPosition(0, -200, 0, 1));
        plan.add(new TurretPosition(0, -200, 200, 1));
        plan.add(new TurretPosition(-1150, -950, 600, 1));
        plan.add(new TurretPosition(-1150, -950, 600, 0));
        plan.add(new TurretPosition(-300, -950, 600, 0));
        plan.add(new TurretPosition(0, -200, 200, 0));
        plan.add(new TurretPosition(0, 0, 0, 0));

        return plan;
    }

    /** Create the MotionPlan for the Left-side starting positions */
    public static MotionPlan getLeftAutonMotionPlan(Turret turret, TurretPosition target) {

        MotionPlan plan = new MotionPlan(turret, turret.getHighPower());

        plan.add(AutonMotionPlans.getPreloadPlan(turret, AutonPositions.getMiddleJunction()));
        plan.add(AutonMotionPlans.getJunctionPlan(turret, AutonPositions.getMiddleJunction()));

        return plan;
    }

    /** Create the MotionPlan for the Left-side starting positions */
    public static MotionPlan getRightAutonMotionPlan(Turret turret, TurretPosition target) {

        MotionPlan plan = new MotionPlan(turret, turret.getHighPower());

        TurretPosition pickupPosition = AutonPositions.getTopLoadConeRight().offsetExtension(25);
        TurretPosition reversedTarget = target.setRotation(-1*target.rotation());

        plan.add(AutonMotionPlans.getPreloadPlan(turret, reversedTarget));
        plan.add(AutonMotionPlans.getJunctionPlan(turret, pickupPosition, reversedTarget));

        return plan;
    }

    /**
     * Gets the {@link MotionPlan} to deliver the preloaded cone to the proposed junction
     *
     * @param turret The turret that will execute the plan
     * @param target The position to target for delivery
     * @return The MotionPlan to deliver the preloaded cone to the target
     */
    public static MotionPlan getPreloadPlan(Turret turret, TurretPosition target) {
        MotionPlan plan = new MotionPlan(turret);

        TurretPosition delta = new TurretPosition(50, 100, 100, 1);
        TurretPosition smallDelta = new TurretPosition(10, 10, 10, 1);

        plan.add(turret.getTransitPosition().closed(turret), delta);
        plan.add(target.closed(turret).setExtension(0), delta);
        plan.add(target.closed(turret));
        plan.add(target.opened(turret), smallDelta);
        plan.add(target.opened(turret).setExtension(0), delta);
//        plan.add(turret.getCurrentPosition().setExtension(0).opened(turret));
        plan.add(turret.getTransitPosition().opened(turret), delta);

        return plan;
    }

    public static MotionPlan getDeliveryPlan(Turret turret, TurretPosition target) {
        MotionPlan plan = new MotionPlan(turret);

        plan.add(target.closed(turret).setExtension(0));
        plan.add(target.closed(turret));
        plan.add(target.opened(turret));

        return plan;
    }

    public static MotionPlan getJunctionPlan(Turret turret, TurretPosition target) {
        TurretPosition loadPosition = AutonPositions.getTopLoadConeLeft();
        return AutonMotionPlans.getJunctionPlan(turret, loadPosition,target);
    }
    public static MotionPlan getJunctionPlan(Turret turret, TurretPosition initialLoadPosition, TurretPosition target) {
//        MotionPlan plan = new MotionPlan(turret, turret.getHighPower());
        MotionPlan plan = new MotionPlan(turret);

        TurretPosition loadPosition = initialLoadPosition;
        TurretPosition delta = new TurretPosition(50, 100, 100, 1);
        TurretPosition smallDelta = new TurretPosition(10, 10, 10, 1);

        int coneHeight = 125;
//        int stackedConeCount = 5;
        // We only have time for 2 cones
        int stackedConeCount = 2;
        int stackedConeAdjustment = -30;

//        plan.add(turret.getHomePosition().opened(turret));

        for (int ii = 0 ; ii < stackedConeCount ; ii++) {
            loadPosition = loadPosition.offsetElevation(stackedConeAdjustment);
            plan.add(loadPosition.opened(turret), smallDelta);
            plan.add(loadPosition.closed(turret), smallDelta);
            plan.add(new WaitForCloseElement(turret));

            plan.add(loadPosition.closed(turret).offsetExtension(-20).offsetElevation(coneHeight), smallDelta);

            plan.add(turret.getTransitPosition().closed(turret), delta);
            plan.add(target.closed(turret).setExtension(0), delta);
            plan.add(target.closed(turret));
            plan.add(target.opened(turret), smallDelta);
            plan.add(target.opened(turret).setExtension(0), delta);
//            plan.add(turret.getCurrentPosition().setExtension(0).opened(turret));
            plan.add(turret.getTransitPosition().opened(turret), delta);

        }
        return plan;
    }
}
