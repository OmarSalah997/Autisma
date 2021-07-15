package com.example.autisma;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import wseemann.media.FFmpegMediaMetadataRetriever;

import static com.example.autisma.RecorderService.Videopath;

public class VideoToFrames {
    private int mode;
    private final ArrayList<Bitmap> frames= new ArrayList<>();
    public VideoToFrames(int mode) {
        this.mode=mode;
    }
    public boolean convert(Uri videopath, Context context)
    {
        FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
        if(videopath!=null) {
            try {
                retriever.setDataSource((videopath).toString());
            }catch (RuntimeException ignored)
            {
            }
        }
        for (int i = 0; i< 340; i++)
        {
            FFmpegMediaMetadataRetriever.Metadata met=  retriever.getMetadata();
            Bitmap bmp2 =retriever.getFrameAtTime(i*100000,FFmpegMediaMetadataRetriever.OPTION_NEXT_SYNC);
            frames.add(bmp2);
            //saveToInternalStorage(bmp2,String.valueOf(i),context);
        }
        retriever.release();
        Videopath=null;
        File imagesFolder = new File(context.getExternalFilesDir(null),"autizma");/*(getApplicationContext().getFilesDir(),);*/
        File video = new File(imagesFolder, "V1" + ".mp4");
        video.delete();
        try {
            faceDetection D= new faceDetection(context,frames,mode);
            D.detect();
            while (!D.DetectionComplete)
            {
                int dummy;
            }
        } catch (IOException e) {
            e.printStackTrace();
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
}
