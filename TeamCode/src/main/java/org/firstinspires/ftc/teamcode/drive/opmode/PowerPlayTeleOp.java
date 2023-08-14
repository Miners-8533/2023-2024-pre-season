package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.csa.miners.TurretPower;
import org.csa.miners.motionplan.MotionPlan;
import org.csa.miners.Turret;
import org.csa.miners.TurretFactory;
import org.csa.miners.TurretPosition;
import org.csa.miners.motionplan.MotionPlanReport;
import org.csa.miners.motionplan.ReplayElement;
import org.csa.miners.motionplan.WaitForCloseElement;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This OpMode scans a single servo back and forward until Stop is pressed.
 * The code is structured as a LinearOpMode
 * INCREMENT sets how much to increase/decrease the servo position each cycle
 * CYCLE_MS sets the update period.
 *
 * This code assumes a Servo configured with the name "left_hand" as is found on a Robot.
 *
 * NOTE: When any servo position is set, ALL attached servos are activated, so ensure that any other
 * connected servos are able to move freely before running this test.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

public abstract class PowerPlayTeleOp extends LinearOpMode {

    // Tire Motors
    private DcMotor leftFrontWheel;
    private DcMotor leftRearWheel;
    private DcMotor rightFrontWheel;
    private DcMotor rightRearWheel;


    public static int HIGH_JUNCTION_HEIGHT = 925;  // reduced from 935 per drive team request -30
    public static int MID_JUNCTION_HEIGHT = 660;  // reduced from 700 per drive team request -40
    public static int LOW_JUNCTION_HEIGHT = 420;  // reduced from 470 per drive team request -50


    Turret turret;
    MotionPlan plan;

    TurretPosition loadPosition;
    TurretPosition intermediatePosition;
    TurretPosition unloadPosition;

    PowerDisplay powerDisplay;
    MotionPlanReport motionPlanReport;

    Boolean runningOnTestRig = false;

    @Override
    public void runOpMode() {

        // =========================================================================================
        // Initialization Code
        // This code belongs in the init() method but this is already defined as final.
        // Need to move this to a more appropriate class
        // =========================================================================================
        runningOnTestRig = TurretFactory.isRunningOnTestRig(hardwareMap);
        if (!runningOnTestRig) {
            this.initDriverCode();
        }

        Gamepad drivePad = this.getDriverPad();
        Gamepad turretPad = this.getTurretPad();
        Gamepad rotatePad = this.getRotationPad();
//        Gamepad drivePad = gamepad1;
//        Gamepad turretPad = gamepad2;
//        Gamepad rotatePad = drivePad;

        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);

        turret = TurretFactory.getTurretFromHardwareMap(this, hardwareMap);
        powerDisplay = new PowerDisplay(this);
        motionPlanReport = new MotionPlanReport(this, plan);

        loadPosition = new TurretPosition(0,0, 0, 0);
        unloadPosition = new TurretPosition(0,0, 0, 0);

        MotionPlan recorderPlan = new MotionPlan(turret);
        recorderPlan.add(loadPosition);
        recorderPlan.add(unloadPosition);
        plan = recorderPlan;

        // Wait for the start button
        telemetry.addData(">", "Press Start to scan Servo." );
        telemetry.update();

        waitForStart();


