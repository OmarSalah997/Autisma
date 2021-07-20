package com.example.autisma;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.autisma.ml.EmotionClassification;
import com.example.autisma.ml.ModelAdam0;
import com.squareup.picasso.Picasso;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
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
    ProgressBar loading;
    private static final int PERMISSION_REQUEST_CODE = 200;
    TextureView cameraPreview;
    TextView instructions;
    int clickCount=0;
    ImageView img;
    MediaRecorder mMediaRecorder;
    File mCurrentFile;
    Button ToEmotionResult;
    private String myCameraID;
    private CameraManager myCameraManager;
    private CameraDevice myCameraDevice;
    private CameraCaptureSession myCameraCaptureSession;
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
        loading=findViewById(R.id.indeterminateBar);
        loading.setVisibility(View.INVISIBLE);
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
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Matrix matrix = new Matrix();
                final int width = cameraPreview.getWidth();
                final int height = cameraPreview.getHeight();
                // Rotate the camera preview when the screen is landscape.
                matrix.setPolyToPoly(
                        new float[]{
                                0.f, 0.f, // top left
                                width, 0.f, // top right
                                0.f, height, // bottom left
                                width, height, // bottom right
                        }, 0,
                        orientation == 90 ?
                                // Clockwise
                                new float[]{
                                        0.f, height, // top left
                                        0.f, 0.f, // top right
                                        width, height, // bottom left
                                        width, 0.f, // bottom right
                                } : // mDisplayOrientation == 270
                                // Counter-clockwise
                                new float[]{
                                        width, 0.f, // top left
                                        width, height, // top right
                                        0.f, 0.f, // bottom left
                                        0.f, height, // bottom right
                                }, 0,
                        4);
                cameraPreview.setTransform(matrix);
        }
        if (!checkPermission()) {
            requestPermission();
        }
         ToEmotionResult =findViewById(R.id.emotion_done);
        ToEmotionResult.setOnClickListener(new View.OnClickListener() {
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
                    ToEmotionResult.setVisibility(View.INVISIBLE);
                    Picasso.get()
                            .load(R.drawable.happy3)
                            .resize(800, 760)
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
                    ToEmotionResult.setVisibility(View.INVISIBLE);
                    Picasso.get()
                            .load(R.drawable.sad4)
                            .resize(760, 720)
                            .into(img);
                    try {
                        //sad1[0] =captureEmotion("sad1");
                        MyAsyncTask VideoToFrames1=new MyAsyncTask();
                        VideoToFrames1.setCount(1);
                        VideoToFrames1.setvideopath(happy1[0]);
                        VideoToFrames1.execute();
                        //executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return;

                }
                if(clickCount==3)
                {
                    clickCount++;
                    ToEmotionResult.setVisibility(View.INVISIBLE);
                    Picasso.get()
                            .load(R.drawable.happy5)
                            .resize(760, 720)
                            .into(img);
                    try {
                      //   happy2[0] =captureEmotion("happy2");
                       // MyAsyncTask VideoToFrames2=new MyAsyncTask();
                        //VideoToFrames2.setCount(2);
                        //VideoToFrames2.setvideopath(sad1[0]);
                        //VideoToFrames2.execute();
                        //executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if(clickCount==4)
                {
                    clickCount++;
                    ToEmotionResult.setVisibility(View.INVISIBLE);
                    Picasso.get()
                            .load(R.drawable.angry2)
                            .resize(760, 720)
                            .into(img);
                    try {
                      //   angry1[0] =captureEmotion("angry1");
                      //  MyAsyncTask VideoToFrames3=new MyAsyncTask();
                      //  VideoToFrames3.setCount(3);
                      //  VideoToFrames3.setvideopath(happy2[0]);
                      //  VideoToFrames3.execute();
                        //executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return;
                }
                if(clickCount==5)
                {
                    clickCount++;
                    ToEmotionResult.setVisibility(View.INVISIBLE);
                    Picasso.get()
                            .load(R.drawable.happy7)
                            .resize(760, 720)
                            .into(img);
                    try {
                        // happy3[0] =captureEmotion("happy3");
                       // MyAsyncTask VideoToFrames4=new MyAsyncTask();
                       // VideoToFrames4.setCount(4);
                       // VideoToFrames4.setvideopath(angry1[0]);
                       // VideoToFrames4.execute();
                        //executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if(clickCount>5)
                {
                    loading.setVisibility(View.VISIBLE);
                    loading.invalidate();
                    ToEmotionResult.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.INVISIBLE);
                    cameraPreview.setVisibility(View.INVISIBLE);
                  //  MyAsyncTask VideoToFrames5=new MyAsyncTask();
                   // VideoToFrames5.setCount(5);
                   // VideoToFrames5.setvideopath(happy3[0]);
                   // VideoToFrames5.execute();
                   // VideoToFrames5.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    //Intent myIntent = new Intent(getApplicationContext(), pressDotActivity.class);
                    //startActivity(myIntent);
                    //we should run the model
                    //finish();
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
                            ToEmotionResult.setVisibility(View.VISIBLE);
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
        mMediaRecorder.setVideoSize(720, 480);
        mMediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
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
                    myCameraCaptureSession = cameraCaptureSession;
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
            myCameraCaptureSession.stopRepeating();
            myCameraCaptureSession.abortCaptures();
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
        warning.setIcon(R.drawable.wrong_sign);
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
    @SuppressLint("StaticFieldLeak")
    public class MyAsyncTask extends AsyncTask<Void, Void, String> {
        Uri videopath;
        int count;
        VideoToFrames converter=new VideoToFrames(2);
        public MyAsyncTask() {

        }
        public void setvideopath(Uri videopath)
        {
            this.videopath = videopath;
        }
        public  void setCount(int i)
        {
            count=i;
        }

        @Override protected String doInBackground(Void... params) {
            converter.convert(videopath,getBaseContext());
            if(count>=5)
                loading.setVisibility(View.VISIBLE);
            return "Executed";
        }
        @Override protected void onPostExecute(String result) {
            switch (count) {
               /* case 1:
                    happy1Frames = converter.croppedframes;
                    break;
                case 2:
                    sad1Frames=converter.croppedframes;
                    break;
                case 3:
                    happy2Frames = converter.croppedframes;
                    break;
                case 4:
                   angry1Frames = converter.croppedframes;
                    break;*/
                case 1:
                    happy1Frames = converter.croppedframes;
                    try {

                        EmotionClassification model = EmotionClassification.newInstance(getBaseContext());
                        TensorImage I=new TensorImage(DataType.UINT8);
                        ByteBuffer buff=ByteBuffer.allocate(9216);
                        TensorBuffer inputFeature0;
                        inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 48, 48, 1}, DataType.FLOAT32);
                        EmotionClassification.Outputs outputs;
                        TensorBuffer outputFeature0;
                        float[] happy1;
                        int max;
                        for(int i=0;i<happy1Frames.size();i++)
                        {
                            I.load(happy1Frames.get(i));//=  TensorImage();
                            buff.put(I.getBuffer());
                            inputFeature0.loadBuffer(buff);
                            outputs = model.process(inputFeature0);
                            outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                            happy1=outputFeature0.getFloatArray();
                            // 0 angry   1 happy   2 sad   3 neutral
                             max=max(happy1);

                        }
                        model.close();
                    } catch (IOException e) {
                        // TODO Handle the exception
                    }
                    loading.setVisibility(View.INVISIBLE);
                    ToEmotionResult.setVisibility(View.VISIBLE);

                    break;
            }
        }
    }
    public static int max(float[] t) {
        float maximum = t[0];// start with the first value
        int maxindex=-1;
        for (int i=1; i<t.length; i++) {
            if (t[i] > maximum) {
                maximum = t[i];
                maxindex=i;// new maximum
            }
        }
        return maxindex;
    }
}