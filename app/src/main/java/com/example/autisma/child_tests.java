package com.example.autisma;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class child_tests extends AppCompatActivity {
    private Button  takePhotoTest,toddlers,morethan2yrs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadlocale();
        setContentView(R.layout.activity_child_tests);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        toddlers=findViewById(R.id.toddlers);
        morethan2yrs=findViewById(R.id.morethan2yrs);
       /* takePhotoTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UploadImageActivity.class));
            }
        })*/;
        toddlers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Tomcq= new Intent(child_tests.this, Main5Q.class);
                Tomcq.putExtra("Age", 200);
                child_tests.this.startActivity(Tomcq);
                //startActivity(new Intent(getApplicationContext(),ToddlerTestIntro.class));
            }
        });
        morethan2yrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Tomcq= new Intent(child_tests.this, Main5Q.class);
                Tomcq.putExtra("Age", 300);
                child_tests.this.startActivity(Tomcq);
               // startActivity(new Intent(getApplicationContext(),questions_Home.class));

            }
        });
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

    private void loadlocale() {
        SharedPreferences prefs= getSharedPreferences("settings_lang", Activity.MODE_PRIVATE);
        String language=prefs.getString("my lang","");
        if(language.equals(""))
            language= Resources.getSystem().getConfiguration().locale.getLanguage();
        setLocale(language);
    }
}