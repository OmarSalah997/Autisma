package com.example.autisma.Eye_Gaze;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import com.google.mlkit.vision.face.FaceContour;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.opencv.android.Utils.bitmapToMat;

public class Eyegaze {
    public List<FaceContour> contours = new ArrayList<FaceContour>();
    public Bitmap frame;
    public Mat frameGrayscale = new Mat();

    public char eyegaze() {


        frameGrayscale = convert_to_grayscale();
        int right_eye_diff = 0;
        int left_eye_diff = 0;
FaceContour right_eye = null;
FaceContour left_eye=null;
for (int i = 0; i < contours.size(); i++) {
    if (contours.get(i).getFaceContourType() == 7)
        right_eye = contours.get(i);
    else if (contours.get(i).getFaceContourType() == 6)
        left_eye = contours.get(i);
}
if(right_eye !=null)
        right_eye_diff = get_gaze_ratio(right_eye);
if(left_eye!=null)
        left_eye_diff = get_gaze_ratio(left_eye);
                /*
            } else if (contours.get(i).getFaceContourType() == 6) {
                //left eye
                left_eye_diff = get_gaze_ratio(contours.get(i));
            }
        }*/

        if (left_eye_diff > right_eye_diff) {

        //    Log.e("looking right", "");
            return 'R';
        } else if(left_eye_diff <right_eye_diff) {
         //   Log.e("looking left", "");
            return 'L';
        }
        else
            return 'E';
        /*
        if(left_eye_diff >right_eye_diff){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "looking right", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "looking left", Toast.LENGTH_SHORT).show();
                }
            });
        }
*/
    }

    private Mat convert_to_grayscale() {
        Mat frameMatrix = new Mat();
        Mat bmpGrayscale = new Mat();
        Bitmap bmp32 = frame.copy(Bitmap.Config.ARGB_8888, true);
        bitmapToMat(bmp32, frameMatrix);
        Imgproc.cvtColor(frameMatrix, bmpGrayscale, Imgproc.COLOR_BGR2GRAY);
        return bmpGrayscale;
    }

    private int get_gaze_ratio(FaceContour eye) {
        // Gaze detection
        //getting the area from the frame of the left eye only

        Mat imgToProcess = frameGrayscale;
        List<MatOfPoint> eye_region = new ArrayList<MatOfPoint>();
        MatOfPoint m = new MatOfPoint();
        eye_region.add(
                new MatOfPoint(
                        new Point(eye.getPoints().get(0).x, eye.getPoints().get(0).y),
                        new Point(eye.getPoints().get(1).x, eye.getPoints().get(1).y),
                        new Point(eye.getPoints().get(2).x, eye.getPoints().get(2).y),
                        new Point(eye.getPoints().get(3).x, eye.getPoints().get(3).y),
                        new Point(eye.getPoints().get(4).x, eye.getPoints().get(4).y),
                        new Point(eye.getPoints().get(5).x, eye.getPoints().get(5).y),
                        new Point(eye.getPoints().get(6).x, eye.getPoints().get(6).y),
                        new Point(eye.getPoints().get(7).x, eye.getPoints().get(7).y),
                        new Point(eye.getPoints().get(8).x, eye.getPoints().get(8).y),
                        new Point(eye.getPoints().get(9).x, eye.getPoints().get(9).y),
                        new Point(eye.getPoints().get(10).x, eye.getPoints().get(10).y),
                     new Point(eye.getPoints().get(11).x, eye.getPoints().get(11).y),
                        new Point(eye.getPoints().get(12).x, eye.getPoints().get(12).y),
                        new Point(eye.getPoints().get(13).x, eye.getPoints().get(13).y),
                        new Point(eye.getPoints().get(14).x, eye.getPoints().get(14).y),
                        new Point(eye.getPoints().get(15).x, eye.getPoints().get(15).y)
                )
        );
        Mat mask = new Mat(imgToProcess.rows(), imgToProcess.cols(), CvType.CV_8U, Scalar.all(0));
        Imgproc.polylines(mask, eye_region, true, Scalar.all(255), 2);
        Imgproc.fillPoly(mask, eye_region, Scalar.all(255));
        Mat eyeMatrix = new Mat();
        Core.bitwise_and(imgToProcess, imgToProcess, eyeMatrix, mask = mask);
        List<Integer> xs = new ArrayList<Integer>();
        List<Integer> ys = new ArrayList<Integer>();
        for (int i = 0; i < 16; i++) {
            xs.add((int) eye.getPoints().get(i).x);
            ys.add((int) eye.getPoints().get(i).y);
        }

        int min_x = Collections.min(xs);
        int max_x = Collections.max(xs);
        int min_y = Collections.min(ys);
        int max_y = Collections.max(ys);
try {
    Rect eye_Rec = new Rect(min_x, min_y, max_x - min_x, max_y - min_y);
    Mat cropped_Eye = eyeMatrix.submat(eye_Rec).clone();
    Mat thresh_eye = new Mat();
    Imgproc.threshold(cropped_Eye, thresh_eye, 70, 255, Imgproc.THRESH_BINARY);
    Mat left_side = thresh_eye.submat(0, thresh_eye.height(), 0, thresh_eye.width() / 2).clone();
    Mat right_side = thresh_eye.submat(0, thresh_eye.height(), thresh_eye.width() / 2 + 1, thresh_eye.width()).clone();

    int right = Core.countNonZero(right_side);
    int left = Core.countNonZero(left_side);
    /*Toast.makeText(getApplicationContext(), //Context
            "left is"+left +"right is"+right, // Message to display
            Toast.LENGTH_SHORT // Duration of the message, another possible value is Toast.LENGTH_LONG
    ).show();*/
    if (right > left) {
        Log.e("right", "right and number of right white pixels minus left " + (right - left));
        return (right - left);
    } else if(left>right){
        Log.e("left", "left and number of left white pixels minus right " + (left - right));
        return (left - right);
    }
}catch (Exception e)
{
    Log.e("excpetion",e.getMessage().toString());
}
return 0; }
}