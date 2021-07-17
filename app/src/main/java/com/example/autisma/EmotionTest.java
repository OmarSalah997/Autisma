package com.example.autisma;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    Button next;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        final int score = intent.getIntExtra("5Qscore",0);
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
                    img.setVisibility(View.VISIBLE);
                   // OpencameraPreview();
                    Picasso.get()
                            .load(R.drawable.happy3)
                            .resize(760, 720)
                            .into(img);
                    try {
                       captureEmotion("happy1");
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
                        captureEmotion("sad1");
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
                        captureEmotion("happy2");
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
                        captureEmotion("angry1");
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
                        captureEmotion("happy3");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                Intent myIntent = new Intent(getApplicationContext(), pressDotActivity.class);
                startActivity(myIntent);
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

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        cameraPreview.setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
    public void captureEmotion(final String path) throws Exception {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setUpMediaRecorder(EmotionTest.this,getBaseContext(),path);
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
    }
    private void setUpMediaRecorder(Activity activity,Context context,String path) throws IOException {
        if (null == activity) {
            return;
        }
        mMediaRecorder = new MediaRecorder();
        //.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        /**
         * create video output file
         */
        File imagesFolder = new File(getExternalFilesDir(null),"autizma");/*(getApplicationContext().getFilesDir(),);*/
        if (!imagesFolder.exists())
            imagesFolder.mkdirs();
         mCurrentFile= new File(imagesFolder, path + ".mp4");
        /**
         * set output file in media recorder
         */
        mMediaRecorder.setOutputFile(mCurrentFile.getPath());
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        mMediaRecorder.setVideoFrameRate(profile.videoFrameRate);
        mMediaRecorder.setVideoSize(720, 480);
        mMediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setOrientationHint(270);
        mMediaRecorder.prepare();
    }

    public void startRecordingVideo() {
        if (!cameraPreview.isAvailable()) {
            return;
        }
        try {
            SurfaceTexture texture = cameraPreview.getSurfaceTexture();
            texture.setDefaultBufferSize(cameraPreview.getWidth(),cameraPreview.getHeight());
            myCaptureRequestBuilder = myCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaces = new ArrayList<>();
            Surface previewSurface = new Surface(texture);
            surfaces.add(previewSurface);
            myCaptureRequestBuilder.addTarget(previewSurface);
            //MediaRecorder setup for surface
            Surface recorderSurface = mMediaRecorder.getSurface();
            surfaces.add(recorderSurface);

            myCaptureRequestBuilder.addTarget(recorderSurface);
            // Start a capture session
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
    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
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
        //OpencameraPreview();

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
}