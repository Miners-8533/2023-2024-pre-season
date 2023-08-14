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

public class YCrCbThresholdPipeline extends OpenCvPipeline {
    private final Scalar BLUE = new Scalar(0, 0, 255);

    static final int detectRectWidth = 50;
    static final int detectRectHeight = 50;

    /*
     * These are our variables that will be
     * modifiable from the variable tuner.
     *
     * Scalars in OpenCV are generally used to
     * represent color. So our values in the
     * lower and upper Scalars here represent
     * the Y, Cr and Cb values respectively.
     *
     * YCbCr, like most color spaces, range
     * from 0-255, so we default to those
     * min and max values here for now, meaning
     * that all pixels will be shown.
     */
    public Scalar lower = new Scalar(0, 0, 0);
    public Scalar upper = new Scalar(255, 255, 255);

    public Scalar blueMin = new Scalar(0,0,143);
    public Scalar blueMax = new Scalar(147,123,255);

    public Scalar redMin = new Scalar(0,185,0);
    public Scalar redMax = new Scalar(165,255,132);

    public Scalar yellowMin = new Scalar(100,0,0);
    public Scalar yellowMax = new Scalar(255,161,80);
    public Scalar rAvg = new Scalar(0,0,0);
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

    private Mat ybinaryMat      = new Mat();
    private Mat ymaskedInputMat = new Mat();

    private Mat rSubMat         = new Mat();
    private Mat rbinaryMat      = new Mat();
    private Mat rmaskedInputMat = new Mat();
    private Mat rgb2YcRb        = new Mat();
    private Mat rgb2Hsv         = new Mat();
    private Mat rgb2Lab         = new Mat();


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

    public YCrCbThresholdPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public Mat processFrame(Mat input) {
        Point topLeft = new Point(
                input.cols()/2 - detectRectWidth/2,
                input.rows()/2 - detectRectHeight/2);
        Point bottomRight = new Point(
                input.cols()/2 + detectRectWidth/2,
                input.rows()/2 + detectRectHeight/2);
        Rect detectRect = new Rect(topLeft, bottomRight);

        /*
         * Converts our input mat from RGB to
         * specified color space by the enum.
         * EOCV ALWAYS returns RGB mats, so you'd
         * always convert from RGB to the color
         * space you want to use.
         *
         * Takes our "input" mat as an input, and outputs
         * to a separate Mat buffer "ycrcbMat"
         */

        Imgproc.cvtColor(input, rgb2Hsv, Imgproc.COLOR_RGB2YCrCb);
        //Imgproc.cvtColor(input, ycrcbMat, Imgproc.COLOR_RGB2YCrCb);
        //Imgproc.cvtColor(input, rgb2Lab, Imgproc.COLOR_RGB2Lab );

        //ySubMat = ycrcbMat.submat(0,240,0,320);
        //bSubMat = ycrcbMat.submat(0,240,0,320);
        //rSubMat = ycrcbMat.submat(0,240,0,320);


        //Core.extractChannel(bSubMat,bSubMat,2);
        /*
         * This is where our thresholding actually happens.
         * Takes our "ycrcbMat" as input and outputs a "binary"
         * Mat to "binaryMat" of the same size as our input.
         * "Discards" all the pixels outside the bounds specified
         * by the scalars above (and modifiable with EOCV-Sim's
         * live variable tuner.)
         *
         * Binary meaning that we have either a 0 or 255 value
         * for every pixel.
         *
         * 0 represents our pixels that were outside the bounds
         * 255 represents our pixels that are inside the bounds
         */

        //Core.inRange(ycrcbMat, lower, upper, binaryMat);
        //Core.inRange(bSubMat,blueMin,blueMax,binaryMat);

        //maskedInputMat.release();
        //Core.bitwise_and(input,input,maskedInputMat,binaryMat);
        //Scalar bAvg = Core.mean(binaryMat);

        //rmaskedInputMat.release();
        //Core.inRange(rgb2Hsv,redMin,redMax,rbinaryMat);
        //Core.bitwise_and(input,input,maskedInputMat,binaryMat);
        //Scalar rAvg = Core.mean(maskedInputMat);

        //maskedInputMat.release();
        //rbinaryMat.release();
        //Core.inRange(rgb2Lab,redMin,redMax,rbinaryMat);
        //Core.bitwise_and(input,input,maskedInputMat,binaryMat);
        //Scalar labAvg = Core.mean(maskedInputMat);

        //maskedInputMat.release();
        //rbinaryMat.release();
        //Core.inRange(rgb2YcRb,redMin,redMax,rbinaryMat);
        //Core.bitwise_and(input,input,maskedInputMat,binaryMat);
        //Scalar ycrbrAvg = Core.mean(maskedInputMat);

        //Core.inRange(ySubMat,yellowMin,yellowMax,ybinaryMat);
        //Core.bitwise_and(input,input,ymaskedInputMat,ybinaryMat);
        //Scalar yAvg = Core.mean(ymaskedInputMat);
        maskedInputMat.release();
        Core.inRange(rgb2Hsv, redMin,redMax,binaryMat);
        Core.bitwise_and(input,input,maskedInputMat,binaryMat);
        Scalar ycrcbAvg = Core.mean(maskedInputMat);
        /*
         * Release the reusable Mat so that old data doesn't
         * affect the next step in the current processing
         */


        /*
         * Now, with our binary Mat, we perform a "bitwise and"
         * to our input image, meaning that we will perform a mask
         * which will include the pixels from our input Mat which
         * are "255" in our binary Mat (meaning that they're inside
         * the range) and will discard any other pixel outside the
         * range (RGB 0, 0, 0. All discarded pixels will be black)
         */
        //Core.bitwise_and(input, input, maskedInputMat, binaryMat);


        /**
         * Add some nice and informative telemetry messages
         */
        //telemetry.addData("colorspace convert code", colorSpace.cvtCode);
        //telemetry.addData("mean blue values", bAvg);
        //telemetry.addData("mean red values", rAvg);
        //telemetry.addData("mean yellow values", yAvg);
        //telemetry.addData("Lab mean avg", labAvg;
        telemetry.addData("mean avg:", ycrcbAvg);
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