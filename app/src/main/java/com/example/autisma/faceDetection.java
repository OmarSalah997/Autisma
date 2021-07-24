package com.example.autisma;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;

import org.tensorflow.lite.support.image.TensorImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;

import static com.google.mlkit.vision.common.InputImage.IMAGE_FORMAT_NV21;

public class faceDetection {
    public  boolean DetectionComplete =false;
        InputImage Inimage;
        Context context;
        Bitmap image;
        int mode;
        ArrayList<Bitmap> frames;
        ArrayList<Bitmap> Croppedframes= new ArrayList<>();
    List<List<FaceLandmark>> allLandmarks=new ArrayList<List<FaceLandmark>>();
    List<List<FaceContour>> allContours=new ArrayList<List<FaceContour>>();
    public faceDetection(Context context,ArrayList<Bitmap> frames,int mode) throws IOException {
        this.context=context;
        this.frames =frames;
        this.mode=mode;// mode =1:  cropping  mode =2 : cropping and resizing
    }
    public void detect() throws IOException {
        FaceDetectorOptions LandMarksOn = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                .build();
        if(mode==2)//emotion, no need for contours
        {
            LandMarksOn = new FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    .build();
        }
        FaceDetector detector = FaceDetection.getClient(LandMarksOn);
        for(int i=0; i<frames.size();i++){
            image=frames.get(i);
            final int finalI = i;
            if(image!=null)
            {
                Inimage =InputImage.fromBitmap(image,0);
                Task<List<Face>> result = detector.process(Inimage).addOnSuccessListener(
                    new OnSuccessListener<List<Face>>() {
                        @Override
                        public void onSuccess(@NonNull List<Face> faces) {
                               if(image!=null)
                               {
                                        for (Face face : faces) {
                                            Rect bounds = face.getBoundingBox();
                                            if(bounds.left>0 & bounds.top>0 & bounds.right<image.getWidth())
                                {
                                    Bitmap cropped;
                                       int hcorrect;
                                       if (bounds.top+bounds.height()>image.getHeight())
                                           hcorrect=480-bounds.top;
                                       else
                                           hcorrect=bounds.height();
                                       cropped=Bitmap.createBitmap(image,bounds.left ,bounds.top,bounds.width(),hcorrect);
                                     if(mode==2)
                                    {
                                        if(cropped!=null){
                                        Bitmap resized;
                                        if(cropped.getHeight()>0 & cropped.getWidth()>0)
                                        {
                                            Bitmap grey=toGrayscale(cropped);
                                            resized=Bitmap.createScaledBitmap(grey, 48, 48,true);
                                            Croppedframes.add(resized);
                                        }
                                        }
                                    }
                                    else {
                                        if(cropped!=null) {
                                            Croppedframes.add(cropped);
                                         List<FaceLandmark> landmarks=face.getAllLandmarks();
                                         List<FaceContour> contours=face.getAllContours();
                                         Log.e("in face detection", String.valueOf(face.getAllContours().size()));
                                         allLandmarks.add(landmarks);
                                         allContours.add(contours);

                                        }
                                    }

                            }}}
                            if(finalI==frames.size()-1)
                                DetectionComplete=true;
                        }
                    }).addOnCompleteListener(new OnCompleteListener<List<Face>>() {
                @Override
                public void onComplete(@NonNull Task<List<Face>> task) {
                    { }
                }
            }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int fail;
                    }
                });

            }

        }
    }
    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

}
