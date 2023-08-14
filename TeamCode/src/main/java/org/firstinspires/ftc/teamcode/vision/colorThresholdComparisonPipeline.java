/*
 * Copyright (c) 2021 Sebastian Erives
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package org.firstinspires.ftc.teamcode.vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class colorThresholdComparisonPipeline extends OpenCvPipeline {

    public enum ParkZone{
        LEFT,
        CENTER,
        RIGHT
    }
    private volatile ParkZone parkZone = ParkZone.CENTER; //default

    public Scalar blueMin = new Scalar(0,0,143);
    public Scalar blueMax = new Scalar(147,123,255);

    public Scalar redMin = new Scalar(0,185,0);
    public Scalar redMax = new Scalar(165,255,132);

    public Scalar yellowMin = new Scalar(100,0,0);
    public Scalar yellowMax = new Scalar(255,161,80);

    /**
     * This will allow us to choose the color
     * space we want to use on the live field
     * tuner instead of hardcoding it
     */
    public ColorSpace colorSpace = ColorSpace.YCrCb;

    /*
     * A good practice when typing EOCV pipelines is
     * declaring the Mats you will use here at the top
     * of your pipeline, to reuse the same buffers every
     * time. This removes the need to call mat.release()
     * with every Mat you create on the processFrame method,
     * and therefore, reducing the possibility of getting a
     * memory leak and causing the app to crash due to an
     * "Out of Memory" error.
     */
    Mat bSubMat;
    Mat ySubMat;
    //Mat rSubMat;

    private Mat ycrcbMat       = new Mat();
    private Mat binaryMat      = new Mat();
    private Mat maskedInputMat = new Mat();


    private Mat redMask         = new Mat();
    private Mat blueMask        = new Mat();
    private Mat yellowMask      = new Mat();

    private Mat redMaskResult   = new Mat();
    private Mat blueMaskResult  = new Mat();
    private Mat yellowMaskResult = new Mat();



    private Telemetry telemetry = null;

    /**
     * Enum to choose which color space to choose
     * with the live variable tuner isntead of
     * hardcoding it.
     */

    enum ColorSpace {
        /*
         * Define our "conversion codes" in the enum
         * so that we don't have to do a switch
         * statement in the processFrame method.
         */
        RGB(Imgproc.COLOR_RGBA2RGB),
        HSV(Imgproc.COLOR_RGB2HSV),
        YCrCb(Imgproc.COLOR_RGB2YCrCb),
        Lab(Imgproc.COLOR_RGB2Lab);

        //store cvtCode in a public var
        public int cvtCode = 0;

        //constructor to be used by enum declarations above
        ColorSpace(int cvtCode) {
            this.cvtCode = cvtCode;
        }
    }


    public colorThresholdComparisonPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public Mat processFrame(Mat input) {

        maskedInputMat.release();
        Imgproc.cvtColor(input,ycrcbMat,Imgproc.COLOR_RGB2YCrCb);

        Core.inRange(ycrcbMat,redMin,redMax,redMask); // red filter mask
        Core.inRange(ycrcbMat,blueMin,blueMax,blueMask); // blue filter mask
        Core.inRange(ycrcbMat,yellowMin,yellowMax,yellowMask); // yellow filter mask

        redMaskResult.release();
        Core.bitwise_and(input,input,redMaskResult,redMask);
        int redAvg = (int) Core.mean(redMaskResult).val[0];

        blueMaskResult.release();
        Core.bitwise_and(input,input,blueMaskResult,blueMask);
        int blueAvg = (int) Core.mean(blueMaskResult).val[0];

        yellowMaskResult.release();
        Core.bitwise_and(input,input,yellowMaskResult,yellowMask);
        int yellowAvg = (int) Core.mean(yellowMaskResult).val[0];

        int redOrBlue = Math.max(redAvg, blueAvg);
        int highAvg = Math.max(redOrBlue, yellowAvg);

        if(highAvg == redAvg){
            parkZone = ParkZone.RIGHT;
            redMaskResult.copyTo(maskedInputMat);
        }
        else if(highAvg == blueAvg){
            parkZone = ParkZone.LEFT;
            blueMaskResult.copyTo(maskedInputMat);
        }
        else if(highAvg == yellowAvg){
            parkZone = ParkZone.CENTER;
            yellowMaskResult.copyTo(maskedInputMat);
        }

        telemetry.addData("park zone dest:", parkZone);
        telemetry.update();

        /*
         * The Mat returned from this method is the
         * one displayed on the viewport.
         *
         * To visualize our threshold, we'll return
         * the "masked input mat" which shows the
         * pixel from the input Mat that were inside
         * the threshold range.
         */
        return maskedInputMat;
    }

}