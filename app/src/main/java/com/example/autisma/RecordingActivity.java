package com.example.autisma;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.hardware.Camera;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.CamcorderProfile;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.SeekBar;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.UUID;

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
    public static SurfaceHolder mSurfaceHolder;
    public static Camera mCamera;
    public static boolean mPreviewRunning;
    Uri outputFileUri = null;
    public static VideoView videoview;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        private RecorderService recService=new RecorderService();
    //  MediaController mediaController ;
    Intent mIntent;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
try{
            setContentView(R.layout.recording_activity);
          captureButton = (Button) findViewById(R.id.start_watch);
          nextbutton=(Button)findViewById(R.id.next_video);
            uploadImage = (Button) findViewById(R.id.uploadimage);
            mStorageRef = FirebaseStorage.getInstance().getReference();
            // mediaController = new MediaController(this);
            videoview = (VideoView) findViewById(R.id.video_view);
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

    public void startWatch(View v) {
        captureButton.setVisibility(View.INVISIBLE);
        //  if (!videoview.isPlaying()) {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.geom);

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
                    mIntent.putExtra("dir","geom");
                    startService(mIntent);
                }
                return false;
            }

        });

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                //here upload video
                videoview.seekTo(1);

   //recService.uploadVideo();
                stopService(mIntent);
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
    public void next_video(View v){
        stopService(mIntent);
        video_number ++ ;
        Uri uri = null;
        if(video_number == 1){
         uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bubbles);
            mIntent.putExtra("dir","bubbles/");
            captureButton.setVisibility(View.INVISIBLE);
        }
        else if(video_number == 2){
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bunny);
            mIntent.putExtra("dir","bunny/");
        }else if(video_number ==3){
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.wheels);
            mIntent.putExtra("dir","wheels/");

        }else if(video_number ==4){
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.balls);
            mIntent.putExtra("dir","toys/");
            nextbutton.setVisibility(View.INVISIBLE);
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
      //  stopService(mIntent);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
       stopService(mIntent);
    }

}












