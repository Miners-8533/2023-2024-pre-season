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
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

@Disabled
@Autonomous(name = "Auton Right w/OpenCV Color Detection", group = "visiontest")
public class AutonRightUpdatedVisionOpenCVColorDetection extends LinearOpMode {

    static final int STREAM_WIDTH = 320;
    static final int STREAM_HEIGHT = 240;
    OpenCvCamera phoneCam;
    SamplePipeline pipeline;

    enum ParkZone {
        LEFT,
        CENTER,
        RIGHT
    }
    public ElapsedTime runtime = new ElapsedTime();

    Turret turret;
    MotionPlan plan;
    MotionPlanReport report;
    PositionReport positionReport;

    public void runOpMode() throws InterruptedException {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        pipeline = new SamplePipeline();
        phoneCam.setPipeline(pipeline);
        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                phoneCam.startStreaming(STREAM_WIDTH,STREAM_HEIGHT, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        turret = AutonTurret.AutonTurretFromMap(hardwareMap);
        plan = AutonMotionPlans.getLeftAutonMotionPlan(turret, AutonPositions.getMiddleJunction());
        report = new MotionPlanReport(this, plan);
        positionReport = new PositionReport(this, turret);

        turret.closeGripper();

        double toScoringLocFromStartTraj = 10; // distance to travel towards cone stack from end of trajectory1

        Trajectory trajectoryT1 = drive.trajectoryBuilder(new Pose2d())
                .back(49.5)
                .build();

        Trajectory trajectoryT2 = drive.trajectoryBuilder(trajectoryT1.end().plus(new Pose2d(0,0,Math.toRadians(90))))
                .forward(toScoringLocFromStartTraj)
                .build();

        Trajectory parkZone3 = drive.trajectoryBuilder(trajectoryT2.end())
                .forward(8)
                .build();

        Trajectory parkZone2 = drive.trajectoryBuilder(trajectoryT2.end())
                .back(toScoringLocFromStartTraj)
                .build();

        Trajectory parkZone1 = drive.trajectoryBuilder(trajectoryT2.end())
                .back(31.5)
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
            while (opModeIsActive() & runtime.seconds() < 2.0) {
                if (targetZone == null) {
                    switch (pipeline.getParkZoneLoc()){
                        case LEFT:
                            targetZone = parkZone1;
                            break;
                        case RIGHT:
                            targetZone = parkZone3;
                            break;
                        case CENTER:
                            targetZone = parkZone2;
                            break;
                        default:
                            targetZone = parkZone3;
                    }
                }
                telemetry.addData("parkZone destination", pipeline.getParkZoneLoc());
                telemetry.update();
            }
            drive.followTrajectory(trajectoryT1);
            drive.turn(Math.toRadians(90));
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
                telemetry.addData("parkZone destination", pipeline.getParkZoneLoc());
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
                telemetry.addData("parkZone destination", pipeline.getParkZoneLoc());
                this.telemetry.update();
            }
        }
    }

    public class SamplePipeline extends OpenCvPipeline {

        private volatile ParkZone parkZone = ParkZone.CENTER; //default

        public Scalar blueMin = new Scalar(30,0,82);
        public Scalar blueMax = new Scalar(66,128,255);

        public Scalar redMin = new Scalar(52,171,103);
        public Scalar redMax = new Scalar(255,255,121);

        public Scalar yellowMin = new Scalar(113,92,0);
        public Scalar yellowMax = new Scalar(255,162,86);

        private Mat ycrcbMat = new Mat();
        private Mat maskedInputMat = new Mat();


        private Mat redMask = new Mat();
        private Mat blueMask = new Mat();
        private Mat yellowMask = new Mat();

        private Mat redMaskResult = new Mat();
        private Mat blueMaskResult = new Mat();
        private Mat yellowMaskResult = new Mat();

        public Mat processFrame(Mat input) {

            Imgproc.cvtColor(input, ycrcbMat, Imgproc.COLOR_RGB2YCrCb);

            Core.inRange(ycrcbMat, redMin, redMax, redMask); // red filter mask
            Core.inRange(ycrcbMat, blueMin, blueMax, blueMask); // blue filter mask
            Core.inRange(ycrcbMat, yellowMin, yellowMax, yellowMask); // yellow filter mask

            redMaskResult.release();
            Core.bitwise_and(input, input, redMaskResult, redMask);
            int redAvg = (int) Core.mean(redMaskResult).val[0];

            blueMaskResult.release();
            Core.bitwise_and(input, input, blueMaskResult, blueMask);
            int blueAvg = (int) Core.mean(blueMaskResult).val[0];

            yellowMaskResult.release();
            Core.bitwise_and(input, input, yellowMaskResult, yellowMask);
            int yellowAvg = (int) Core.mean(yellowMaskResult).val[0];

            int redOrBlue = Math.max(redAvg, blueAvg);
            int highAvg = Math.max(redOrBlue, yellowAvg);

            maskedInputMat.release();
            if (highAvg == redAvg) {
                parkZone = ParkZone.RIGHT;
                redMaskResult.copyTo(maskedInputMat);
            } else if (highAvg == blueAvg) {
                parkZone = ParkZone.LEFT;
                blueMaskResult.copyTo(maskedInputMat);
            } else if (highAvg == yellowAvg) {
                parkZone = ParkZone.CENTER;
                yellowMaskResult.copyTo(maskedInputMat);
            }

            return maskedInputMat;
        }

        public ParkZone getParkZoneLoc() {
            return parkZone;
        }
    }
}