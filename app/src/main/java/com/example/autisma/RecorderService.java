package com.example.autisma;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.params.OutputConfiguration;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.graphics.PixelFormat;

import android.media.CamcorderProfile;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.util.Size;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;


public class RecorderService extends Service  {
    static final int REQUEST_VIDEO_CAPTURE = 1;
  //  private WindowManager windowManager;
    private SurfaceView surfaceview;
    private SurfaceHolder mHolder;
    private Camera camera = null;
    private MediaRecorder mediaRecorder = null;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;
    File video ;
    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {/*
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Recording Video")
                .setContentText("")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        startForeground(1234, notification);
*/
            CharSequence name = "Recording video";
            String description = "";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1234", name, importance);
            channel.setDescription(description);

            // Don't see these lines in your code...
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        surfaceview = RecordingActivity.mSurfaceView;
        mHolder = RecordingActivity.mSurfaceHolder;

    }


    boolean isFrontFacing = true;

    private Camera openFrontFacingCameraGingerbread() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }
        Camera cam = null;
        if (isFrontFacing && checkFrontCamera(RecorderService.this)) {
            int cameraCount = 0;
            cam = null;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            cameraCount = Camera.getNumberOfCameras();
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    try {
                        cam = Camera.open(camIdx);
                    } catch (RuntimeException e) {
                        Log.e("Camera",
                                "Camera failed to open: " + e.getLocalizedMessage());

                    }
                }
            }
        } else {
            cam = Camera.open();
        }
        return cam;
    }

    private Camera.Size pictureSize;

    private Camera.Size getBiggesttPictureSize(Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedVideoSizes()) {
            if (result == null) {
                result = size;
            } else {
                int resultArea = result.width * result.height;
                int newArea = size.width * size.height;

                if (newArea > resultArea) {
                    result = size;
                }
            }
        }

        return (result);
    }

    // Method called right after Surface created (initializing and starting MediaRecorder)

    public void startRec() {

        camera = openFrontFacingCameraGingerbread();
        camera.setDisplayOrientation(90);
        mediaRecorder = new MediaRecorder();
        camera.unlock();


        mediaRecorder.setPreviewDisplay(mHolder.getSurface());
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setProfile(CamcorderProfile.get
                (CamcorderProfile.QUALITY_LOW));


        File imagesFolder = new File(
                Environment.getExternalStorageDirectory(), "Videos");
        if (!imagesFolder.exists())
            imagesFolder.mkdirs(); // <----

        video = new File(imagesFolder, System.currentTimeMillis()
                + ".mp4");  //file name + extension is .mp4


        mediaRecorder.setOutputFile(video.getAbsolutePath());

        Log.e("Media", video.getAbsolutePath());
        try {
            mediaRecorder.prepare();
        } catch (Exception e) {
            Log.e("MEDIA RECORDER",
                    "MEDIA failed to prepare: " + e.getStackTrace());
        }
        try {
            mediaRecorder.start();

        } catch (Exception e) {
            Log.e("MEDIA RECORDER",
                    "MEDIA failed to start: ");
            //  e.printStackTrace();
        }


    }

    // Stop recording and remove SurfaceView
    @Override
    public void onDestroy() {

       mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();

        camera.lock();
        camera.release();

        //windowManager.removeView(surfaceView);
        Log.e("MEDIA RECORDER",
                "on destroy called  ");
        uploadVideo();
    }

    private boolean checkFrontCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FRONT)) {
            // this device has front camera
            return true;
        } else {
            // no front camera on this device
            return false;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();

//you can pass using intent,that which camera you want to use front/rear
        isFrontFacing = extras.getBoolean("Front_Request");
startRec();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void uploadVideo() {
        filePath= Uri.fromFile(video);

        if (video.exists()) {
            filePath= Uri.fromFile(video);
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            // progressDialog.show();

            StorageReference ref = storageReference.child("videos/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            //
                              Toast.makeText(RecorderService.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RecorderService.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }
}


