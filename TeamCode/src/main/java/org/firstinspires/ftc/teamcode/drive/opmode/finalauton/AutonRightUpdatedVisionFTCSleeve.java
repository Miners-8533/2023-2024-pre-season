package org.firstinspires.ftc.teamcode.drive.opmode.finalauton;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.csa.miners.AutonTurret;
import org.csa.miners.PositionReport;
import org.csa.miners.Turret;
import org.csa.miners.motionplan.AutonMotionPlans;
import org.csa.miners.motionplan.MotionPlan;
import org.csa.miners.motionplan.MotionPlanReport;
import org.csa.miners.powerplay.AutonPositions;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.List;


//@Config
@Autonomous(name = "FTC Sleeve Right", group = "FTC Vision")
public class AutonRightUpdatedVisionFTCSleeve extends LinearOpMode {

    //tf-vision variables
    float highestConf = 0;
    String highestConfString = " ";
    private static final String TFOD_MODEL_ASSET = "PowerPlay.tflite";
    private static final String[] LABELS = {
            "1 Bolt",
            "2 Bulb",
            "3 Panel"
    };
    private static final String VUFORIA_KEY =
            "AafAKQf/////AAABmXNkaNJ/ZkGJltimPGAalr0fXbX9d/WA5O/2mU+BOR/PAk7XQOxkhPjGRNAw2vQPAf9/14bBEsZE00s8Kfam1lgxj0BvHBgqdlfR7wF0w8FoONj/zIdfcypS8IWpzmrtUxS66ecsmAgGpm0vNbxXKWB47Ls7am0ge3K9BWY8xY65bMFt/qTVHjLEXFtoRvqT5+7c0UuBHbFawfp62ZmEYjmCu0pOykLyXvylg3oC3GOf1PTf7XvLdb0TqXpVPhlTpC2TJTcuF0XmDzWaYAKML65p17DqawNc3Sbiur3j2Tsdm0XyveBlA6Vc5OPzO7KJqOtrctTmDxy4ILUAOCDZKqWHYx9w1t+UMaeWCqEWKLAA";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    public ElapsedTime runtime = new ElapsedTime();

    Turret turret;
    MotionPlan plan;
    MotionPlanReport report;
    PositionReport positionReport;

    public double getRotationToCones() {
        return 90;
    }

    public void runOpMode() throws InterruptedException {
        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.7, 4.0/3.0);
            tfod.setClippingMargins(500,100,500,100);
        }

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        turret = AutonTurret.AutonTurretFromMap(hardwareMap);
        plan = AutonMotionPlans.getRightAutonMotionPlan(turret, AutonPositions.getMiddleJunction());
        report = new MotionPlanReport(this, plan);
        positionReport = new PositionReport(this, turret);

        turret.closeGripper();

        double toScoringLocFromStartTraj = 9.5; // distance to travel towards cone stack from end of trajectory1

        Trajectory trajectoryT1 = drive.trajectoryBuilder(new Pose2d())
                .back(49.5)
                .build();

        Trajectory trajectoryT2 = drive.trajectoryBuilder(trajectoryT1.end().plus(new Pose2d(0,0,Math.toRadians(getRotationToCones()))))
                .forward(toScoringLocFromStartTraj)
                .build();

        Trajectory parkZone3 = drive.trajectoryBuilder(trajectoryT2.end())
                .forward(10.5)
                .build();

        Trajectory parkZone2 = drive.trajectoryBuilder(trajectoryT2.end())
                .back(toScoringLocFromStartTraj)
                .build();

        Trajectory parkZone1 = drive.trajectoryBuilder(trajectoryT2.end())
                .back(33)
                .build();

        Trajectory targetZone = null;

        waitForStart();

        /**
         * opModeActive - initialize
         */
        if (opModeIsActive()) {
            /**
             * opModeActive - Loop
             */
            runtime.reset();
            while (opModeIsActive() & runtime.seconds() < 2) {
            //while ((opModeIsActive()) & (runtime.seconds() < 5.0)) {
                if (tfod != null) {
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Objects Detected", updatedRecognitions.size());
                        telemetry.update();
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getConfidence() > highestConf){
                                highestConf = recognition.getConfidence();
                                highestConfString = recognition.getLabel();
                            }

                        }
                        switch (highestConfString){
                            case "1 Bolt":
                                targetZone = parkZone1;
                                break;
                            case "2 Bulb":
                                targetZone = parkZone2;
                                break;
                            case "3 Panel":
                                targetZone = parkZone3;
                                break;
                            default:
                                targetZone = parkZone2;
                        }
                        telemetry.addData("# Objects Detected", updatedRecognitions.size());
                        telemetry.addData("Highest Confidence", "%s (%.0f %% Conf.)", highestConfString, highestConf * 100);
                        telemetry.update();
                        highestConf = 0; // reset value to 0 for each loop
                    }
                }
            }
            drive.followTrajectory(trajectoryT1);
            drive.turn(Math.toRadians(getRotationToCones()));
            drive.followTrajectory(trajectoryT2);


            // add turret code
            if (plan.isPlanInProgress() && plan.isPlanComplete()) {
                // Do Nothing
            } else if (plan.isPlanInProgress()) {
                plan.advance();
            } else {
                plan.start();
                plan.advance();
            }

            while(!plan.isPlanComplete() && opModeIsActive() && runtime.seconds()<27) {
                telemetry.addData("Time In Process", runtime.seconds());
                plan.advance();
                report.render();
                positionReport.render();
                telemetry.addData("Highest Confidence", "%s (%.0f %% Conf.)", highestConfString, highestConf * 100);
                this.telemetry.update();
            }


//            while(!plan.isPlanComplete() && runtime.seconds()<15) {
//                plan.advance();
////                turret.updateMovements();
//            }

            MotionPlan homePlan = new MotionPlan(turret, turret.getHighPower());
            homePlan.add(turret.getCurrentPosition().setExtension(0).closed(turret));
            homePlan.add(turret.getTransitPosition().closed(turret));
            homePlan.add(turret.getHomePosition().closed(turret));
            plan = homePlan;
            plan.start();
            plan.advance();
            drive.followTrajectory(targetZone);

            while(opModeIsActive()) {
                telemetry.addData("Time In Process", runtime.seconds());
                plan.advance();
                report.render();
                positionReport.render();
//                telemetry.addData("# Objects Detected", updatedRecognitions.size());
                telemetry.addData("Highest Confidence", "%s (%.0f %% Conf.)", highestConfString, highestConf * 100);
                this.telemetry.update();
            }
        }
    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.70f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
}