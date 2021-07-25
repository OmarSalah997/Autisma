package com.example.autisma;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.autisma.ml.Model;
import com.squareup.picasso.Picasso;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private ArrayList<Bitmap> happy1Frames=new ArrayList<>();
    private ArrayList<Bitmap> happy2Frames=new ArrayList<>();
    private ArrayList<Bitmap> happy3Frames=new ArrayList<>();
    private ArrayList<Bitmap> sad1Frames=new ArrayList<>();
    private ArrayList<Bitmap> angry1Frames=new ArrayList<>();
    int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        score = intent.getIntExtra("5Qscore",0);
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
                PackageManager.PERMISSION_GRANTED | ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
        if (!checkPermission()) {
            requestPermission();
        }
         ToEmotionResult =findViewById(R.id.emotion_done);
        ToEmotionResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickCount==0)
                { openCamera();
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
                        sad1[0] =captureEmotion("sad1");
                        MyAsyncTask VideoToFrames1=new MyAsyncTask();
                        VideoToFrames1.setCount(1);
                        VideoToFrames1.setvideopath(happy1[0]);
                        VideoToFrames1.execute();


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
                         happy2[0] =captureEmotion("happy2");
                        MyAsyncTask VideoToFrames2=new MyAsyncTask();
                        VideoToFrames2.setCount(2);
                        VideoToFrames2.setvideopath(sad1[0]);
                        VideoToFrames2.execute();

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
                         angry1[0] =captureEmotion("angry1");
                        MyAsyncTask VideoToFrames3=new MyAsyncTask();
                        VideoToFrames3.setCount(3);
                        VideoToFrames3.setvideopath(happy2[0]);
                        VideoToFrames3.execute();
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
                         happy3[0] =captureEmotion("happy3");
                        MyAsyncTask VideoToFrames4=new MyAsyncTask();
                        VideoToFrames4.setCount(4);
                        VideoToFrames4.setvideopath(angry1[0]);
                        VideoToFrames4.execute();
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
                    MyAsyncTask VideoToFrames5=new MyAsyncTask();
                    VideoToFrames5.setCount(5);
                    VideoToFrames5.setvideopath(happy3[0]);
                    //VideoToFrames5.execute();
                    VideoToFrames5.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                    //VideoToFrames5.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    //Intent myIntent = new Intent(getApplicationContext(), pressDotActivity.class);
                    //startActivity(myIntent);
                    //we should run the model
                    //finish();
                    //loading.setVisibility(View.INVISIBLE);
                    //Intent toScore= new Intent(EmotionTest.this,OtherResult.class);
                    //toScore.putExtra("5Qscore", score);
                    //EmotionTest.this.startActivity(toScore);
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

    public void stopRecordingVideo() throws Exception {
        try {
            myCameraCaptureSession.stopRepeating();
            myCameraCaptureSession.abortCaptures();
            myCameraCaptureSession.close();
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
            Model model = null;
            try {
                model = Model.newInstance(getBaseContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
            TensorImage image;
            Model.Outputs outputs;
            List<Category> probability;
            Category H,A,S,N;
            int partial_score=0;
            float max;
            String label;
            switch (count) {
                case 1:
                    happy1Frames = converter.croppedframes;
                    for(int i=0;i<happy1Frames.size();i++)
                    {
                        image = TensorImage.fromBitmap(happy1Frames.get(i));
                        outputs = model.process(image);
                        probability= outputs.getProbabilityAsCategoryList();
                         max = probability.get(0).getScore();
                         label=probability.get(0).getLabel();
                        if (probability.get(1).getScore() > max)
                            label=probability.get(1).getLabel();
                        if (probability.get(2).getScore() > max)
                            label=probability.get(2).getLabel();
                        if (probability.get(3).getScore() > max)
                            label=probability.get(3).getLabel();

                        if(!label.equals("happy"))
                        {
                            partial_score++;
                        }
                        max=0;
                        label="0";
                    }
                    model.close();
                    if(partial_score>happy1Frames.size()/2)
                    score++;
                    Log.e("1 done", ":(((((((((((");
                    break;
                case 2:
                    sad1Frames=converter.croppedframes;
                    for(int i=0;i<sad1Frames.size();i++)
                    {
                        image = TensorImage.fromBitmap(sad1Frames.get(i));
                        outputs = model.process(image);
                        probability= outputs.getProbabilityAsCategoryList();
                        max = probability.get(0).getScore();
                        label=probability.get(0).getLabel();
                        if (probability.get(1).getScore() > max)
                            label=probability.get(1).getLabel();
                        if (probability.get(2).getScore() > max)
                            label=probability.get(2).getLabel();
                        if (probability.get(3).getScore() > max)
                            label=probability.get(3).getLabel();

                        if(!label.equals("sad"))
                        {
                            partial_score++;
                        }
                        max=0;
                        label="0";
                    }
                    model.close();
                    if(partial_score>sad1Frames.size()/2)
                        score++;
                    Log.e("2 done", ":(((((((((((");

                    break;
                case 3:
                    happy2Frames = converter.croppedframes;

                    for(int i=0;i<happy2Frames.size();i++)
                    {
                        image = TensorImage.fromBitmap(happy2Frames.get(i));
                        outputs = model.process(image);
                        probability= outputs.getProbabilityAsCategoryList();
                        max = probability.get(0).getScore();
                        label=probability.get(0).getLabel();
                        if (probability.get(1).getScore() > max)
                            label=probability.get(1).getLabel();
                        if (probability.get(2).getScore() > max)
                            label=probability.get(2).getLabel();
                        if (probability.get(3).getScore() > max)
                            label=probability.get(3).getLabel();

                        if(!label.equals("happy"))
                        {
                            partial_score++;
                        }
                        max=0;
                        label="0";
                    }
                    model.close();
                    if(partial_score>happy2Frames.size()/2)
                        score++;
                    Log.e("3 done", ":(((((((((((");
                    break;
                case 4:
                   angry1Frames = converter.croppedframes;

                    for(int i=0;i<angry1Frames.size();i++)
                    {
                        image = TensorImage.fromBitmap(angry1Frames.get(i));
                        outputs = model.process(image);
                        probability= outputs.getProbabilityAsCategoryList();
                        max = probability.get(0).getScore();
                        label=probability.get(0).getLabel();
                        if (probability.get(1).getScore() > max)
                            label=probability.get(1).getLabel();
                        if (probability.get(2).getScore() > max)
                            label=probability.get(2).getLabel();
                        if (probability.get(3).getScore() > max)
                            label=probability.get(3).getLabel();

                        if(!label.equals("angry"))
                        {
                            partial_score++;
                        }
                        max=0;
                        label="0";
                    }
                    model.close();
                    if(partial_score>angry1Frames.size()/2)
                        score++;
                    Log.e("4 done", ":(((((((((((");
                    break;
                case 5:
                    happy3Frames = converter.croppedframes;


                    for(int i=0;i<happy3Frames.size();i++)
                    {
                        image = TensorImage.fromBitmap(happy3Frames.get(i));
                        outputs = model.process(image);
                        probability= outputs.getProbabilityAsCategoryList();
                        max = probability.get(0).getScore();
                        label=probability.get(0).getLabel();
                        if (probability.get(1).getScore() > max)
                            label=probability.get(1).getLabel();
                        if (probability.get(2).getScore() > max)
                            label=probability.get(2).getLabel();
                        if (probability.get(3).getScore() > max)
                            label=probability.get(3).getLabel();

                        if(!label.equals("happy"))
                        {
                            partial_score++;
                        }
                        max=0;
                        label="0";
                        Log.e("5 done", " ");
                    }
                    model.close();
                    if(partial_score>happy3Frames.size()/2)
                        score++;
                    Log.e("all done", ":(((((((((((");
                    (new Handler()).postDelayed(
                            new Runnable() {
                                public void run() {
                                    try {
                                        loading.setVisibility(View.INVISIBLE);
                                        Intent toScore= new Intent(EmotionTest.this,OtherResult.class);
                                        toScore.putExtra("nonToddlerScore", score);
                                        EmotionTest.this.startActivity(toScore);
                                        finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            ,
                            100000);

                    //ToEmotionResult.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
    public static String max(Category a, Category b, Category c, Category d) {

        float max = a.getScore();
        String label=a.getLabel();
        if (b.getScore() > max)
            label=b.getLabel();
        if (c.getScore() > max)
        label=c.getLabel();
        if (d.getScore() > max)
        label=d.getLabel();

        return label;
    }
    private MappedByteBuffer loadModel() throws IOException
    {
        AssetFileDescriptor descriptor=this.getAssets().openFd("emotion classification.tflite");
        FileInputStream fileInputStream=new FileInputStream(descriptor.getFileDescriptor());
        FileChannel fileChannel=fileInputStream.getChannel();
        long startOffset = descriptor.getStartOffset();
        long declaredLength= descriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength);
    }
    private ByteBuffer getByteBuffer(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        ByteBuffer mImgData = ByteBuffer
                .allocateDirect(4 * width * height);
        mImgData.order(ByteOrder.nativeOrder());
        int[] pixels = new int[width*height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int pixel : pixels) {
            float value = (float) Color.red(pixel)/255.0f;
            mImgData.putFloat(value);
           // mImgData.putFloat((float) Color.red(pixel));
        }
        return mImgData;
    }
    TensorImage processImage(Bitmap sourceImage) {
      /*  ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeOp(48, 48, ResizeOp.ResizeMethod.BILINEAR))
                        .build();*/
        TensorImage tImage = new TensorImage(DataType.FLOAT32);
        tImage.load(sourceImage);
        //tImage = imageProcessor.process(tImage);
        return tImage;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed(); return  true;
    }
}