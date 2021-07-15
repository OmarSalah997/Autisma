 package com.example.autisma;

 import android.annotation.SuppressLint;
 import android.app.NotificationChannel;
 import android.app.NotificationManager;
 import android.app.ProgressDialog;
 import android.app.Service;
 import android.content.Context;
 import android.content.Intent;
 import android.content.pm.PackageManager;
 import android.hardware.Camera;
 import android.media.CamcorderProfile;
 import android.media.MediaMetadataRetriever;
 import android.media.MediaRecorder;
 import android.net.Uri;
 import android.os.Build;
 import android.os.Bundle;
 import android.os.IBinder;
 import android.util.Log;
 import android.view.SurfaceHolder;
 import android.view.SurfaceView;
 import android.widget.Toast;

 import com.google.android.gms.tasks.OnFailureListener;
 import com.google.android.gms.tasks.OnSuccessListener;
 import com.google.firebase.storage.FirebaseStorage;
 import com.google.firebase.storage.OnProgressListener;
 import com.google.firebase.storage.StorageReference;
 import com.google.firebase.storage.UploadTask;

 import java.io.File;
 import java.util.UUID;

 import androidx.annotation.NonNull;
 import androidx.annotation.Nullable;

 import static com.example.autisma.LOGIN.IP;

public class RecorderService extends Service  {
    public static Uri Videopath=null;
    static final int REQUEST_VIDEO_CAPTURE = 1;
  //  private WindowManager windowManager;
    private SurfaceView surfaceview;
    private SurfaceHolder mHolder;
    private Camera camera = null;
    private MediaRecorder mediaRecorder = null;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;
    private File video ;
    String directory_touploadto;
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    @Override
    public void onCreate() {

       // Token = preferences.getString("TOKEN",null);
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

    @SuppressLint("SetWorldReadable")
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
                (CamcorderProfile.QUALITY_480P));




   /*     File imagesFolder = new File(getExternalFilesDir("VIDEOS_AUTISM"),System.currentTimeMillis()
                + ".mp4");
        if (!imagesFolder.exists())
            imagesFolder.mkdirs(); // <---- */
        File imagesFolder = new File(getExternalFilesDir(null),"autizma");/*(getApplicationContext().getFilesDir(),);*/
        if (!imagesFolder.exists())
            imagesFolder.mkdirs(); // <---- */

        video = new File(imagesFolder, "V1" + ".mp4");  //file name + extension is .mp4

          Videopath= Uri.fromFile(video);//.getAbsolutePath();
                  //Uri.fromFile(new File(videoResource));
        //=Uri.fromFile(video);
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
       //uploadVideo();
     //  video.delete();
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
        directory_touploadto=extras.getString("dir","geom/");
        if(directory_touploadto != "do not record")
startRec();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void uploadVideo() {
        filePath= Uri.fromFile(video);

        Communication com=new Communication(RecorderService.this);

        String url = IP+"vidtest"; // route


        retriever.setDataSource(RecorderService.this, Uri.fromFile(video));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillisec = Long.parseLong(time);

     //   if (video.exists()&& timeInMillisec>62000)
        if(video.exists())
        {
            filePath= Uri.fromFile(video);

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
          //   progressDialog.show();

            StorageReference ref = storageReference.child(directory_touploadto+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            video.delete();
                            //
                          //    Toast.makeText(RecorderService.this, "Uploaded", Toast.LENGTH_SHORT).show();
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
        //    video.delete();
        }
    }
}


