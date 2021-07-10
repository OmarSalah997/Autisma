package com.example.autisma;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Main5Q extends AppCompatActivity {
    int total_score=0;
    int questionNumber=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5_q);
        Intent intent = getIntent();
        final int childage=intent.getIntExtra("Age",0);//200 = toddler , 300= bigger than toddler
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        final RadioGroup grp=findViewById(R.id.grp);
        final Button nxt=findViewById(R.id.next);
        final TextView Qnumber=findViewById(R.id.questionNB);
        final TextView Q=findViewById(R.id.question);
        final String[] mQuestions = new String[]{
                getString(R.string.Mq1),
                getString(R.string.Mq2),
                getString(R.string.Mq3),
                getString(R.string.Mq4),
                getString(R.string.Mq5),
                getString(R.string.Mq6)
        };
        Q.setText(mQuestions[0]);
        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer radioButtonID = grp.getCheckedRadioButtonId();
                if(radioButtonID==-1)
                    Toast.makeText(Main5Q.this,getString(R.string.Please_choose_an_answer) , Toast.LENGTH_SHORT).show();
                else{
                    RadioButton radioButton = (RadioButton) grp.findViewById(radioButtonID);
                    int idx = grp.indexOfChild(radioButton)+1;
                    grp.clearCheck();
                    total_score+=idx;
                    if(questionNumber<6)
                        Q.setText(mQuestions[questionNumber]);
                    questionNumber++;
                        if(questionNumber==7)
                        {
                            if(childage==200)
                            {
                                Intent toScore= new Intent(Main5Q.this, ToddlerTestIntro.class);
                                toScore.putExtra("5Qscore", total_score);
                                Main5Q.this.startActivity(toScore);
                                finish();
                            }
                            else if(childage==300)
                            {
                                //emotion
                                Intent toScore= new Intent(Main5Q.this, EmotionTest.class);
                                toScore.putExtra("5Qscore", total_score);
                                Main5Q.this.startActivity(toScore);
                                finish();
                            }
                        }
                        else
                        {
                            Qnumber.setText(String.valueOf(questionNumber));
                        }

                }
            }
        });
    }
    }
