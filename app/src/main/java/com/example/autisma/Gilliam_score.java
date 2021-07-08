package com.example.autisma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Gilliam_score extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gilliam_score);
        Intent intent = getIntent();
        int totalScore = intent.getIntExtra("totalScore",0);
        int Stereotyped_Behaviors_score = intent.getIntExtra("Stereotyped_Behaviors_score",0);
        int Communication_Behaviors_score = intent.getIntExtra("Communication_Behaviors_score",0);
        int Social_Interaction_Behaviors_score = intent.getIntExtra("Social_Interaction_Behaviors_score",0);

    }
}