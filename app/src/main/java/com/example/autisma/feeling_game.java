package com.example.autisma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class feeling_game extends AppCompatActivity {
    Button h;
    Button s;
    Button a;
    ImageView img;
    String[] feelings = {"happy" , "sad","angry"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling_game);

        h=(Button) findViewById(R.id.happy);
        s=(Button) findViewById(R.id.sad);
        a=(Button) findViewById(R.id.angry);
        img=(ImageView) findViewById(R.id.feeling_image);
        Random rd = new Random(); // creating Random object

        int feelingIndex = rd.ints(0, 2).findFirst().getAsInt();

        int imgIndex = rd.ints(1, 5).findFirst().getAsInt();

        int resId = this.getResources().getIdentifier(feelings[feelingIndex]+ imgIndex,"drawable",this.getPackageName());

        img.setImageResource(resId);

        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });
    }
}