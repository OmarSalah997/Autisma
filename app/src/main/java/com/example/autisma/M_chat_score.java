package com.example.autisma;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class M_chat_score extends AppCompatActivity {
    int score=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_chat_score);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        score = getIntent().getIntExtra("score",2);
        TextView details=findViewById(R.id.score_details);
        com.github.lzyzsd.circleprogress.DonutProgress Scorebar=findViewById(R.id.score_progress);
        if(score<=2)
        {
        float f=((float) score/(float)20)*100;
        Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
        Scorebar.setUnfinishedStrokeColor(Color.GRAY);
        Scorebar.setFinishedStrokeColor(Color.GREEN);
        details.setText(R.string.score_10);
        }
        if(score>2 & score<=7)
        {
            float f=((float) score/(float)20)*100;
            Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
            Scorebar.setUnfinishedStrokeColor(Color.GRAY);
            Scorebar.setFinishedStrokeColor(Color.YELLOW);
            details.setText(R.string.score_35);
        }
        if(score>7 & score<=20)
        {
            float f=((float) score/(float)20)*100;
            Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
            Scorebar.setUnfinishedStrokeColor(Color.GRAY);
            Scorebar.setFinishedStrokeColor(Color.RED);
            details.setText(R.string.score_40_upove);
        }
        Button done=findViewById(R.id.mchat_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainHOME.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), MainHOME.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}