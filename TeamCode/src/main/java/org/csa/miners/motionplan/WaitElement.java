package org.csa.miners.motionplan;

import org.csa.miners.Turret;

public class WaitElement implements MotionPlanElement {

    long pauseTime;
    boolean waiting = true;

    public WaitElement(long pauseTime) {
        this.pauseTime = pauseTime;
    }

    @Override
    public void initiateMotion(MotionPlan plan) {
        this.sleep(this.pauseTime);
        this.waiting = false;
    }

    @Override
    public boolean isComplete() {
        return !this.waiting;
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
