package com.example.autisma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class Gilliam_score extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadlocale();
        setContentView(R.layout.activity_gilliam_score);
        Intent intent = getIntent();
        int score = intent.getIntExtra("totalScore",0);
        int Stereotyped_Behaviors_score = intent.getIntExtra("Stereotyped_Behaviors_score",0);
        int Communication_Behaviors_score = intent.getIntExtra("Communication_Behaviors_score",0);
        int Social_Interaction_Behaviors_score = intent.getIntExtra("Social_Interaction_Behaviors_score",0);
        TextView details=findViewById(R.id.Gscore_details);
        com.github.lzyzsd.circleprogress.DonutProgress Scorebar=findViewById(R.id.Gscore_progress);
        if(score<=69)
        {
            float f=((float) score/(float)126)*100;
            Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
            Scorebar.setUnfinishedStrokeColor(Color.GRAY);
            Scorebar.setFinishedStrokeColor(Color.GREEN);
            details.setText(R.string.GlowScore);
        }
        if(score>70 & score<=84)
        {
            float f=((float) score/(float)126)*100;
            Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
            Scorebar.setUnfinishedStrokeColor(Color.GRAY);
            Scorebar.setFinishedStrokeColor(Color.YELLOW);
            details.setText(R.string.GmediumScore);
        }
        if(score>=85)
        {
            float f=((float) score/(float)126)*100;
            Scorebar.setDonut_progress(String.valueOf(Math.round(f)));
            Scorebar.setUnfinishedStrokeColor(Color.GRAY);
            Scorebar.setFinishedStrokeColor(Color.RED);
            details.setText(R.string.GhighScore);
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
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), MainHOME.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void loadlocale() {
        SharedPreferences prefs= getSharedPreferences("settings_lang", Activity.MODE_PRIVATE);
        String language=prefs.getString("my lang","");
        if(language.equals(""))
            language= Resources.getSystem().getConfiguration().locale.getLanguage();
        setLocale(language);
    }
    private void setLocale(String s) {
        Locale locale= new Locale(s);
        Locale.setDefault(locale);
        Configuration config=new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor= getSharedPreferences("settings_lang",MODE_PRIVATE).edit();
        editor.putString("my lang",s);
        editor.apply();
    }

}