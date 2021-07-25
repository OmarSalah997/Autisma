package com.example.autisma;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
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
    private int mode; //mode = 1 : toddler test   mode=2 : emotion test
    public final ArrayList<Bitmap> frames= new ArrayList<>();
    public ArrayList<Bitmap> croppedframes=new ArrayList<>();
    public List<List<FaceLandmark> >landmarks;
    public List<List<FaceContour> >contours=new ArrayList<>();
    public VideoToFrames(int mode) {
        this.mode=mode;
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
        if(mode==1)//toddler video, 35 seconds
        {
            for (int i = 0; i< 170; i++)
            {
                Bitmap bmp2 =retriever.getFrameAtTime(i*200000,FFmpegMediaMetadataRetriever.OPTION_NEXT_SYNC);
                if(bmp2!=null)
                    frames.add(bmp2);
            }
                retriever.release();
                Videopath=null;
                File imagesFolder = new File(context.getExternalFilesDir(null),"autizma");/*(getApplicationContext().getFilesDir(),);*/
                File video = new File(imagesFolder, "V1" + ".mp4");
                video.delete();
                try {
                    faceDetection D= new faceDetection(context,frames,mode); //mode =1 : eyegaze   mode = 2 : emotion
                    D.detect();
                    while (!D.DetectionComplete);
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
                    Bitmap resized=Bitmap.createScaledBitmap(b3, 48, 48,true);
                    Bitmap b4 = toGrayscale(resized);
                    frames.add(b4);

                }
            }
            retriever.release();
            File imagesFolder = new File(context.getExternalFilesDir(null),"autizma");
            String fileName=getFileName(videopath,context);
            File video = new File(imagesFolder, fileName );
            video.delete();
            croppedframes=frames;
        }
        return true;
    }
    public String getFileName(Uri uri,Context context) {
        if (uri != null) {
            String[] splits = uri.getPath().split(File.separator);
            return splits[splits.length - 1]; }
        return null;
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
    }}
