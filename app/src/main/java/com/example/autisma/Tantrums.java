package com.example.autisma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Tantrums extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tantrums);
        TextView textView = (TextView)findViewById(R.id.TantrumsTV);
        InputStream inputStream = this.getResources().openRawResource(R.raw.tantrumsanswer);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer stringBuffer = new StringBuffer();
        String echodata = "";
        if(inputStream!=null){
            try {
                while ((echodata = bufferedReader.readLine())!=null){
                    stringBuffer.append(echodata + "\n");
                }
                textView.setText(stringBuffer);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}