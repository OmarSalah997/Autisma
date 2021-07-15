package com.example.autisma;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;

import static com.google.mlkit.vision.common.InputImage.IMAGE_FORMAT_NV21;

public class faceDetection {
    public  boolean DetectionComplete =false;
        InputImage Inimage;
        Context context;
        Uri imgPath;
        Bitmap image;
        int mode;
        ArrayList<Bitmap> frames;
    public faceDetection(Context context,ArrayList<Bitmap> frames,int mode) throws IOException {
        this.context=context;
        imgPath=Uri.parse(context.getExternalFilesDir(null)+"/autizma/");
        this.frames =frames;
        this.mode=mode;// mode =1:  cropping  mode =2 : cropping and resizing
    }
    public void detect() throws IOException {
        FaceDetectorOptions LandMarksOn = new FaceDetectorOptions.Builder()
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .build();
        FaceDetector detector = FaceDetection.getClient(LandMarksOn);
        for(int i=0; i<frames.size();i++){
            //final Bitmap src= BitmapFactory.decodeFile(imgPath.toString()+ i +".jpg");
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //src.compress(Bitmap.CompressFormat.PNG, 100, baos);

            image=frames.get(i);
            if(image!=null)
            {Inimage =InputImage.fromBitmap(image,0);
            //image =InputImage.fromFilePath(context, Uri.fromFile(new File(imgPath.toString()+ i +".jpg")));
            final InputImage finalImage = Inimage;
            final int finalI = i;
            Task<List<Face>> result = detector.process(Inimage).addOnSuccessListener(
                    new OnSuccessListener<List<Face>>() {
                        @Override
                        public void onSuccess(@NonNull List<Face> faces) {
                            // Task completed successfully
                            for (Face face : faces) {
                                Rect bounds = face.getBoundingBox();
                                if(bounds.left>0 & bounds.right<image.getWidth())
                                {
                                       int hcorrect;
                                       if (bounds.top+bounds.height()>image.getHeight())
                                           hcorrect=480-bounds.top;
                                       else
                                           hcorrect=bounds.height();
                                       Bitmap cropped=Bitmap.createBitmap(image,bounds.left ,bounds.top,bounds.width(),hcorrect);
                                       // Paint paint = new Paint();
                                       // paint.setStrokeWidth(6);
                                       // paint.setColor(Color.RED);
                                       // paint.setStyle(Paint.Style.STROKE);
                                        //Bitmap tempBitmap = Bitmap.createBitmap(finalImage.getWidth(),finalImage.getHeight(), Bitmap.Config.RGB_565);
                                       // Canvas canvas = new Canvas(tempBitmap);
                                       //canvas.drawBitmap(Objects.requireNonNull(finalImage.getBitmapInternal()),0,0,null);
                                        //canvas.drawRoundRect(new RectF(bounds.left, bounds.top, bounds.right, bounds.bottom), 2, 2, paint);
                                        saveToInternalStorage(cropped, "Face"+ finalI);
                                        if(finalI==339)
                                            DetectionComplete=true;
                            }}
                        }
                    }).addOnCompleteListener(new OnCompleteListener<List<Face>>() {
                @Override
                public void onComplete(@NonNull Task<List<Face>> task) {
                    {
                       // File imagesFolder = new File(context.getExternalFilesDir(null),"autizma");
                       // File pic = new File(imagesFolder, finalI + ".jpg");
                        //pic.delete();
                    }
                }
            });}
        }
    }
    private String saveToInternalStorage(Bitmap bitmapImage,String name){
        // path to /data/data/yourapp/app_data/imageDir
        File directory = new File(context.getExternalFilesDir(null),"autizma");
        // Create imageDir
        File mypath=new File(directory,name + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

}
