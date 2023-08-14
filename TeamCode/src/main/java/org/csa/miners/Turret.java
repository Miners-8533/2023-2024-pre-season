package org.csa.miners;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Turret {

    /** The gripper position index that opens it */
    private double GRIPPER_OPEN_POSITION = 0;

    /** The gripper position index that closes it */
    private double GRIPPER_CLOSE_POSITION = 1;

    /**
     * The turret elevation where turret rotation limits change
     */
    private int HIGH_LOW_TRANSIT_HEIGHT = 200;

    /** The motor lifting the Left side of the Robot Turret */
    public DcMotor liftLeft;

    /** The motor lifting the Right side of the Robot Turret */
    public DcMotor liftRight;

    /** The motor controlling the extension arm */
    public DcMotor extensionArm;

    /** The motor controlling the Turret Rotation */
    public DcMotor turretRotate;

    /** The servo driving the Gripper */
    Servo gripper;

    /** The Left Lift power adjustment to ensure that the sides remain aligned */
    public double LIFT_LEFT_POWER_ADJUST  = 0.20;

    /** The Right Lift power adjustment to ensure that the sides remain aligned */
    public double LIFT_RIGHT_POWER_ADJUST = 0.20;

    /** The extension arm power adjustment to limit motor speed */
    public double EXTEND_POWER_ADJUST = 0.20;

    /** Maximum allowed speed for the turret rotation motor */
    public double MAX_ROTATION_SPEED = 0.20;

    public int ROTATION_SAFE_ELEVATION = -30;

    /** The Turret CCW Soft Limit */
    double ROTATION_SAFE_LOW_MAX = 1400;

    /** The Turret CW Soft Limit */
    double ROTATION_SAFE_LOW_MIN = -1400;

    /** Motion outside the safety envelope has been permitted
     *
     * <p>
     * SAFETY NOTE: This MUST be private and managed within this object
     * </p>
     */
    private boolean overrideEnvelopeSafety = false;

    /**
     * The turret rotation to try to attain
     */
    int rotation_target = 0;

    /**
     * The allowable rotational delta
     */
    int rotation_delta = 20;

    boolean rotating_to_target = false;

    /**
     * The turret extension to try to attain
     */
    int extension_target = 0;

    /**
     * The allowable extension delta
     */
    int extension_delta = 45;

    boolean extending_to_target = false;

    boolean hold_turret = false;

    int turret_hold_position = 100;
    int elevation_delta = 2;

    boolean elevating_to_target = false;

    public TurretPosition home = new TurretPosition(0,0,0, 0);

    /**
     * Get the {@link Turret} Transition Position.  This is the position where the Turret can safely
     * move from the lower restricted working envelope to the upper envelope
     *
     * @return The transition location as a {@link TurretPosition}
     */
    public TurretPosition getTransitPosition() {
        return new TurretPosition(0, this.getTransitionHeight(),0, this.GRIPPER_OPEN_POSITION);
    }


    /**
     * Get the {@link Turret} Home Position.  This is the lowest position the robot can have.
     * We assume the gripper is open.
     * @return The home location as a {@link TurretPosition}
     */
    public TurretPosition getHomePosition() {
        return new TurretPosition(0, 0,0, this.GRIPPER_OPEN_POSITION);
    }

    public Turret(DcMotor liftLeft, DcMotor liftRight, DcMotor extensionArm, DcMotor turretRotate,
                  Servo gripperServo){
        this.liftLeft = liftLeft;
        this.liftRight = liftRight;
        this.extensionArm = extensionArm;
        this.turretRotate = turretRotate;
        this.gripper = gripperServo;
    }

    /**
     * Raise the turret at a defined rate
     *
     * @param liftRate The percentage of the max rate of speed to raise the turret
     */
    public void raiseTurret(double liftRate) {

        if (Math.abs(liftRate) < 0.1 && !hold_turret ) {
            hold_turret = true;
            turret_hold_position = this.getTurretHeight();
        }

        if (Math.abs(liftRate) < 0.1) {
            liftLeft.setTargetPosition(turret_hold_position);
            liftRight.setTargetPosition(turret_hold_position);
            liftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftLeft.setPower(LIFT_LEFT_POWER_ADJUST);
            liftRight.setPower(LIFT_RIGHT_POWER_ADJUST);
        } else {
            hold_turret = false;
            int liftPositionAdjust = 250;

            liftLeft.setTargetPosition(liftLeft.getCurrentPosition() + liftPositionAdjust*Integer.signum((int) Math.round(liftRate)));
            liftRight.setTargetPosition(liftLeft.getCurrentPosition() + liftPositionAdjust*Integer.signum((int) Math.round(liftRate)));
            liftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftLeft.setPower(LIFT_LEFT_POWER_ADJUST);
            liftRight.setPower(LIFT_RIGHT_POWER_ADJUST);
        }

    }

    public void setLiftHoldPosition(int position) {
        hold_turret = true;
        turret_hold_position = position;
    }

    /**
     * Extends the Turret Arm at a rate defined as a percentage of total power
     *
     * @param extendRate The rate to extend at (as percentage of total power)
     */
    public void extendArm(double extendRate) {
        double extendPower;
        if (this.isMotionAllowed()) {
            extendPower = extendRate * EXTEND_POWER_ADJUST;
        } else {
            extendPower = 0;
        }

        extensionArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        extensionArm.setPower(extendPower);
    }

    /**
     * Rotate the Turret Assembly
     *
     * @param rotationRate The rate to extend at (as percentage of total power)
     */
    public void rotate(double rotationRate) {
        double rotatePower;
        if (this.isMotionAllowed()) {
            rotatePower = rotationRate * MAX_ROTATION_SPEED;
        } else {
            rotatePower = 0;
        }

        turretRotate.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turretRotate.setPower(rotatePower);
    }

    /** Close the Gripper at the End Effector */
    public void closeGripper() {
        gripper.setPosition(GRIPPER_CLOSE_POSITION);
    }

    /** Open the Gripper at the End Effector */
    public void openGripper() {
        gripper.setPosition(GRIPPER_OPEN_POSITION);
    }

    public boolean isGripperClosed() {
        return this.gripper.getPosition() == this.getGripperClosePosition();
    }

    public double getGripperClosePosition() {
        return GRIPPER_CLOSE_POSITION;
    }

    public double getGripperOpenPosition() {
        return GRIPPER_OPEN_POSITION;
    }



    public int getTransitionHeight() {
        return HIGH_LOW_TRANSIT_HEIGHT;
    }

    /** Send the robot to the transport position
     *
     * The transport position is where:
     * <ul>
     *     <li>The Gripper is facing front and center</li>
     *     <li>The Turret is completely lowered</li>
     *     <li>The robot arm is fully retracted</li>
     * </ul>
     */
    public void sendToTransport() {
        moveToHighHome();

        // Lower to 0
    }

    public TurretPosition getHome() {
        return home;
    }

    public void moveToHighHome() {
        // Turret Rotation to 0
        // Extension to
    }

    /**
     * Report the current status of the Turret through FTC telemetry
     *
     * @param telemetry The Telemetry object to report through
     *
     * NOTE: This method probably belongs in a class other than Turret.
     */
    public void reportTelemetry(Telemetry telemetry) {
        if (this.outsideEnvelope()) {
            telemetry.addData("WARNING", "TURRET OUTSIDE ENVELOPE");
        }
        telemetry.addData(">", "GRIP |LLFT  |RLFT  |EXT   |ROT  | TB | ");
        telemetry.addData("C", "%4.2f |%05d |%05d |%05d |%05d | %s%s",
                gripper.getPosition(),
                liftLeft.getCurrentPosition(),
                liftRight.getCurrentPosition(),
                extensionArm.getCurrentPosition(),
                turretRotate.getCurrentPosition(),
                this.turretRotate.isBusy() ? "#" : "-",
                this.isBusy() ? "#" : "-"
        );
        telemetry.addData("T", "%4.2f |%05d |%05d |%05d |%05d | %s%s",
                0.0,
                this.turret_hold_position,
                0,
                this.extension_target,
                this.rotation_target,
                this.turretRotate.isBusy() ? "#" : "-",
                this.isBusy() ? "#" : "-"
        );

    }

    /**
     * Verify that motion to a point stays within the working envelope
     *
     * @return True if the movement is allowed, False if the motion is not allowed
     */
    public boolean insideEnvelope() {
        TurretPosition currentPosition = this.getCurrentPosition();
        return currentPosition.rotation() > this.ROTATION_SAFE_LOW_MIN &&
                currentPosition.rotation() < this.ROTATION_SAFE_LOW_MAX;
    }

    public boolean pointIsInsideEnvelope(TurretPosition targetPosition) {
        return targetPosition.rotation() > this.ROTATION_SAFE_LOW_MIN &&
                targetPosition.rotation() < this.ROTATION_SAFE_LOW_MAX;
    }

    public boolean isMotionAllowed() {
        return this.overrideEnvelopeSafety || this.insideEnvelope();
    }

    public boolean outsideEnvelope() {
        return !this.insideEnvelope();
    }

    public void allowOverride() {
        this.overrideEnvelopeSafety = true;
    }

    public void disallowOverride() {
        this.overrideEnvelopeSafety = false;
    }

    public void moveTo(TurretPosition targetPosition) {
//        TurretPower turretPower = new TurretPower(this.MAX_ROTATION_SPEED, LIFT_LEFT_POWER_ADJUST, EXTEND_POWER_ADJUST, 0);
        TurretPower turretPower = this.getDefaultPower();
        this.moveTo(targetPosition, turretPower);
    }

    public void moveTo(TurretPosition targetPosition, TurretPower power) {
        if (this.isMotionAllowed() && this.pointIsInsideEnvelope(targetPosition)) {
            this.hold_turret = false;
            this.turret_hold_position = targetPosition.elevation();
            this.rotation_target = targetPosition.rotation();
            this.extension_target = targetPosition.extension();

            gripper.setPosition(targetPosition.grip());
            liftLeft.setTargetPosition(targetPosition.elevation());
            liftRight.setTargetPosition(targetPosition.elevation());
            extensionArm.setTargetPosition(targetPosition.extension());
            turretRotate.setTargetPosition(targetPosition.rotation());


            liftLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            liftRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            extensionArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            turretRotate.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            liftLeft.setPower(power.getElevation());
            liftRight.setPower(power.getElevation());
            extensionArm.setPower(power.getExtension());
            turretRotate.setPower(power.getRotation());
        }

    }


    /**
     * Gets the current height of the Turret
     *
     * @return The rotational index of the left turret motor encoder
     */
    private int getTurretHeight() {
        return liftLeft.getCurrentPosition();
    }

    /**
     * Get the current rotational position of the Turret
     *
     * @return The rotational index of the turret rotational motor encoder
     */
    private int getTurretRotation() {
        return this.turretRotate.getCurrentPosition();
    }

    /**
     * Get the current status of the Gripper
     *
     * @return The positional value of the Gripper
     */
    private double getGripperPosition() {
        return this.gripper.getPosition();
    }

    /**
     * Get the current position of the Extension Arm
     *
     * @return The rotational index of the Extension Arm motor encoder
     */
    private int getExtensionPosition() {
        return this.extensionArm.getCurrentPosition();
    }

    public TurretPosition getCurrentPosition() {
        return new TurretPosition(
                this.getTurretRotation(),
                this.getTurretHeight(),
                this.getExtensionPosition(),
                this.getGripperPosition()
        );
    }

    public boolean isBusy() {
//        boolean rotation_busy = !(Math.abs(this.rotation_target - this.turretRotate.getCurrentPosition()) < this.rotation_delta);
//        boolean extension_busy = !(Math.abs(this.extension_target - this.extensionArm.getCurrentPosition()) < this.extension_delta);

        return liftLeft.isBusy()
                || liftRight.isBusy()
                || turretRotate.isBusy()
                || extensionArm.isBusy();
//                || extension_busy
//                || rotation_busy;


//        boolean rotation_in_position = (Math.abs(this.rotation_target - this.turretRotate.getCurrentPosition()) < this.rotation_delta);
//        boolean extension_in_position = (Math.abs(this.extension_target - this.extensionArm.getCurrentPosition()) < this.extension_delta);
//        boolean lift_in_hold_position = (Math.abs(this.turret_hold_position - this.liftLeft.getCurrentPosition()) < this.elevation_delta);
//        //boolean lift_in_targeted_position = (Math.abs(this.liftLeft.getTargetPosition() - this.liftLeft.getCurrentPosition()) < this.elevation_delta);
//
//        return
//                (!extension_in_position && this.extending_to_target);
////        return //!lift_in_targeted_position ||
////                !lift_in_hold_position ||
////                !extension_in_position ||
////                !rotation_in_position;
    }

    public TurretPower getDefaultPower() {
        return new TurretPower(this.MAX_ROTATION_SPEED, LIFT_LEFT_POWER_ADJUST, EXTEND_POWER_ADJUST, 0);
    }

    public TurretPower getHighPower() {
        return new TurretPower(this.MAX_ROTATION_SPEED, LIFT_LEFT_POWER_ADJUST, EXTEND_POWER_ADJUST, 0);
    }
}
