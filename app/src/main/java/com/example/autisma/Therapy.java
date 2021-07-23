package com.example.autisma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Therapy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapy);
        TextView textView = (TextView)findViewById(R.id.TherapyTV);
        InputStream inputStream = this.getResources().openRawResource(R.raw.therapyanswer);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer stringBuffer = new StringBuffer();
        String therapydata = "";
        if(inputStream!=null){
            try {
                while ((therapydata = bufferedReader.readLine())!=null){
                    stringBuffer.append(therapydata + "\n");
                }
                textView.setText(stringBuffer);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}