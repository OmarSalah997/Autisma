package com.example.autisma;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class pressDotActivity extends AppCompatActivity {
    ImageButton dot ;
    TextView instructions;
    Button nxt;
    RelativeLayout window;
    int maxW;
    int minW;
    int maxH;
    int minH;
    int result=0;
    int count=0;
    int dotCount;
    Date Satrt;
    boolean firstdot=false;
    boolean pressed=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int score = intent.getIntExtra("5Qscore",0);
        setContentView(R.layout.activity_pressdot);
        instructions=findViewById(R.id.Dot_instructions);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        dot=findViewById(R.id.dot);
        nxt=findViewById(R.id.toEmotion);
        window=findViewById(R.id.Window);

        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count==0)
                {
                    instructions.setText(getString(R.string.Dot2));
                    count++;
                    return;
                }
                if(count==1)
                {
                    instructions.setVisibility(View.GONE);
                    nxt.setVisibility(View.GONE);
                    dot.setVisibility(View.VISIBLE);
                    count++;
                    Satrt = Calendar.getInstance().getTime();
                }
                if(dotCount>=4)
                {
                    Intent toScore= new Intent(pressDotActivity.this, SpeakingActivity.class);
                    toScore.putExtra("5Qscore", score+result);
                    pressDotActivity.this.startActivity(toScore);
                    finish();
                }
            }
        });

        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dotCount++;
                changeDot();
                Date presstime=Calendar.getInstance().getTime();
                long diffInMs = presstime.getTime() - Satrt.getTime();
                long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
                if(diffInSec>2)
                    result++;
                if(dotCount>=5)
                {
                    dot.setVisibility(View.INVISIBLE);
                    nxt.setVisibility(View.VISIBLE);
                    return;
                }
                Satrt = Calendar.getInstance().getTime();
            }
        });


    }
    private void changeDot(){
        maxW= window.getWidth()-200;
        minW=0;
        maxH=window.getHeight()-400;
        minH=0;
        final int randomwidth = new Random().nextInt((maxW - minW) + 1) + minW;
        final int randomheight = new Random().nextInt((maxH- minH) + 1) + minH;
        dot.setX(randomwidth);
        dot.setY(randomheight);
        dot.invalidate();

    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder warning = new AlertDialog.Builder(this);
        warning.setIcon(R.drawable.wrong_sign);
        warning.setTitle(R.string.warning);
        warning.setMessage(R.string.resLost);
        warning.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(getBaseContext(), child_tests.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        warning.setNegativeButton(getString(R.string.cancel),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        warning.show();
    }
}
