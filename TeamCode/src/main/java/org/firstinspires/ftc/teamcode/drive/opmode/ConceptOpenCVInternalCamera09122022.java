package org.firstinspires.ftc.teamcode.drive.opmode;

import android.icu.util.Output;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.vuforia.CameraDevice;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
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
@TeleOp(name = "Concept: EasyOpenCV Internal Camera w/color detector", group = "Concept")
public class ConceptOpenCVInternalCamera09122022 extends LinearOpMode {
    static final int STREAM_WIDTH = 320;
    static final int STREAM_HEIGHT = 240;

    OpenCvCamera phoneCam;
    SamplePipeline pipeline;

    @Override
    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        pipeline = new SamplePipeline();
        phoneCam.setPipeline(pipeline);
        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()

        {
            @Override
            public void onOpened()
            {
                phoneCam.startStreaming(STREAM_WIDTH,STREAM_HEIGHT, OpenCvCameraRotation.SIDEWAYS_RIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });
        waitForStart();

        while (opModeIsActive()) {
            telemetry.update();
            sleep(50);
        }
    }

    public class SamplePipeline extends OpenCvPipeline {
        private final Scalar BLUE = new Scalar(0, 0, 255);

        static final int detectRectWidth = 50;
        static final int detectRectHeight = 50;

        private static final int THRESHOLD = 107;

        Mat HSVHSubMat;
        Mat HSVSSubMat;
        Mat HSVVSubMat;

        Mat BGRBSubMat;
        Mat BGRGSubMat;
        Mat BGRRSubMat;

        Mat LABLSubMat;
        Mat LABASubMat;
        Mat LABBSubMat;

        Mat ySubMat;
        Mat rSubMat;
        Mat bSubMat;

        Mat YCrCb = new Mat();
        Mat outPut = new Mat();
        Mat HSV = new Mat();
        Mat BGR = new Mat();
        Mat LAB = new Mat();

        double finYAvg;
        double finRAvg;
        double finBAvg;

        /*
        private void inputToCb(Mat input) {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, 2);
        }

        private void inputToY(Mat input) {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Y, 0);
        }

        private void inputToR(Mat input) {
            Core.extractChannel(input, R, 0);
        }

        private void inputToG(Mat input) {
            Core.extractChannel(input, G, 1);
        }

        @Override
        public void init(Mat input) {
            inputToCb(input);
            inputToY(input);
            inputToR(input);
            inputToG(input);

            //region1_Cb = Cb.submat(new Rect(topLeft, bottomRight));
            //region1_Y = Y.submat(new Rect(topLeft, bottomRight));

        }
        */
        public Mat processFrame(Mat input) {
            Point topLeft = new Point(
                    input.cols()/2 - detectRectWidth/2,
                    input.rows()/2 - detectRectHeight/2);
            Point bottomRight = new Point(
                    input.cols()/2 + detectRectWidth/2,
                    input.rows()/2 + detectRectHeight/2);

            Imgproc.cvtColor(input,YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Imgproc.cvtColor(input,HSV, Imgproc.COLOR_RGB2HSV);
            Imgproc.cvtColor(input,BGR, Imgproc.COLOR_RGB2BGR);
            Imgproc.cvtColor(input,LAB, Imgproc.COLOR_RGB2Lab);

            Rect detectRect = new Rect(topLeft, bottomRight);

            HSVHSubMat = HSV.submat(detectRect);
            BGRBSubMat = BGR.submat(detectRect);
            LABASubMat = LAB.submat(detectRect);

            input.copyTo(outPut);
            Imgproc.rectangle(outPut,detectRect, BLUE, 4);

            ySubMat = YCrCb.submat(detectRect);
            rSubMat = YCrCb.submat(detectRect);
            bSubMat = YCrCb.submat(detectRect);

            Core.extractChannel(ySubMat, ySubMat, 0);
            Core.extractChannel(rSubMat, rSubMat, 1);
            Core.extractChannel(bSubMat, bSubMat, 2);

            Scalar yAvg = Core.mean(ySubMat);
            Scalar rAvg = Core.mean(rSubMat);
            Scalar bAvg = Core.mean(bSubMat);

            finYAvg = yAvg.val[0];
            finRAvg = rAvg.val[0];
            finBAvg = bAvg.val[0];

            telemetry.addData("HSV", HSVHSubMat);
            telemetry.addData("YCB", ySubMat);
            telemetry.addData("LAB", LABASubMat);
            //telemetry.addData("avg Y val:", finYAvg);
            //telemetry.addData("avg R val:", finRAvg);
            //telemetry.addData("avg B val:", finBAvg);
            telemetry.update();


            return (outPut);
        }

        /*
        public int getAverage() {
            return average;
        }


        public TYPE getType() {
            return type;
        }

        public enum TYPE {
            BALL, CUBE
        }
         */
    }
}