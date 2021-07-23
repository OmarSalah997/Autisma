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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.autisma.Eye_Gaze.Eyegaze;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.OpenCVLoader;

import wseemann.media.FFmpegMediaMetadataRetriever;

import static com.example.autisma.RecorderService.Videopath;

public class ToddlerResult extends AppCompatActivity {
    ProgressBar loading;
    Button ToMCHAT;
    TextView details;
    com.github.lzyzsd.circleprogress.DonutProgress Scorebar;
    TextView scoretxt;
    Button done;
    private ArrayList<Bitmap> frames= new ArrayList<>();
    VideoToFrames converter=new VideoToFrames(1); //mode =1 : eyegaze   mode = 2 : emotion
    int Result;
    static {
        OpenCVLoader.initDebug();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toddler_result);
        System.loadLibrary("opencv_java3");
        loading=findViewById(R.id.indeterminateBar);
        ToMCHAT=findViewById(R.id.proceed_to_MCHAT);
        details=findViewById(R.id.Gscore_details);
        Scorebar=findViewById(R.id.Gscore_progress);
        scoretxt=findViewById(R.id.toddscore);
        done=findViewById(R.id.done);
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
            converter.convert(Videopath,getBaseContext());
            return "Executed";
        }
        @Override protected void onPostExecute(String result) {
            Log.e("before eye gaze","on post execute");
            loading.setVisibility(View.INVISIBLE);
            scoretxt.setVisibility(View.VISIBLE);
            details.setVisibility(View.VISIBLE);
            Scorebar.setVisibility(View.VISIBLE);
            done.setVisibility(View.VISIBLE);
            frames=converter.frames;
            Log.e("before eye gaze 2","on post execute");

            List<List<FaceContour> >contours=new ArrayList<List<FaceContour>>();
            contours=converter.contours;
         int gazeResult=0;
         int gaze_left=0;
         int gaze_right=0;
            for(int i=0;i<contours.size();i++) {
                Eyegaze e = new Eyegaze();
                e.contours = contours.get(i);
                e.frame =frames.get(i);
                if(e.eyegaze()=='L')
                gaze_left+=1;
                if(e.eyegaze()=='R')
                    gaze_right +=1;
            }
Log.e("gaze left", String.valueOf(gaze_left));
            Log.e("gaze right", String.valueOf(gaze_right));
            if(gaze_left>gaze_right)
                Result+=6;//looked right less than 60%, failed the test :(
            if(Result<=6)
            {
                float f=((float) Result/(float)30)*100;
                Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
                Scorebar.setUnfinishedStrokeColor(Color.GRAY);
                Scorebar.setFinishedStrokeColor(Color.GREEN);
                details.setText(R.string.ToddlerLow);
            }
            if(Result>11 & Result<=18)
            {
                float f=((float) Result/(float)30)*100;
                Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
                Scorebar.setUnfinishedStrokeColor(Color.GRAY);
                Scorebar.setFinishedStrokeColor(Color.YELLOW);
                details.setText(R.string.ToddlerMedium);
            }
            if(Result>=19)
            {
                Log.e("after eye gaze","on post execute");

                float f=((float) Result/(float)30)*100;
                Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
                Scorebar.setUnfinishedStrokeColor(Color.GRAY);
                Scorebar.setFinishedStrokeColor(Color.RED);
                details.setText(R.string.ToddlerHigh);
                ToMCHAT.setVisibility(View.VISIBLE);
            }

        }
    }

}