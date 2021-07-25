package com.example.autisma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;


import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.autisma.Sound_classification.DTW;
import com.example.autisma.Sound_classification.MFCC;
import com.example.autisma.Sound_classification.WavFile;
import com.example.autisma.Sound_classification.WavFileException;

public class SpeakingActivity extends AppCompatActivity {
    public Vector<int[]> vec = new Vector<int[]>(72);

    public int numberOfSpeaker=3;
    ArrayList<Integer> classifications=new ArrayList<Integer>();
    public int classification;
    int filesNumber=0;
    int speakScore=0;
    int result=0;
    private Thread recordingThread;
    public int w;
    private AudioRecord mRecorder;
    private boolean isRecording = false;
    public static int SAMPLE_RATE = 16000;
    private static final int samplingRates[] = {16000, 11025, 11000, 8000, 6000};
    private short[] mBuffer;


    final static public int RECORDER_SAMPLERATE = 44100;
    final static public int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    final static public int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    final static public int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    final static public int BytesPerElement = 2; // 2 bytes in 16bit format*/
    String storagePath;
    File dir ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        storagePath= String.valueOf(getApplicationContext().getFilesDir());

         dir= new File(storagePath, "/autisma_files");
        Intent intent = getIntent();
        result = intent.getIntExtra("5Qscore",0);
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) ){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) ){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        }
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) ){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        transfer_files();
        final Button start = findViewById(R.id.start_speaking);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
setContentView(R.layout.activity_two);
                final Button stop = findViewById(R.id.next_two);
                startRecording(0);

                stop.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Code here executes on main thread after user presses button
                        mRecorder.stop();
                        isRecording = false;
                        mRecorder.release();
                        Log.e("STOP", "STOP");

                        File f1 = new File(dir + "/z0.pcm"); // The location of your PCM file
                        File f2 = new File(dir + "/recorded0.wav"); // The location where you want your WAV file
                        try {
                            rawToWave(f1, f2);
                            calculatesingleMFCC(f2);
                        } catch (IOException | WavFileException e) {
                            e.printStackTrace();
                        }
                        setContentView(R.layout.activity_cat);
                        startRecording(1);
                        final  Button finish=findViewById(R.id.next_cat);
                        finish.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                mRecorder.stop();
                                isRecording = false;
                                mRecorder.release();
                                Log.e("STOP", "STOP");
                                File f1 = new File(dir + "/z1.pcm"); // The location of your PCM file
                                File f2 = new File(dir + "/recorded1.wav"); // The location where you want your WAV file
                                try {
                                    rawToWave(f1, f2);
                                    calculatesingleMFCC(f2);
                                } catch (IOException | WavFileException e) {
                                    e.printStackTrace();
                                }
                                try {

                                    calculateMFCCs();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (WavFileException e) {
                                    e.printStackTrace();
                                }
                                compute_distances(0);
                                classify();
                                Log.e("w", String.valueOf(w));
                                if (w == 1 || w == 3){
                                    speakScore ++;
                                    Log.e("classified correct", "class correct");
                            }
                                compute_distances(1);
                                classify();
                                Log.e("w", String.valueOf(w));
                                if(w==2 || w==0){
                                Log.e("classified correct","class correct");
                                speakScore ++;}
                                Intent toEmotion= new Intent(SpeakingActivity.this,EmotionTest.class);
                                toEmotion.putExtra("5Qscore",speakScore+result);
                                startActivity(toEmotion);
                            };


                        });


                    }
                });

            }
        });


    }

    private void startRecording(int u) {

        mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);

        mRecorder.startRecording();
        isRecording = true;

        recordingThread = new Thread(new Runnable() {
            public void run() {
                try {
                    writeAudioDataToFile(u);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
/*
        for (int rate : samplingRates) {
            int bufferSize = AudioRecord.getMinBufferSize(rate, AudioFormat.CHANNEL_CONFIGURATION_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
            if (bufferSize > 0) {
                SAMPLE_RATE=rate;
                break;
            }
        }
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        mBuffer = new short[bufferSize];
        mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);
       isRecording = true;
        mRecorder.startRecording();*/


    }

    private void writeAudioDataToFile(int u) throws FileNotFoundException {
        if (!dir.exists())
            dir.mkdirs();
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(dir + "/z"+String.valueOf(u)+".pcm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format
            short sData[] = new short[BufferElements2Rec];
            mRecorder.read(sData, 0, BufferElements2Rec);
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer

                byte bData[] = short2byte(sData);

                os.write(bData, 0, BufferElements2Rec * BytesPerElement);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        }




    //convert short to byte
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    private void rawToWave(final File rawFile, final File waveFile) throws IOException {
        Log.e("raw to wave", "raw to wave");


        byte[] rawData = new byte[(int) rawFile.length()];
        DataInputStream input = null;
        try {
            input = new DataInputStream(new FileInputStream(rawFile));
            input.read(rawData);
        } finally {
            if (input != null) {
                input.close();
            }
        }

        DataOutputStream output = null;
        try {
            output = new DataOutputStream(new FileOutputStream(waveFile));
            // WAVE header
            // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, 44100); // sample rate
            writeInt(output, RECORDER_SAMPLERATE * 2); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
       //     Audio data (conversion big endian -> little endian)
            short[] shorts = new short[rawData.length / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts) {
                bytes.putShort(s);
            }

            output.write(fullyReadFileToBytes(rawFile));
        } finally {
            rawFile.delete();
            if (output != null) {
                output.close();
            }
        }
    }

    byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis = new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        } catch (IOException e) {
            throw e;
        } finally {
            fis.close();

        }

        return bytes;
    }

    private void writeInt(final DataOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private void writeShort(final DataOutputStream output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
    }

    private void writeString(final DataOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }

    protected void calculateMFCCs() throws IOException, WavFileException {

                Log.e("clculate mfccs","hbkjm");
        AssetManager assetManager = getApplicationContext().getResources().getAssets();

        String[] files = null;
try {

    files = assetManager.list("files"); //ringtone is folder name
    filesNumber = files.length;

    for (int i = 0; i < (files.length); i += 1) {


        File file;

        file = new File(String.valueOf(getApplicationContext().getFilesDir()) + "/autisma_files/", files[i]);
        try {
          readMfcctoVec(file);
          // int[] mean_mfcc=calculatesingleMFCC(file);
          //  writeMFCC(file.getName(),mean_mfcc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}catch(IOException e){
            e.printStackTrace();
    }


    }
    public int[] calculatesingleMFCC(File file) throws IOException, WavFileException {

        Log.e("calculating single mfcc", "uvjh");
        int mNumFrames;
        int mSampleRate;
        int mChannels;
        int[] meanMFCCValues = null;

        try {
            WavFile wavFile;
            wavFile = WavFile.openWavFile(file);
            mNumFrames = (int) wavFile.numFrames;
            mSampleRate = (int) wavFile.sampleRate;
            mChannels = wavFile.numChannels;
            double[][] buffer = new double[mChannels][];
            for (int l = 0; l < mChannels; l++) {
                double[] var = new double[mNumFrames];
                buffer[l] = var;
            }

            int frameOffset = 0;

            int loopCounter = mNumFrames * mChannels / 4096 + 1;
            for (int p = 0; p < loopCounter; p++) {
                frameOffset = wavFile.readFrames(buffer, mNumFrames, frameOffset);
            }

            //trimming the magnitude values to 5 decimal digits
            DecimalFormat df = new DecimalFormat("#.#####");
            df.setRoundingMode(RoundingMode.CEILING);
            double[] meanBuffer = new double[mNumFrames];
            for (int q = 0; q < mNumFrames; q++) {
                double frameVal = 0.0;
                for (int p = 0; p < mChannels; p++) {
                    frameVal = frameVal + buffer[p][q];
                }
                meanBuffer[q] = frameVal / mChannels;
            }


            //MFCC java library.
            MFCC mfccConvert = new MFCC();
            mfccConvert.setSampleRate(mSampleRate);
            int nMFCC = 40;
            mfccConvert.setN_mfcc(nMFCC);
            float[] mfccInput = mfccConvert.process(meanBuffer);
            int nFFT = mfccInput.length / nMFCC;
            double[][] mfccValues = new double[nMFCC][];
            for (int j = 0; j < nMFCC; j++) {
                double[] var = new double[nFFT];
                mfccValues[j] = var;
            }

            //loop to convert the mfcc values into multi-dimensional array
            for (int k = 0; k < nFFT; k++) {
                int indexCounter = k * nMFCC;
                int rowIndexValue = k % nFFT;
                for (int j = 0; j < nMFCC; j++) {
                    mfccValues[j][rowIndexValue] = mfccInput[indexCounter];
                    indexCounter++;
                }
            }


            //code to take the mean of mfcc values across the rows such that
            //[nMFCC x nFFT] matrix would be converted into
            //[nMFCC x 1] dimension - which would act as an input

            meanMFCCValues = new int[nMFCC];
            for (int p = 0; p < nMFCC; p++) {
                double fftValAcrossRow = 0.0;
                for (int q = 0; q < nFFT; q++)
                    fftValAcrossRow = fftValAcrossRow + mfccValues[p][q];
                double fftMeanValAcrossRow = fftValAcrossRow / nFFT;
                meanMFCCValues[p] = (int) fftMeanValAcrossRow;

            }
            file.delete();
            vec.add(meanMFCCValues);
         

        
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WavFileException e) {
            e.printStackTrace();
        }
        return meanMFCCValues;
}

    public void writeMFCC(String fileName, int[] j) throws IOException {
        FileOutputStream out = new FileOutputStream(String.valueOf(Environment.getExternalStorageDirectory()) + "/"+fileName+".raw");
        byte buf[] = new byte[4 * j.length];
        for (int i = 0; i < j.length; ++i) {
            int val = (int) j[i];
          /*  buf[4 * i] = (byte) (val >> 24);
            buf[4 * i + 1] = (byte) (val >> 16);
            buf[4 * i + 2] = (byte) (val >> 8);
            buf[4 * i + 3] = (byte) (val);*/
            out.write(val>> 0);
            out.write(val >> 8);
            out.write(val >> 16);
            out.write(val >> 24);
        }

        // out.write(buf);
        out.close();
    }

    public void readMfcctoVec(File f) throws IOException {
        int[] meanmfcc=new int[(int) (f.length()/4)];
        int size = (int) f.length();
        byte[] bytes = new byte[size];
        try {
            FileInputStream fin = new FileInputStream(f);
            BufferedInputStream bin = new BufferedInputStream(fin);
            DataInputStream din = new DataInputStream(bin);

            int count = (int) (f.length() / 4);
            int[] values = new int[count];
            for (int i = 0; i < count; i++) {
                meanmfcc[i] = din.readInt();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        vec.add(meanmfcc);
        f.delete();
    }





    public void compute_distances(int f){

                //TODO your background code
                Log.e("in compute distances","yfyj");
                int numberOfSpeaker = filesNumber / 4;
                int soundsclassifiers = 4;
                // Vector<Integer>classifications =new Vector<Integer>(numberOfSpeaker);
        Vector<Double> currentSpeakerDistances = new Vector<Double>(soundsclassifiers);
                for (int i = 2; i < vec.size(); i += soundsclassifiers) {
                   // Vector<Double> currentSpeakerDistances = new Vector<Double>(soundsclassifiers);
                    for (int n = i; n < soundsclassifiers + i; n++) {
                        final DTW lDTW = new DTW();
                   //     Log.e("vec", String.valueOf(vec.size()));
                        double dist = lDTW.compute(vec.get(n), vec.get(f)).getDistance();
                        Log.e("distances", String.valueOf(dist));
                        currentSpeakerDistances.add(dist);
                    }

                    classifications.add(currentSpeakerDistances.indexOf(Collections.min(currentSpeakerDistances)));

                       Log.e("min distances", String.valueOf(Collections.min(currentSpeakerDistances)));
                    currentSpeakerDistances.clear();
                }
                /*
                Log.e("class size", String.valueOf((classifications.get(2))));
                for (int i = 0; i < classifications.size(); i++)
                    Log.e("distances", "distance is " + classifications.get(i));*/

    }
    public void classify(){

        // Sort the array

        Collections.sort(classifications);

        // find the max frequency using linear
        // traversal
        int max_count = 1, res = classifications.get(0);
        int curr_count = 1;

        for (int i = 1; i < classifications.size(); i++)
        {
            if (classifications.get(i) == classifications.get(i-1))
                curr_count++;
            else
            {
                if (curr_count > max_count)
                {
                    max_count = curr_count;
                    res = classifications.get(i-1);
                }
                curr_count = 1;
            }
        }

        // If last element is most frequent
        if (curr_count > max_count)
        {
            max_count = curr_count;
            res = classifications.get(classifications.size()-1);
        }
        classification=res;
        classifications.clear();
      w=classification;

    }
    public void transfer_files() {
        Log.e("transfer","in");

        AssetManager assetManager = getApplicationContext().getResources().getAssets();

        String[] files = null;

        try {
            files = assetManager.list("files");
        } catch (Exception e) {
            Log.e("transfer files asset ", "ERROR: " + e.toString());
        }


        if (!dir.exists())
            dir.mkdirs();


        for (int i = 0; i < files.length; i++) {
            InputStream in = null;
            FileOutputStream out = null;
            try {
                in = assetManager.open("files/" + files[i]);
                File dir2 = new File (storagePath + "/autisma_files/"+files[i]);
                dir2.createNewFile();
                out = new FileOutputStream(dir+"/"+files[i]);

                byte[] buffer = new byte[65536 * 2];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }


                out.flush();
                in.close();
                in = null;
                out.close();
                out = null;
                Log.d("transfer files", "File Copied in Storage");
            } catch (Exception e) {
                Log.e("transfer files", "ERROR: " + e.toString());
            }
        }
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed(); return  true;
    }
}