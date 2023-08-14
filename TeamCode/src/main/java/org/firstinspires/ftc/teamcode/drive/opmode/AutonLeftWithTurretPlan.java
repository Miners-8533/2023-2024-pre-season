package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.CameraDevice;

import org.csa.miners.PositionReport;
import org.csa.miners.motionplan.AutonMotionPlans;
import org.csa.miners.AutonTurret;
import org.csa.miners.motionplan.MotionPlan;
import org.csa.miners.Turret;
import org.csa.miners.motionplan.MotionPlanReport;
import org.csa.miners.powerplay.AutonPositions;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.List;


@Config
@Disabled
@Autonomous(name = "Auton Left W/ Turret", group = "auton")
public class AutonLeftWithTurretPlan extends LinearOpMode {

    //tf-vision variables
    float highestConf = 0;
    String highestConfString = " ";
    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/8533_powerplay_signal.tflite";
    private static final String[] LABELS = {
            "lemons",
            "minerals",
            "worms"
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
        plan = AutonMotionPlans.getLeftAutonMotionPlan(turret, AutonPositions.getMiddleJunction());
        report = new MotionPlanReport(this, plan);
        positionReport = new PositionReport(this, turret);

        turret.closeGripper();

        // heading is based on Unit Circle convention, positive degrees = CCW, negative = CW.
        // default/'0' heading is positive x (or 'right' when looking from F5 tile start position)

        //Pose2d endPos = new Pose2d(36,12, Math.toRadians(0)); // zone 2 default
        //Pose2d startPose = new Pose2d(36, 54, Math.toRadians(90));
        //Pose2d zoneThree = new Pose2d(12, 12, Math.toRadians(0)); forward 15"
        //Pose2d zoneTwo = new Pose2d(36,12, Math.toRadians(0)); back 7"
        //Pose2d zoneOne = new Pose2d(60,12, Math.toRadians(0)); back 31"


        Trajectory trajectoryT1 = drive.trajectoryBuilder(new Pose2d())
                .back(49.5)
                .build();

        Trajectory trajectoryT2 = drive.trajectoryBuilder(trajectoryT1.end().plus(new Pose2d(0,0,Math.toRadians(270))))
                .forward(8)
                .build();

        Trajectory parkZone1 = drive.trajectoryBuilder(trajectoryT2.end())
                .forward(13)
                .build();

        Trajectory parkZone2 = drive.trajectoryBuilder(trajectoryT2.end())
                .back(8)
                .build();

        Trajectory parkZone3 = drive.trajectoryBuilder(trajectoryT2.end())
                .back(31)
                .build();

        Trajectory targetZone = parkZone3;

        sleep(2000);

        waitForStart();

        /**
         * opModeActive - initialize
         */
        if (opModeIsActive()) {
            /**
             * opModeActive - Loop
             */
            runtime.reset();
            while (opModeIsActive() & runtime.seconds() < 5.0 & highestConfString != null) {
            //while ((opModeIsActive()) & (runtime.seconds() < 5.0)) {
                if (tfod != null) {
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Objects Detected", updatedRecognitions.size());
                        for (Recognition recognition : updatedRecognitions) {
                            double col = (recognition.getLeft() + recognition.getRight()) / 2 ;
                            double row = (recognition.getTop()  + recognition.getBottom()) / 2 ;
                            double width  = Math.abs(recognition.getRight() - recognition.getLeft()) ;
                            double height = Math.abs(recognition.getTop()  - recognition.getBottom()) ;

                            if (recognition.getConfidence() > highestConf){
                                highestConf = recognition.getConfidence();
                                highestConfString = recognition.getLabel();
                            }

                            telemetry.addData(""," ");
                            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100 );
                            telemetry.addData("- Position (Row/Col)","%.0f / %.0f", row, col);
                            //telemetry.addData("- Size (Width/Height)","%.0f / %.0f", width, height);
                            telemetry.addData("# Objects Detected", updatedRecognitions.size());
                        }
                        switch (highestConfString){
                            case "lemons":
                                targetZone = parkZone1;
                                break;
                            case "minerals":
                                targetZone = parkZone2;
                                break;
                            case "worms":
                                targetZone = parkZone3;
                                break;
                        }
                        telemetry.addData("Highest Confidence", "%s (%.0f %% Conf.)", highestConfString, highestConf * 100);
                        highestConf = 0; // reset value to 0 for each loop
                        telemetry.update();
                    }
                }
            }
            drive.followTrajectory(trajectoryT1);
            drive.turn(Math.toRadians(-90));
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

            runtime.reset();

            while(!plan.isPlanComplete() && opModeIsActive()) {
                plan.advance();
                report.render();
                positionReport.render();
                this.telemetry.update();
            }


//            while(!plan.isPlanComplete() && runtime.seconds()<15) {
//                plan.advance();
////                turret.updateMovements();
//            }

            drive.followTrajectory(targetZone);

        }
    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.72f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }
}