        // Scan servo till stop pressed.
        while(opModeIsActive()){
            if (!runningOnTestRig) {
                this.manageDriverInputs(drivePad);
            }


            if (turretPad.ps || drivePad.ps) {
                turret.allowOverride();
            } else {
                turret.disallowOverride();
            }

            if (turretPad.start) {
                // Special Mode


            } else if (turretPad.y || plan.isPlanComplete()) {
                plan.resetPlan();

            } else if (plan.isPlanInProgress()) {
                plan.advance();

            } else {

                if (turretPad.x) {
                    plan = recorderPlan;
                    plan.start();
                    plan.advance();
                }

                if (turretPad.b) {
                    turret.openGripper();
                    unloadPosition = turret.getCurrentPosition();
//                    plan = new LoadUnloadPlan(turret, loadPosition, unloadPosition);
                    recorderPlan = this.getMotionPlan(turret, loadPosition, unloadPosition);
                }

                if (turretPad.a) {
                    turret.closeGripper();
                    loadPosition = turret.getCurrentPosition();
//                    plan = new LoadUnloadPlan(turret, loadPosition, unloadPosition);
                    recorderPlan = this.getMotionPlan(turret, loadPosition, unloadPosition);
                }

                if (turretPad.back) {
                    MotionPlan homePlan = new MotionPlan(turret);
                    homePlan.add(turret.getCurrentPosition().setExtension(0).opened(turret));
                    homePlan.add(turret.getTransitPosition().opened(turret));
                    homePlan.add(turret.getHomePosition().opened(turret));
                    plan = homePlan;
                    plan.start();
                    plan.advance();
                }

                if (rotatePad.left_trigger > 0) {
                    turret.rotate(rotatePad.left_trigger);
                } else if (rotatePad.right_trigger > 0) {
                    turret.rotate(0 - rotatePad.right_trigger);
                } else if (rotatePad.left_bumper) {
                    turret.rotate(0.25);
                } else if (rotatePad.right_bumper) {
                    turret.rotate(0 - 0.25);
                } else {
                    turret.rotate(0);
                }

                if (turretPad.dpad_up) {
                    turret.setLiftHoldPosition(HIGH_JUNCTION_HEIGHT);
                }

                if (turretPad.dpad_right) {
                    turret.setLiftHoldPosition(MID_JUNCTION_HEIGHT);
                }

                if (turretPad.dpad_down) {
                    turret.setLiftHoldPosition(LOW_JUNCTION_HEIGHT);
                }

                if (false) { //turretPad.y) {
                    // The Logitech F310 does not support rumble
                    // https://www.logitechg.com/en-us/products/gamepads/f310-gamepad.940-000110.html
                    // https://www.amazon.com/Logitech-940-000110-Gamepad-F310/dp/B003VAHYQY
                    //
                    // The Logitech F710 supports rumble but is not explicitly "Legal"
                    // https://www.logitechg.com/en-us/products/gamepads/f710-wireless-gamepad.940-000117.html
                    // https://www.amazon.com/Logitech-Wireless-Nano-Receiver-Controller-Vibration/dp/B0041RR0TW/ref=sr_1_2?keywords=logitech+f510&qid=1668253571&sr=8-2

                    turretPad.rumble(Gamepad.RUMBLE_DURATION_CONTINUOUS);
                }


                turret.extendArm(-turretPad.right_stick_y);
                turret.raiseTurret(-turretPad.left_stick_y);

            }



            turret.reportTelemetry(telemetry);

            reportPosition("L", loadPosition);
            reportPosition("U", unloadPosition);

            telemetry.addData("Plan In Progress", "%s", plan.isPlanInProgress() ? "TRUE" : "FALSE");
            telemetry.addData("Plan Complete", "%s", plan.isPlanComplete() ? "TRUE" : "FALSE");
            telemetry.addData("Plan", "Step %d of %d", plan.getCurrentStepIndex(), plan.getTotalSteps());

            telemetry.addData(">", "TurretPad");
            reportGamepadTelemetry(turretPad);

            telemetry.addData(">", "RotatePad");
            reportGamepadTelemetry(rotatePad);

            powerDisplay.render();

            telemetry.addData(">", "Press Stop to end test." );
            telemetry.update();

            idle();
        }

