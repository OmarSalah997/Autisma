package com.example.autisma;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Path;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceLandmark;
import com.google.mlkit.vision.face.FaceContour;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;

import static com.example.autisma.RecorderService.Videopath;

public class VideoToFrames {
    private int mode;
    public final ArrayList<Bitmap> frames= new ArrayList<>();
    public ArrayList<Bitmap> croppedframes=new ArrayList<>();
    public List<List<FaceLandmark> >landmarks;
    public List<List<FaceContour> >contours=new ArrayList<>();
    public VideoToFrames(int mode) {
        this.mode=mode;
    }
    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }
    public boolean convert(Uri videopath, Context context)
    {
        FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
        if(videopath!=null) {
            try {
                retriever.setDataSource((videopath).toString());
            }catch (RuntimeException e)
            {
                e.printStackTrace();
            }
        }
        if(mode==1)//eye gaze
        {
            for (int i = 0; i< 170; i++)
            {
                Bitmap bmp2 =retriever.getFrameAtTime(i*200000,FFmpegMediaMetadataRetriever.OPTION_NEXT_SYNC);
                if(bmp2!=null)
                    frames.add(bmp2);
                //saveToInternalStorage(bmp2,String.valueOf(i),context);
            }
                retriever.release();
                Videopath=null;
                File imagesFolder = new File(context.getExternalFilesDir(null),"autizma");/*(getApplicationContext().getFilesDir(),);*/
                File video = new File(imagesFolder, "V1" + ".mp4");
                video.delete();
                try {
                    faceDetection D= new faceDetection(context,frames,mode); //mode =1 : eyegaze   mode = 2 : emotion
                    D.detect();
                    while (!D.DetectionComplete)
                    {
                        int dummy;
                    }
                    croppedframes=D.Croppedframes;
                    landmarks=D.allLandmarks;
                    contours=D.allContours;
                    Log.e("in video to frames", String.valueOf(contours.size()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        if(mode==2)//emotion test
        {
            for (int i = 0; i<15; i++)
            {
                Bitmap bmp2 =retriever.getFrameAtTime(i*200000,FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
                if(bmp2!=null)
                {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(270);
                    Bitmap b3 = Bitmap.createBitmap(bmp2, 0, 0, bmp2.getWidth(), bmp2.getHeight(), matrix, false);
                    Bitmap b4 = Bitmap.createScaledBitmap(b3, 720, 480,false);
                    frames.add(b4);
                    //saveToInternalStorage(bmp2,String.valueOf(i),context);

                }
            }
            retriever.release();
            File imagesFolder = new File(context.getExternalFilesDir(null),"autizma");
            String fileName=getFileName(videopath,context);
            File video = new File(imagesFolder, fileName );
            video.delete();
            try {
                faceDetection D= new faceDetection(context,frames,mode); //mode =1 : eyegaze   mode = 2 : emotion
                D.detect();
                while (!D.DetectionComplete);
                croppedframes=D.Croppedframes;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    private String saveToInternalStorage(Bitmap bitmapImage,String name,Context context){
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
    public String getFileName(Uri uri,Context context) {
        if (uri != null) {
            String[] splits = uri.getPath().split(File.separator);
            return splits[splits.length - 1]; }
        return null;
}}
