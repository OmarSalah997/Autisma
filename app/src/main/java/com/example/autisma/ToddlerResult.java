package com.example.autisma;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import wseemann.media.FFmpegMediaMetadataRetriever;

import static com.example.autisma.RecorderService.Videopath;

public class ToddlerResult extends AppCompatActivity {
    ProgressBar loading;
    Button ToMCHAT;
    TextView details;
    com.github.lzyzsd.circleprogress.DonutProgress Scorebar;
    TextView scoretxt;
    Button done;
    //TextView wait;
    int Result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toddler_result);
        loading=findViewById(R.id.indeterminateBar);
        ToMCHAT=findViewById(R.id.proceed_to_MCHAT);
        details=findViewById(R.id.Gscore_details);
        Scorebar=findViewById(R.id.Gscore_progress);
        scoretxt=findViewById(R.id.toddscore);
        done=findViewById(R.id.done);
        //wait=findViewById(R.id.wait);
        Intent intent = getIntent();
        Result=intent.getIntExtra("ToddlerScore",0);
        ToMCHAT.setVisibility(View.INVISIBLE);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainHOME.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        ToMCHAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), M_chat_info.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        MyAsyncTask videoToFrame= new MyAsyncTask();
        videoToFrame.execute();// video is split into 340 frame in background
        /////////////////face detection and cropping
      /*  InputImage image;
        try {
            FaceDetectorOptions LandMarksOn = new FaceDetectorOptions.Builder().setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL).build();
            FaceDetector detector = FaceDetection.getClient(LandMarksOn);
            for(int i=0; i<1050;i++){
                image = InputImage.fromFilePath(getBaseContext(), Uri.parse(imagesFolder.toString()+String.valueOf(i)+".jpeg"));
                final InputImage finalImage = image;
                final int finalI = i;
                Task<List<Face>> result = detector.process(image).addOnSuccessListener(
                        new OnSuccessListener<List<Face>>() {
                            @Override
                            public void onSuccess(List<Face> faces) {
                                // Task completed successfully
                                for (Face face : faces) {
                                    Rect bounds = face.getBoundingBox();
                                    Paint paint = new Paint();
                                    paint.setStrokeWidth(6);
                                    paint.setColor(Color.RED);
                                    paint.setStyle(Paint.Style.STROKE);
                                    Bitmap tempBitmap = Bitmap.createBitmap(finalImage.getWidth(),finalImage.getHeight(), Bitmap.Config.RGB_565);
                                    Canvas canvas = new Canvas(tempBitmap);
                                    canvas.drawBitmap(finalImage.getBitmapInternal(),0,0,null);
                                    canvas.drawRoundRect(new RectF(bounds.left, bounds.top, bounds.right, bounds.bottom), 2, 2, paint);
                                    saveToInternalStorage(tempBitmap,String.valueOf(finalI)+"done");
                                }
                                for(int i=0; i<1050;i++){
                                    File imagesFolder = new File(getExternalFilesDir(null),"autizma");
                                    File pic = new File(imagesFolder, String.valueOf(i) + ".jpeg");
                                    pic.delete();
                                }

                            }
                        }).addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                // ...
                            }
                        });
            }
            }catch (IOException e) {
            e.printStackTrace();
        }*/

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), MainHOME.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    public class MyAsyncTask extends AsyncTask<Void, Void, String> {
        @Override protected String doInBackground(Void... params) {
            VideoToFrames converter=new VideoToFrames(1);
            converter.convert(Videopath,getBaseContext());
           /* try {
                faceDetection Detector= new faceDetection(getBaseContext());
                Detector.detect();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            return "Executed";
        }
        @Override protected void onPostExecute(String result) {
            loading.setVisibility(View.INVISIBLE);
            //wait.setVisibility(View.INVISIBLE);
            scoretxt.setVisibility(View.VISIBLE);
            details.setVisibility(View.VISIBLE);
            Scorebar.setVisibility(View.VISIBLE);
            done.setVisibility(View.VISIBLE);
            if(Result<=6)
            {
                float f=((float) Result/(float)24)*100;
                Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
                Scorebar.setUnfinishedStrokeColor(Color.GRAY);
                Scorebar.setFinishedStrokeColor(Color.GREEN);
                details.setText(R.string.ToddlerLow);
            }
            if(Result>13 & Result<=18)
            {
                float f=((float) Result/(float)24)*100;
                Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
                Scorebar.setUnfinishedStrokeColor(Color.GRAY);
                Scorebar.setFinishedStrokeColor(Color.YELLOW);
                details.setText(R.string.ToddlerMedium);
            }
            if(Result>=19)
            {
                float f=((float) Result/(float)24)*100;
                Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
                Scorebar.setUnfinishedStrokeColor(Color.GRAY);
                Scorebar.setFinishedStrokeColor(Color.RED);
                details.setText(R.string.ToddlerHigh);
                ToMCHAT.setVisibility(View.VISIBLE);
            }

        }
    }

}