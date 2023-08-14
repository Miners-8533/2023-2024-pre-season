package org.csa.miners.motionplan;

import org.csa.miners.Turret;
import org.csa.miners.TurretPosition;

/**
 * This plan allows an operator to save load and unload positions and repeatedly cycle between them
 */
public class LoadUnloadPlan extends MotionPlan {

    /**
     * The distance to offset the extension arm during the load lead in
     */
    public static final int LOAD_EXTENSION_LEADIN_DISTANCE = 2;

    /**
     * The distance to offset the elevation arm during the load lead in
     */
    public static final int LOAD_ELEVATION_LEADIN_DISTANCE = 0;

    /**
     * The distance to offset the extension arm during the load lead out
     */
    public final int LOAD_EXTENSION_LEADOUT_DISTANCE = 20;

    /**
     * The distance to offset the elevation arm during the load lead out
     */
    public final int LOAD_ELEVATION_LEADOUT_DISTANCE = 20;

    /**
     * The distance to offset the extension arm during the unload lead in
     */
    public static final int UNLOAD_EXTENSION_LEADIN_DISTANCE = 0;

    /**
     * The distance to offset the elevation arm during the unload lead in
     */
    public static final int UNLOAD_ELEVATION_LEADIN_DISTANCE = 20;

    /**
     * The distance to offset the extension arm during the unload lead out
     */
    public static final int UNLOAD_EXTENSION_LEADOUT_DISTANCE = 20;

    /**
     * The distance to offset the elevation arm during the unload lead out
     */
    public static final int UNLOAD_ELEVATION_LEADOUT_DISTANCE = 0;

    LoadUnloadPlan(Turret turret) {
        super(turret);
    }

    public LoadUnloadPlan(Turret turret, TurretPosition load, TurretPosition unload) {
        super(turret);
        this.add(this.buildCyclePlan(load, unload));
    }

    /**
     * Build a plan for a complete cycle to intake and deliver a cone
     * @param load The load position to intake the cone
     * @param unload The unload position to deliver the cone at
     * @return The MotionPlan describing the intake and delivery of the cone
     */
    MotionPlan buildCyclePlan(TurretPosition load, TurretPosition unload) {
        MotionPlan cyclePlan = new MotionPlan(this._turret);
        cyclePlan.add(this.getIntakePlan(load));
        cyclePlan.add(this.getLoadedHighLowTransitionHeight(load));
        cyclePlan.add(this.getDropoffPlan(unload));
        cyclePlan.add(this.getUnloadedHighLowTransitionHeight(load));
        return cyclePlan;
    }

    /**
     * Get a MotionPlan to approach and intake a cone
     *
     * @param load The position of the cone to load
     * @return A MotionPlan describing the load process
     */
    MotionPlan getIntakePlan(TurretPosition load) {
        MotionPlan intakePlan = new MotionPlan(this._turret);
        intakePlan.add(this.getLoadLeadInStart(load));
        intakePlan.add(this.getLoadLeadInEnd(load));
        intakePlan.add(this.getLoadEngage(load));
        intakePlan.add(this.getLoadLeadOutEnd(load));
        return intakePlan;
    }

    /**
     * Get a MotionPlan to approach and dropoff a cone
     *
     * @param unload The dropoff point to deliver the cone
     * @return A MotionPlan describing the unload process
     */
    MotionPlan getDropoffPlan(TurretPosition unload) {
        MotionPlan dropoffPlan = new MotionPlan(this._turret);
        dropoffPlan.add(this.getUnloadLeadInStart(unload));
        dropoffPlan.add(this.getUnloadLeadInEnd(unload));
        dropoffPlan.add(this.getUnloadDisengage(unload));
        dropoffPlan.add(this.getUnloadRetractEnd(unload));
        return dropoffPlan;
    }

    private TurretPosition getLoadLeadInStart(TurretPosition load) {
        return new TurretPosition(load.rotation(),
                load.elevation() + LOAD_ELEVATION_LEADIN_DISTANCE,
                load.extension() - LOAD_EXTENSION_LEADIN_DISTANCE,
                this._turret.getGripperOpenPosition()
        );
    }

    private TurretPosition getLoadLeadInEnd(TurretPosition load) {
        return new TurretPosition(load.rotation(),
                load.elevation(),
                load.extension(),
                this._turret.getGripperOpenPosition()
        );
    }

    private TurretPosition getLoadEngage(TurretPosition load) {
        return new TurretPosition(load.rotation(),
                load.elevation(),
                load.extension(),
                this._turret.getGripperClosePosition()
        );
    }

    private TurretPosition getLoadLeadOutEnd(TurretPosition load) {
        return new TurretPosition(load.rotation(),
                load.elevation() + LOAD_ELEVATION_LEADOUT_DISTANCE,
                load.extension() + LOAD_EXTENSION_LEADOUT_DISTANCE,
                this._turret.getGripperClosePosition()
        );
    }

    TurretPosition getLoadedHighLowTransitionHeight(TurretPosition load) {
        return new TurretPosition(load.rotation(),
                this._turret.getTransitionHeight(),
                load.extension(),
                this._turret.getGripperClosePosition()
        );
    }

    private TurretPosition getUnloadLeadInStart(TurretPosition unload) {
        return new TurretPosition(
                unload.rotation(),
                unload.elevation() + UNLOAD_ELEVATION_LEADIN_DISTANCE,
                unload.extension() + UNLOAD_EXTENSION_LEADIN_DISTANCE,
                this._turret.getGripperClosePosition()
        );
    }

    private TurretPosition getUnloadLeadInEnd(TurretPosition unload) {
        return new TurretPosition(
                unload.rotation(),
                unload.elevation(),
                unload.extension(),
                this._turret.getGripperClosePosition()
        );
    }

    private TurretPosition getUnloadDisengage(TurretPosition unload) {
        return new TurretPosition(
                unload.rotation(),
                unload.elevation(),
                unload.extension(),
                this._turret.getGripperOpenPosition()
        );
    }

    private TurretPosition getUnloadRetractEnd(TurretPosition unload) {
        return new TurretPosition(
                unload.rotation(),
                unload.elevation() + UNLOAD_ELEVATION_LEADOUT_DISTANCE,
                unload.extension() + UNLOAD_EXTENSION_LEADOUT_DISTANCE,
                this._turret.getGripperOpenPosition()
        );
    }


    TurretPosition getUnloadedHighLowTransitionHeight(TurretPosition load) {
        return new TurretPosition(load.rotation(),
                this._turret.getTransitionHeight(),
                load.extension(),
                this._turret.getGripperOpenPosition()
        );
    }
}
