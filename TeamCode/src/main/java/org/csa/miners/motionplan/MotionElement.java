package org.csa.miners.motionplan;

import org.csa.miners.Turret;
import org.csa.miners.TurretPosition;

public class MotionElement implements MotionPlanElement {

    TurretPosition position;
    Turret turret;

    public MotionElement(Turret turret, TurretPosition position) {
        this.position = position;
        this.turret = turret;
    }

    @Override
    public void initiateMotion(MotionPlan plan) {
        this.turret.moveTo(this.position, plan.defaultPlanPower);
        this.waitForMotionToStart();
    }

    @Override
    public boolean isComplete() {
        return !this.turret.isBusy();
    }

    private void waitForMotionToStart() {
        this.sleep(100);
    }

    /**
     * Sleeps for the given amount of milliseconds, or until the thread is interrupted. This is
     * simple shorthand for the operating-system-provided {@link Thread#sleep(long) sleep()} method.
     *
     * @param milliseconds amount of time to sleep, in milliseconds
     * @see Thread#sleep(long)
     */
    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
