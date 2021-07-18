package com.example.autisma;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EmotionTest extends AppCompatActivity implements TextureView.SurfaceTextureListener
{
    private static final int PERMISSION_REQUEST_CODE = 200;
    TextureView cameraPreview;
    TextView instructions;
    int clickCount=0;
    ImageView img;
    MediaRecorder mMediaRecorder;
    File mCurrentFile;
    private String myCameraID;
    private CameraManager myCameraManager;
    private CameraDevice myCameraDevice;
    private CameraCaptureSession myCameraCaptureSession;
    private CameraCaptureSession myCameraCaptureSession2;
    private CaptureRequest.Builder myCaptureRequestBuilder;
    private CaptureRequest.Builder myCaptureRequestBuilder2;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private ArrayList<Bitmap> happy1Frames=new ArrayList<>();
    private ArrayList<Bitmap> happy2Frames=new ArrayList<>();
    private ArrayList<Bitmap> happy3Frames=new ArrayList<>();
    private ArrayList<Bitmap> sad1Frames=new ArrayList<>();
    private ArrayList<Bitmap> angry1Frames=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        final int score = intent.getIntExtra("5Qscore",0);
        final Uri[] happy1 = new Uri[1];
        final Uri[] happy2 = new Uri[1];
        final Uri[] happy3 = new Uri[1];
        final Uri[] sad1 = new Uri[1];
        final Uri[] angry1 = new Uri[1];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_test);
        instructions=findViewById(R.id.instructions);
        instructions.setText(getString(R.string.Emotest1));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},
                    50); }
        img=findViewById(R.id.EmotionImg);
        img.setVisibility(View.INVISIBLE);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        cameraPreview = findViewById(R.id.campreview);
        myCameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        openCamera();
        if (!checkPermission()) {
            requestPermission();
        }
        Button ToDotTest=findViewById(R.id.emotion_done);
        ToDotTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickCount==0)
                {
                    clickCount++;
                    instructions.setText(getString(R.string.Emotest2));
                    return;
                }
                if(clickCount==1)
                {
                    clickCount++;
                    instructions.setVisibility(View.GONE);
                    cameraPreview.setVisibility(View.VISIBLE);
                    img.setVisibility(View.VISIBLE);
                   // OpencameraPreview();
                    Picasso.get()
                            .load(R.drawable.happy3)
                            .resize(760, 720)
                            .into(img);
                    try {
                        happy1[0] =captureEmotion("happy1");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if(clickCount==2)
                {
                    clickCount++;
                    Picasso.get()
                            .load(R.drawable.sad4)
                            .resize(760, 720)
                            .into(img);
                    try {
                         sad1[0] =captureEmotion("sad1");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return;

                }
                if(clickCount==3)
                {
                    clickCount++;
                    Picasso.get()
                            .load(R.drawable.happy5)
                            .resize(760, 720)
                            .into(img);
                    try {
                         happy2[0] =captureEmotion("happy2");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return;
                }
                if(clickCount==4)
                {
                    clickCount++;
                    Picasso.get()
                            .load(R.drawable.angry2)
                            .resize(760, 720)
                            .into(img);
                    try {
                         angry1[0] =captureEmotion("angry1");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return;
                }
                if(clickCount==5)
                {
                    clickCount++;
                    Picasso.get()
                            .load(R.drawable.happy7)
                            .resize(760, 720)
                            .into(img);
                    try {
                         happy3[0] =captureEmotion("happy3");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if(clickCount>5)
                {
                    Intent myIntent = new Intent(getApplicationContext(), pressDotActivity.class);
                    startActivity(myIntent);
                    VideoToFrames v1=new VideoToFrames(2);
                    v1.convert(happy1[0],getBaseContext());
                    happy1Frames=v1.croppedframes;
                    v1=new VideoToFrames(2);
                    v1.convert(sad1[0],getBaseContext());
                    sad1Frames=v1.croppedframes;
                    v1=new VideoToFrames(2);
                    v1.convert(happy2[0],getBaseContext());
                    happy2Frames=v1.croppedframes;
                    v1=new VideoToFrames(2);
                    v1.convert(angry1[0],getBaseContext());
                    v1=new VideoToFrames(2);
                    angry1Frames=v1.croppedframes;
                    v1.convert(happy3[0],getBaseContext());
                    happy3Frames=v1.croppedframes;
                    finish();
                }
            }
        });
    }

    private CameraDevice.StateCallback myStateCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            myCameraDevice = camera;
        }
        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            myCameraDevice.close();
        }
        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            myCameraDevice.close();
            myCameraDevice = null;
        }
    };

    private void openCamera() {
        try {
            myCameraID = myCameraManager.getCameraIdList()[1];
            ActivityCompat.requestPermissions(EmotionTest.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            myCameraManager.openCamera(myCameraID, myStateCallBack, null);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

/*
    public void OpencameraPreview(){
        SurfaceTexture mySurfaceTexture = cameraPreview.getSurfaceTexture();
        Surface mySurface = new Surface(mySurfaceTexture);
        try {
            myCaptureRequestBuilder = myCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            myCaptureRequestBuilder.addTarget(mySurface);
            myCameraDevice.createCaptureSession(Arrays.asList(mySurface), new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            myCameraCaptureSession = session;
                            myCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                            try {
                                myCameraCaptureSession.setRepeatingRequest(myCaptureRequestBuilder.build(), null, null);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
}
*/
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        cameraPreview.setSurfaceTextureListener(this); }
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
    }
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {}
    public Uri captureEmotion(final String path) throws Exception {
        final Uri[] vpath = new Uri[1];
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                          vpath[0] =  setUpMediaRecorder(EmotionTest.this,getBaseContext(),path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    startRecordingVideo();
        (new Handler()).postDelayed(
                new Runnable() {
                    public void run() {
                        try {
                            stopRecordingVideo();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                ,
                4000);
        return vpath[0];
    }
    private Uri setUpMediaRecorder(Activity activity,Context context,String path) throws IOException {
        if (null == activity) {
            return null;
        }
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        File imagesFolder = new File(getExternalFilesDir(null),"autizma");/*(getApplicationContext().getFilesDir(),);*/
        if (!imagesFolder.exists())
            imagesFolder.mkdirs();
        mCurrentFile= new File(imagesFolder, path + ".mp4");
        Uri vpath=Uri.parse(mCurrentFile.getPath());
        mMediaRecorder.setOutputFile(mCurrentFile.getPath());
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        mMediaRecorder.setVideoFrameRate(profile.videoFrameRate);
        mMediaRecorder.setVideoSize(640, 480);
        mMediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setOrientationHint(270);
        mMediaRecorder.prepare();
        return vpath;
    }

    public void startRecordingVideo() {
        if (!cameraPreview.isAvailable()) {return; }
        try {
            SurfaceTexture texture = cameraPreview.getSurfaceTexture();
            texture.setDefaultBufferSize(cameraPreview.getWidth(),cameraPreview.getHeight());
            myCaptureRequestBuilder = myCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaces = new ArrayList<>();
            Surface previewSurface = new Surface(texture);
            surfaces.add(previewSurface);
            myCaptureRequestBuilder.addTarget(previewSurface);
            Surface recorderSurface = mMediaRecorder.getSurface();
            surfaces.add(recorderSurface);
            myCaptureRequestBuilder.addTarget(recorderSurface);
            try{
            myCameraDevice.createCaptureSession(surfaces,
                    new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    try{
                        cameraCaptureSession.setRepeatingRequest(
                                myCaptureRequestBuilder.build(),null,null
                        );
                    }catch (CameraAccessException e){
                        e.printStackTrace();
                    }
                    myCameraCaptureSession2 = cameraCaptureSession;
                    EmotionTest.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Start recording
                            mMediaRecorder.start();
                        }
                    });
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                }
            }, null);}
         catch (CameraAccessException e) {
                e=e;
        } catch (IllegalStateException e) {
                e=e;
        }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    /*
    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/
    public void stopRecordingVideo() throws Exception {
        try {
            myCameraCaptureSession2.stopRepeating();
            myCameraCaptureSession2.abortCaptures();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        // Stop recording
        mMediaRecorder.stop();
        mMediaRecorder.reset();
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO, CAMERA, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }
    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result3=ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result1==PackageManager.PERMISSION_GRANTED && result2==PackageManager.PERMISSION_GRANTED && result3==PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final AlertDialog.Builder warning = new AlertDialog.Builder(this);
        warning.setIcon(R.drawable.ic_baseline_g_translate_24);
        warning.setTitle(R.string.warning);
        warning.setMessage(R.string.resLost);
        warning.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(getBaseContext(), child_tests.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        warning.setNegativeButton(getString(R.string.cancel),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        warning.show();
    }
}