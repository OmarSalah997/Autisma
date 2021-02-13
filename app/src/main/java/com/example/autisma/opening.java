package com.example.autisma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class opening extends AppCompatActivity {
    Animation updown;
    Animation fade;
    ImageView logo;
    ImageView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        logo = (ImageView) findViewById(R.id.imageView);
        name = (ImageView) findViewById(R.id.imageView3);
        updown = AnimationUtils.loadAnimation(this,R.anim.updown);
        fade = AnimationUtils.loadAnimation(this,R.anim.fadein);
        name.setAnimation(fade);
        logo.setAnimation(updown);
        SharedPreferences prefs2= getSharedPreferences("MY_APP", Activity.MODE_PRIVATE);
        if (!prefs2.getString("name", "").equals("")){
            final Intent intent = new Intent(this, MainHOME.class);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                }
            }, 5000);

        }
        else {

            final Intent intent = new Intent(this, WelcomeActivity.class);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                }
            }, 5000);
        }

    }
}