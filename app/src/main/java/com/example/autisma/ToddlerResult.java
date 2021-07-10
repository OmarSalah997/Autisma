package com.example.autisma;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ToddlerResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toddler_result);
        Intent intent = getIntent();
        final int Result=intent.getIntExtra("ToddlerScore",0);
        Button ToMCHAT=findViewById(R.id.proceed_to_MCHAT);
        ToMCHAT.setVisibility(View.INVISIBLE);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        TextView details=findViewById(R.id.Gscore_details);
        com.github.lzyzsd.circleprogress.DonutProgress Scorebar=findViewById(R.id.Gscore_progress);
        if(Result<=6)
        {
            float f=((float) Result/(float)24)*100;
            Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
            Scorebar.setUnfinishedStrokeColor(Color.GRAY);
            Scorebar.setFinishedStrokeColor(Color.GREEN);
            details.setText(R.string.ToddlerLow);
        }
        if(Result>13 & Result<=18)
        {
            float f=((float) Result/(float)24)*100;
            Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
            Scorebar.setUnfinishedStrokeColor(Color.GRAY);
            Scorebar.setFinishedStrokeColor(Color.YELLOW);
            details.setText(R.string.ToddlerMedium);
        }
        if(Result>=19)
        {
            float f=((float) Result/(float)24)*100;
            Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
            Scorebar.setUnfinishedStrokeColor(Color.GRAY);
            Scorebar.setFinishedStrokeColor(Color.RED);
            details.setText(R.string.ToddlerHigh);
            ToMCHAT.setVisibility(View.VISIBLE);
        }
        Button done=findViewById(R.id.gilliam_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainHOME.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        ToMCHAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), M_chat_info.class);
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