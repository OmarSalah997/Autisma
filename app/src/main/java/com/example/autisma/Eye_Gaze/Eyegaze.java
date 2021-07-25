package com.example.autisma.Eye_Gaze;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Environment;
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
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.opencv.android.Utils.bitmapToMat;
import static org.opencv.android.Utils.loadResource;

public class Eyegaze {
    public List<FaceContour> contours = new ArrayList<FaceContour>();
    public Bitmap frame;
    public Mat frameGrayscale = new Mat();

    public char eyegaze() {


        frameGrayscale = convert_to_grayscale();
        int right_eye_diff = 0;
        int left_eye_diff = 0;
        FaceContour right_eye = null;
        FaceContour left_eye = null;
        for (int i = 0; i < contours.size(); i++) {
            if (contours.get(i).getFaceContourType() == 7)
                right_eye = contours.get(i);
            else if (contours.get(i).getFaceContourType() == 6)
                left_eye = contours.get(i);
        }

        char n1=get_gaze_ratio(left_eye,'L');
        char n2=get_gaze_ratio(right_eye,'R');
        if (n2 == 'R'){
            return 'L';}
        if (n1 == 'L'){
                 return 'R' ;}


        else return 'E';


    }

    private Mat convert_to_grayscale() {
      Mat frameMatrix = new Mat();
       Mat bmpGrayscale = new Mat();

        bitmapToMat(frame, frameMatrix);
        Imgproc.cvtColor(frameMatrix, bmpGrayscale, Imgproc.COLOR_BGR2GRAY);
        return bmpGrayscale;
    }

    private char get_gaze_ratio(FaceContour eye,char c) {
        // Gaze detection

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

    Rect eye_Rec = new Rect(min_x, min_y, (max_x - min_x)+2, (max_y - min_y)+2);

    Mat cropped_Eye = imgToProcess.submat(eye_Rec).clone();
    Mat thresh_eye = new Mat();
    thresh_eye=cropped_Eye.clone();
    Imgproc.threshold(cropped_Eye, thresh_eye, 100, 255, Imgproc.THRESH_BINARY);

    Mat left_side = thresh_eye.submat(0, thresh_eye.height()-1, 0, (int)(thresh_eye.width() / 2)).clone();
    Mat right_side = thresh_eye.submat(0, thresh_eye.height()-1, (int)(thresh_eye.width() / 2 )+ 1, thresh_eye.width()-1).clone();

    int right =right_side.height()*right_side.width()- Core.countNonZero(right_side);
    int left = left_side.width()*left_side.height()-Core.countNonZero(left_side);

    if (right > left) {
Log.e("right>left", String.valueOf(c));
        return 'R';
    } else if(left>right){
        Log.e("left>right", String.valueOf(c));
        return 'L';
    }

return 0; }
}