        // Signal done;
        telemetry.addData(">", "Done");
        telemetry.update();

    }

    public MotionPlan getMotionPlan(Turret turret, TurretPosition loadPosition, TurretPosition target) {

        TurretPosition delta = new TurretPosition(50, 100, 100, 1);
        TurretPosition smallDelta = new TurretPosition(10, 10, 10, 1);

        MotionPlan returnPlan = new MotionPlan(turret, turret.getHighPower());
        int coneHeight = 125;


        returnPlan.add(turret.getTransitPosition().opened(turret), delta);

        returnPlan.add(loadPosition.opened(turret), smallDelta);
        returnPlan.add(loadPosition.closed(turret), smallDelta);
        returnPlan.add(new WaitForCloseElement(turret));

        returnPlan.add(loadPosition.closed(turret).offsetExtension(-20).offsetElevation(coneHeight), smallDelta);

        returnPlan.add(turret.getTransitPosition().closed(turret), delta);
        returnPlan.add(target.closed(turret).setExtension(0), delta);
        returnPlan.add(target.closed(turret));
        returnPlan.add(target.opened(turret), smallDelta);
        returnPlan.add(target.opened(turret).setExtension(0), delta);
        returnPlan.add(turret.getTransitPosition().opened(turret), delta);

        return returnPlan;
    }

    public void reportPosition(String caption, TurretPosition position) {
//        telemetry.addData(">", "GRIP |ELEV  |EXT  |ROT   ");
//        telemetry.addData(">", "GRIP |LLFT  |RLFT  |EXT   |ROT  ");
        telemetry.addData(caption, "%4.2f |%05d |      |%05d |%05d",
                position.grip(),
                position.elevation(),
                position.extension(),
                position.rotation()
        );
    }

    public void reportGamepadTelemetry(Gamepad gamepad) {
        telemetry.addData(">", "abxyC | ^>v< | LR");
        telemetry.addData(">", "%s%s%s%s%s | %s%s%s%s | %s%s",
                gamepad.a  ? "a" : "-",
                gamepad.b  ? "b" : "-",
                gamepad.x  ? "x" : "-",
                gamepad.y  ? "y" : "-",
                gamepad.ps ? "C" : "-",
                gamepad.dpad_up    ? "^" : "-",
                gamepad.dpad_right ? ">" : "-",
                gamepad.dpad_down  ? "v" : "-",
                gamepad.dpad_left  ? "<" : "-",
                gamepad.left_bumper  ? "L" : "-",
                gamepad.right_bumper ? "R" : "-"
        );

        telemetry.addData(">",  "LT   RT  | LX    LY  | RX    RY   ");
        telemetry.addData(">", "%4.2f %4.2f|%5.2f %5.2f|%5.2f %5.2f",
                gamepad.left_trigger,
                gamepad.right_trigger,
                gamepad.left_stick_x,
                gamepad.left_stick_y,
                gamepad.right_stick_x,
                gamepad.right_stick_y
                );
    }

    public void initDriverCode() {

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        leftFrontWheel = hardwareMap.get(DcMotor.class, "leftFront");
        leftRearWheel = hardwareMap.get(DcMotor.class, "leftRear");
        rightFrontWheel = hardwareMap.get(DcMotor.class, "rightFront");
        rightRearWheel = hardwareMap.get(DcMotor.class, "rightRear");

        // Most robots need the motors on one side to be reversed to drive forward.
        leftFrontWheel.setDirection(DcMotor.Direction.REVERSE);
        leftRearWheel.setDirection(DcMotor.Direction.REVERSE);
        rightFrontWheel.setDirection(DcMotor.Direction.FORWARD);
        rightRearWheel.setDirection(DcMotor.Direction.FORWARD);
    }

    private double drivePowerModifier = 0.5;
    private final double defaultDrivePowerModifier = 0.5;

    double getDrivePowerModifier() {
        return this.drivePowerModifier;
    }

    public void setDrivePowerModifier(double powerModifier) {
        this.drivePowerModifier = powerModifier;
    }

    public void reduceDrivePowerModifier() {
        this.setDrivePowerModifier(this.getDrivePowerModifier()*0.5);
    }

    public void increaseDrivePowerModifier() {
        this.setDrivePowerModifier(this.getDrivePowerModifier()*2);
    }

    public void manageDriverInputs(Gamepad gamePad) {

        if (gamePad.dpad_up) {
            this.increaseDrivePowerModifier();
        } else if (gamePad.dpad_down) {
            this.reduceDrivePowerModifier();
        } else if (gamePad.back) {
            this.setDrivePowerModifier(this.defaultDrivePowerModifier);
        }

        // Driver Code
        double y = -gamePad.left_stick_y; // Remember, this is reversed!
//        double strafe = gamePad.left_stick_x * 1.1; // Counteract imperfect strafing
        double strafe_left = gamePad.left_trigger * 1.1; // Counteract imperfect strafing
        double strafe_right = gamePad.right_trigger * 1.1; // Counteract imperfect strafing
        double strafe = strafe_left > 0 ? -strafe_left : strafe_right;
        double rx = gamePad.right_stick_x;
        double ry = gamePad.right_stick_y;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(strafe) + Math.abs(rx), 1);


        double leftFrontPower = (y + strafe + rx) / denominator;
        double leftRearPower = (y - strafe + rx) / denominator;
        double rightFrontPower = (y - strafe - rx) / denominator;
        double rightRearPower = (y + strafe - rx) / denominator;

        double driveModifier = this.getDrivePowerModifier();

        // Send calculated power to wheels
        leftFrontWheel.setPower(leftFrontPower*driveModifier);
        rightFrontWheel.setPower(rightFrontPower*driveModifier);
        leftRearWheel.setPower(leftRearPower*driveModifier);
        rightRearWheel.setPower(rightRearPower*driveModifier);
    }

    public Gamepad getDriverPad() {
        return gamepad1;
    }

    public Gamepad getTurretPad() {
        return gamepad2;
    }

    public abstract Gamepad getRotationPad();

    public class PowerDisplay {

        private PowerPlayTeleOp teleOp;

        public PowerDisplay(PowerPlayTeleOp teleOp) {
            this.teleOp = teleOp;
        }

        private Telemetry getTelemetry() {
            return this.teleOp.telemetry;
        }

        private void renderPower(String caption, Telemetry telemetry, TurretPower power) {
            telemetry.addData(caption, "%4.2f | %4.2f | %4.2f",
                    power.getElevation(),
                    power.getExtension(),
                    power.getRotation()
            );
        }

        public void render() {
            Telemetry telemetry = this.getTelemetry();
            telemetry.addLine("Power States");

            telemetry.addData("       ", "ELEV | EXT  | ROT   ");

            this.renderPower("DEF POW", telemetry, this.teleOp.turret.getDefaultPower());
            this.renderPower(" HI POW", telemetry, this.teleOp.turret.getHighPower());
        }
    }

}
