package com.example.autisma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Therapy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapy);

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}