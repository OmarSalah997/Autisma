package com.example.autisma;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import static com.example.autisma.RecorderService.Videopath;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import wseemann.media.FFmpegMediaMetadataRetriever;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecordingActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private StorageReference mStorageRef;
    private static final String TAG = "RecorderActivity";
    private Boolean started_watching = false;
    private Button captureButton;
    private Button nextbutton;
    private Button uploadImage;
    public static SurfaceView mSurfaceView;
    private int position = 1;
    int video_number=0;
    int autistic_child=0;
    public static SurfaceHolder mSurfaceHolder;
    public static Camera mCamera;
    public static boolean mPreviewRunning;
    int score5Q =0;
    Uri outputFileUri = null;
    @SuppressLint("StaticFieldLeak")
    public static VideoView videoview;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        private final RecorderService recService=new RecorderService();
    //  MediaController mediaController ;
    Intent mIntent;
    Intent previousIntent;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        score5Q = intent.getIntExtra("5Qscore",0);
try{
    setContentView(R.layout.recording_activity);
    captureButton = (Button) findViewById(R.id.start_watch);
    uploadImage = (Button) findViewById(R.id.uploadimage);
    mStorageRef = FirebaseStorage.getInstance().getReference();
    // mediaController = new MediaController(this);
    videoview = (VideoView) findViewById(R.id.video_view);
    previousIntent=getIntent();
    autistic_child=previousIntent.getIntExtra("autistic_child",0);
    mIntent = new Intent(RecordingActivity.this, RecorderService.class);
    mSurfaceView= (SurfaceView)findViewById(R.id.surfaceView);
    mSurfaceHolder=mSurfaceView.getHolder();
    mSurfaceHolder.addCallback(this);
    } catch (Exception e) {
            e.getMessage();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (!checkPermission()) {
                requestPermission();
            }
        }

        // videoview.setMediaController(mediaController);
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result3=ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO, CAMERA, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (locationAccepted && cameraAccepted)
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    else {

                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(RECORD_AUDIO)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE, CAMERA,READ_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(RecordingActivity.this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.done), okListener)
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
                .show();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void startWatch(View v) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoview.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoview.setLayoutParams(params);
        captureButton.setVisibility(View.INVISIBLE);
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //  if (!videoview.isPlaying()) {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.geom);

        videoview.setVideoURI(uri);
        if(autistic_child ==0)
            mIntent.putExtra("dir","geom/");
        else
            mIntent.putExtra("dir","geom_a/");
        videoview.start();
        mIntent.putExtra("Front_Request",true);
        startService(mIntent);

     //   final MediaController mediaController = new MediaController(this);
     //   videoview.setMediaController(mediaController);
        videoview.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (videoview.isPlaying()) {
                    videoview.pause();
                    stopService(mIntent);

                } else {

                    videoview.start();
                    mIntent.putExtra("Front_Request",true);

                    startService(mIntent);
                }
                return false;
            }

        });

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //here upload video
                videoview.seekTo(1);
                //recService.uploadVideo();
                stopService(mIntent);
                //Here we should calc the percentage and add to score5Q
              /*  FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
                MediaMetadataRetriever ret=new MediaMetadataRetriever();
                if(Videopath!=null) {

                    //send video to the python script
                        ret.setDataSource(getBaseContext(),Videopath);
                        try {
                            retriever.setDataSource((Videopath).toString());
                        }catch (RuntimeException e)
                        {

                        }


                }

                for (int i = 0; i< 1050; i++)
                {
                    //Bitmap bmp = ret.getFrameAtTime(i*33333,MediaMetadataRetriever.OPTION_CLOSEST);
                    FFmpegMediaMetadataRetriever.Metadata met=  retriever.getMetadata();
                    Bitmap bmp2 =retriever.getFrameAtTime(i*1000000,FFmpegMediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                   // saveToInternalStorage(bmp,String.valueOf(i));
                   saveToInternalStorage(bmp2,String.valueOf(i));

                }
                ret.release();
                File imagesFolder = new File(getExternalFilesDir(null),"autizma");/*(getApplicationContext().getFilesDir(),);
                File video = new File(imagesFolder, "V1" + ".mp4");
                video.delete();*/

                Intent toResult= new Intent(RecordingActivity.this, ToddlerResult.class);
                toResult.putExtra("ToddlerScore", score5Q);
                RecordingActivity.this.startActivity(toResult);

            }
        });
        //here
        if (position != 1) {
            videoview.seekTo(position);
            videoview.start();
        } else {
            videoview.seekTo(1);
        }

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        if (videoview!= null)
        {
            savedInstanceState.putInt("position", videoview.getCurrentPosition());
        }
        videoview.pause();
    }
   /* public void next_video(View v){
        stopService(mIntent);
        video_number ++ ;
        Uri uri = null;
        if(video_number == 1){
         uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bubbles);
         if(autistic_child ==0)
            mIntent.putExtra("dir","bubbles/");
         else
             mIntent.putExtra("dir","bubbles_a/");
            captureButton.setVisibility(View.INVISIBLE);
        }
        else if(video_number == 2){
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bunny);
            if(autistic_child ==0)
            mIntent.putExtra("dir","bunny/");
            else
                mIntent.putExtra("dir","bunny_a/");
        }else if(video_number ==3){
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.wheels);
            if(autistic_child ==0)
            mIntent.putExtra("dir","wheels/");
            else
                mIntent.putExtra("dir","wheels_a/");

        }else if(video_number ==4){
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.balls);
            if(autistic_child ==0)
            mIntent.putExtra("dir","toys/");
            else
                mIntent.putExtra("dir","toys_a/");

        }
        else if(video_number ==5){
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alarm);
            if(autistic_child ==0)
                mIntent.putExtra("dir","alarm/");
            else
                mIntent.putExtra("dir","alarm_a/");

        }else if(video_number ==6){
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fireworks);
            if(autistic_child ==0)
                mIntent.putExtra("dir","fireworks/");
            else
                mIntent.putExtra("dir","fireworks_a/");
        }else if(video_number ==7){

            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ezayk);
            if(autistic_child ==0)
                mIntent.putExtra("dir","say_name/");
            else
                mIntent.putExtra("dir","say_name_a/");
nextbutton.setText("Next");
        }
        else if(video_number ==8){

            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.emotions);

                mIntent.putExtra("dir","do not record");


        }
        else if(video_number ==9){

            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.surprised_child);
            if(autistic_child ==0)
            mIntent.putExtra("dir","surprised_child/");
            else
                mIntent.putExtra("dir","surprised_child_a/");

        }
        else if(video_number ==10){
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sad_child);
            if(autistic_child ==0)
            mIntent.putExtra("dir","sad_child/");
            else
                mIntent.putExtra("dir","sad_child_a/");


        }
        else if(video_number ==11){
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.smiling_child);
            if(autistic_child ==0)
                mIntent.putExtra("dir","smiling_child/");
            else
                mIntent.putExtra("dir","smiling_child_a/");

        }
        else if(video_number ==12){

            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.angry_child);
            if(autistic_child ==0)
                mIntent.putExtra("dir","angry_child/");
            else
                mIntent.putExtra("dir","angry_child_a/");

        }
        else if(video_number ==13){

            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dot);

                mIntent.putExtra("dir","do not record");

            nextbutton.setText("Play");
        }
        else if(video_number ==14){

            Intent myIntent = new Intent(getApplicationContext(), pressDotActivity.class);
            startActivity(myIntent);
        }
        videoview.setVideoURI(uri);
        videoview.start();
        mIntent.putExtra("Front_Request",true);
        startService(mIntent);

        //   final MediaController mediaController = new MediaController(this);
        //   videoview.setMediaController(mediaController);
        videoview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (videoview.isPlaying()) {
                    videoview.pause();
                    stopService(mIntent);

                } else {

                    videoview.start();
                    mIntent.putExtra("Front_Request",true);

                    startService(mIntent);
                }
                return false;
            }

        });

    }
*/
    @Override
    public void surfaceCreated(SurfaceHolder holder) { }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
      //  stopService(mIntent);
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
       stopService(mIntent);
    }


}












