package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

/*
 * This version of the internal camera example uses EasyOpenCV's interface to the
 * original Android camera API
 */

@Disabled
@TeleOp(name = "Concept: EasyOpenCV Internal Camera w/o array", group = "Concept")
public class ConceptOpenCVInternalCameraNoArray extends LinearOpMode {
    static final int STREAM_WIDTH = 320;
    static final int STREAM_HEIGHT = 240;
    int avg = 0;
    OpenCvCamera phoneCam;
    SamplePipeline pipeline;
    int snapshotAnalysis = 0;
    @Override
    public void runOpMode() {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        phoneCam.setPipeline(new SamplePipeline());
        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                phoneCam.startStreaming(STREAM_WIDTH, STREAM_HEIGHT, OpenCvCameraRotation.SIDEWAYS_RIGHT);
            }

            @Override
            public void onError(int errorCode) {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });
        while (!isStarted() & !isStopRequested())
        {
            telemetry.addData("Realtime analysis", pipeline.getAnalysis());
            telemetry.update();

            // Don't burn CPU cycles busy-looping in this sample
            sleep(50);
        }

        /*
         * The START command just came in: snapshot the current analysis now
         * for later use. We must do this because the analysis will continue
         * to change as the camera view changes once the robot starts moving!
         */
        snapshotAnalysis = pipeline.getAnalysis();

        /*
         * Show that snapshot on the telemetry
         */
        telemetry.addData("Snapshot post-START analysis", snapshotAnalysis);
        telemetry.update();

    }

    class SamplePipeline extends OpenCvPipeline {

        Mat YCrCb = new Mat();
        Mat Y = new Mat();


        void inputToY(Mat input) {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            ArrayList<Mat> yCrCbChannels = new ArrayList<Mat>(3);
            Core.split(YCrCb, yCrCbChannels);
            Y = yCrCbChannels.get(0);

        }

        @Override
        public void init(Mat firstFrame) {
            inputToY(firstFrame);
        }

        @Override
        public Mat processFrame(Mat input) {
            inputToY(input);
            System.out.println("processing requested");
            avg = (int) Core.mean(Y).val[0];
            YCrCb.release(); // don't leak memory!
            Y.release(); // don't leak memory!
            return input;
        }

        public int getAnalysis() {
            return avg;
        }
    }
}