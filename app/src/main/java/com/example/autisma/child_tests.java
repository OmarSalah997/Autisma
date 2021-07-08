package com.example.autisma;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class child_tests extends AppCompatActivity {
    private Button  dotTest,videoTest,MCQ;
    private CheckBox autistic_child;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_tests);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        autistic_child=findViewById(R.id.checkBox_autistic);
        dotTest=findViewById(R.id.button_takephoto_test);
        videoTest=findViewById(R.id.button_video_test);
        MCQ=findViewById(R.id.button_mcq);
        dotTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), pressDotActivity.class);
                if(autistic_child.isChecked())
                    myIntent.putExtra("autistic_child",1);
                else
                    myIntent.putExtra("autistic_child",0);
                startActivity(myIntent);
            }
        });
        videoTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), RecordingActivity.class);
                if(autistic_child.isChecked())
                myIntent.putExtra("autistic_child",1);
                else
                    myIntent.putExtra("autistic_child",0);
                startActivity(myIntent);

            }
        });
        MCQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), Quiz_Activity.class);
                if(autistic_child.isChecked())
                    myIntent.putExtra("autistic checkbox",1);
                else
                    myIntent.putExtra("autistic checkbox",0);
                startActivity(myIntent);
            }
        });
    }
}