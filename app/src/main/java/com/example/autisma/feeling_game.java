package com.example.autisma;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class feeling_game extends AppCompatActivity {
    Button h;
    Button s;
    Button a;
    int feelingIndex;
    int imgIndex;
    ImageView img , result;
    String[] feelings = {"happy", "sad","angry"};
    MediaPlayer mp;
    Context context = this;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling_game);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        h = findViewById(R.id.happy);
        s = findViewById(R.id.sad);
        a = findViewById(R.id.angry);
        img = findViewById(R.id.feeling_image);
        result = findViewById(R.id.result);
        generate_game();

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setVisibility(View.GONE);
                mp.stop();
                mp.release();
                generate_game();
            }
        });


        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feelingIndex == 0){
                    mp = MediaPlayer.create(context, R.raw.cheer);
                    mp.start();
                    result.setImageResource(R.drawable.right_sign);
                }
                else{
                    mp = MediaPlayer.create(context, R.raw.fail);
                    mp.start();
                    result.setImageResource(R.drawable.wrong_sign);
                }

                result.setVisibility(View.VISIBLE);


            }

        });

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feelingIndex == 1){
                    mp = MediaPlayer.create(context, R.raw.cheer);
                    mp.start();
                    result.setImageResource(R.drawable.right_sign);
                }
                else{
                    mp = MediaPlayer.create(context, R.raw.fail);
                    mp.start();
                    result.setImageResource(R.drawable.wrong_sign);
                }

                result.setVisibility(View.VISIBLE);

            }

        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feelingIndex == 3){
                    mp = MediaPlayer.create(context, R.raw.cheer);
                    mp.start();
                    result.setImageResource(R.drawable.right_sign);}
                else {
                    mp = MediaPlayer.create(context, R.raw.fail);
                    mp.start();
                    result.setImageResource(R.drawable.wrong_sign);
                }
                result.setVisibility(View.VISIBLE);

            }

        });
    }

    void  generate_game (){
        Random rd = new Random(); // creating Random object

        feelingIndex = rd.ints(0, 2).findFirst().getAsInt();

        imgIndex = rd.ints(1, 5).findFirst().getAsInt();

        int resId = this.getResources().getIdentifier(feelings[feelingIndex]+ imgIndex,"drawable",this.getPackageName());
        Picasso.get()
                .load(resId)
                .resize(760, 720)
                .into(img);

    }
}