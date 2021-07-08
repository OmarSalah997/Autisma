package com.example.autisma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ToddlerTestIntro extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toddler_test_intro);
        Button start=findViewById(R.id.startRecord);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int orientation = getResources().getConfiguration().orientation;
                if(orientation == Configuration.ORIENTATION_LANDSCAPE)
                    startActivity(new Intent(getApplicationContext(),RecordingActivity.class));
                else
                    Toast.makeText(ToddlerTestIntro.this,getString(R.string.landscape) , Toast.LENGTH_SHORT).show();
                }
        });
    }
}