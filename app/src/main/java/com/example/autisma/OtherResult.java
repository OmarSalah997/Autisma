package com.example.autisma;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OtherResult extends AppCompatActivity {
int score;
    Button ToGilliam;
    TextView details;
    com.github.lzyzsd.circleprogress.DonutProgress Scorebar;
    TextView scoretxt;
    Button done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_result);
        Intent intent = getIntent();
        final int score = intent.getIntExtra("nonToddlerScore",0);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        getSupportActionBar().setHomeButtonEnabled(true);
        ToGilliam=findViewById(R.id.proceed_to_MCHAT);
        ToGilliam.setText(R.string.gilliam);
        ToGilliam.setVisibility(View.INVISIBLE);
        details=findViewById(R.id.Gscore_details);
        Scorebar=findViewById(R.id.Gscore_progress);
        scoretxt=findViewById(R.id.toddscore);
        done=findViewById(R.id.done);
        ToGilliam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Gilliam_info.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        if(score<=6)
        {
            float f=((float) score/(float)30)*100;
            Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
            Scorebar.setUnfinishedStrokeColor(Color.GRAY);
            Scorebar.setFinishedStrokeColor(Color.GREEN);
            details.setText(R.string.ToddlerLow);
        }
        if(score>6 & score<=18)
        {
            float f=((float) score/(float)30)*100;
            Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
            Scorebar.setUnfinishedStrokeColor(Color.GRAY);
            Scorebar.setFinishedStrokeColor(Color.YELLOW);
            details.setText(R.string.ToddlerMedium);
        }
        if(score>=19)
        {
            float f=((float) score/(float)30)*100;
            Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
            Scorebar.setUnfinishedStrokeColor(Color.GRAY);
            Scorebar.setFinishedStrokeColor(Color.RED);
            details.setText(R.string.ToddlerHigh);
            ToGilliam.setVisibility(View.VISIBLE);
        }
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
        super.onBackPressed();
    }
}