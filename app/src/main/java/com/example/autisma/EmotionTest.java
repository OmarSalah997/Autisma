package com.example.autisma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EmotionTest extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_test);
        Button ToDotTest=findViewById(R.id.emotion_done);
        TextView dummy=findViewById(R.id.dummyScore);
        Intent intent = getIntent();
        final int score = intent.getIntExtra("5Qscore",0);
        dummy.setText(String.valueOf(score));
        ToDotTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), pressDotActivity.class);
                startActivity(myIntent);
            }
        });
    }
}