package org.csa.miners.motionplan;

public class ReplayElement implements MotionPlanElement {

    long pauseTime;

    @Override
    public void initiateMotion(MotionPlan plan) {
        plan.resetPlan();
        plan.start();
    }

    @Override
    public boolean isComplete() {
        return true;
    }
}
