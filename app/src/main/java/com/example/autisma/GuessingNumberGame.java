package com.example.autisma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.Collections;

public class GuessingNumberGame extends AppCompatActivity {

    int number_in_iv;
    int number_in_btn;
    Integer[] arr = new Integer[10];
    int it;
    int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guessing_number_game);

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        final MediaPlayer wrongSound = MediaPlayer.create(this,R.raw.wrong);
        final MediaPlayer rightSound = MediaPlayer.create(this,R.raw.right);
        ImageView guessingimageView = (ImageView)findViewById(R.id.GuesseingNumberIV);
        Button button0 = (Button)findViewById(R.id.button0);
        Button button1 = (Button)findViewById(R.id.button1);
        Button button2 = (Button)findViewById(R.id.button2);
        Button button3 = (Button)findViewById(R.id.button3);
        Button button4 = (Button)findViewById(R.id.button4);
        Button button5 = (Button)findViewById(R.id.button5);
        Button button6 = (Button)findViewById(R.id.button6);
        Button button7 = (Button)findViewById(R.id.button7);
        Button button8 = (Button)findViewById(R.id.button8);
        Button button9 = (Button)findViewById(R.id.button9);

        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        Collections.shuffle(Arrays.asList(arr));
        //System.out.println(Arrays.toString(arr));

        it = 0;
        number = arr[it];
        String number_string = toStr(number);
        guessingimageView.setImageResource(getResources().getIdentifier(number_string, "drawable", getPackageName()));
        number_in_iv = number;

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number_in_iv == 0)
                {
                    rightSound.start();
                    it++;
                    if (it==9)
                    {
                        startActivity(new Intent(GuessingNumberGame.this, GuessingNumberGameOver.class));
                    }
                    number = arr[it];
                    String number_string = toStr(number);
                    guessingimageView.setImageResource(getResources().getIdentifier(number_string, "drawable", getPackageName()));
                    number_in_iv = number;
                }
                else
                {
                    wrongSound.start();
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number_in_iv == 1)
                {
                    rightSound.start();
                    it++;
                    if (it==9)
                    {
                        startActivity(new Intent(GuessingNumberGame.this, GuessingNumberGameOver.class));
                    }
                    number = arr[it];
                    String number_string = toStr(number);
                    guessingimageView.setImageResource(getResources().getIdentifier(number_string, "drawable", getPackageName()));
                    number_in_iv = number;
                }
                else
                {
                    wrongSound.start();
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number_in_iv == 2)
                {
                    rightSound.start();
                    it++;
                    if (it==9)
                    {
                        startActivity(new Intent(GuessingNumberGame.this, GuessingNumberGameOver.class));
                    }
                    number = arr[it];
                    String number_string = toStr(number);
                    guessingimageView.setImageResource(getResources().getIdentifier(number_string, "drawable", getPackageName()));
                    number_in_iv = number;
                }
                else
                {
                    wrongSound.start();
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number_in_iv == 3)
                {
                    rightSound.start();
                    it++;
                    if (it==9)
                    {
                        startActivity(new Intent(GuessingNumberGame.this, GuessingNumberGameOver.class));
                    }
                    number = arr[it];
                    String number_string = toStr(number);
                    guessingimageView.setImageResource(getResources().getIdentifier(number_string, "drawable", getPackageName()));
                    number_in_iv = number;
                }
                else
                {
                    wrongSound.start();
                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number_in_iv == 4)
                {
                    rightSound.start();
                    it++;
                    if (it==9)
                    {
                        startActivity(new Intent(GuessingNumberGame.this, GuessingNumberGameOver.class));
                    }
                    number = arr[it];
                    String number_string = toStr(number);
                    guessingimageView.setImageResource(getResources().getIdentifier(number_string, "drawable", getPackageName()));
                    number_in_iv = number;
                }
                else
                {
                    wrongSound.start();
                }
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number_in_iv == 5)
                {
                    rightSound.start();
                    it++;
                    if (it==9)
                    {
                        startActivity(new Intent(GuessingNumberGame.this, GuessingNumberGameOver.class));
                    }
                    number = arr[it];
                    String number_string = toStr(number);
                    guessingimageView.setImageResource(getResources().getIdentifier(number_string, "drawable", getPackageName()));
                    number_in_iv = number;
                }
                else
                {
                    wrongSound.start();
                }
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number_in_iv == 6)
                {
                    rightSound.start();
                    it++;
                    if (it==9)
                    {
                        startActivity(new Intent(GuessingNumberGame.this, GuessingNumberGameOver.class));
                    }
                    number = arr[it];
                    String number_string = toStr(number);
                    guessingimageView.setImageResource(getResources().getIdentifier(number_string, "drawable", getPackageName()));
                    number_in_iv = number;
                }
                else
                {
                    wrongSound.start();
                }
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number_in_iv == 7)
                {
                    rightSound.start();
                    it++;
                    if (it==9)
                    {
                        startActivity(new Intent(GuessingNumberGame.this, GuessingNumberGameOver.class));
                    }
                    number = arr[it];
                    String number_string = toStr(number);
                    guessingimageView.setImageResource(getResources().getIdentifier(number_string, "drawable", getPackageName()));
                    number_in_iv = number;
                }
                else
                {
                    wrongSound.start();
                }
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number_in_iv == 8)
                {
                    rightSound.start();
                    it++;
                    if (it==9)
                    {
                        startActivity(new Intent(GuessingNumberGame.this, GuessingNumberGameOver.class));
                    }
                    number = arr[it];
                    String number_string = toStr(number);
                    guessingimageView.setImageResource(getResources().getIdentifier(number_string, "drawable", getPackageName()));
                    number_in_iv = number;
                }
                else
                {
                    wrongSound.start();
                }
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number_in_iv == 9)
                {
                    rightSound.start();
                    it++;
                    if (it==9)
                    {
                        startActivity(new Intent(GuessingNumberGame.this, GuessingNumberGameOver.class));
                    }
                    number = arr[it];
                    String number_string = toStr(number);
                    guessingimageView.setImageResource(getResources().getIdentifier(number_string, "drawable", getPackageName()));
                    number_in_iv = number;
                }
                else
                {
                    wrongSound.start();
                }
            }
        });

    }

    String toStr (int num){
        String ret = "";
        switch (num){
            case 0:
                ret = "zero";
                break;
            case 1:
                ret = "one";
                break;
            case 2:
                ret = "twoo";
                break;
            case 3:
                ret = "three";
                break;
            case 4:
                ret = "four";
                break;
            case 5:
                ret = "five";
                break;
            case 6:
                ret = "six";
                break;
            case 7:
                ret = "seven";
                break;
            case 8:
                ret = "eight";
                break;
            case 9:
                ret = "nine";
                break;
            default:
                ret = "zero";
        }
        return ret;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}