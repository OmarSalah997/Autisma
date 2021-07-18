package com.example.autisma;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Gilliam_main extends AppCompatActivity {
    int total_score=0;
    int questionNumber=1;
    int Stereotyped_Behaviors_score=0;
    int Communication_Behaviors_score=0;
    int Social_Interaction_Behaviors_score=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gilliam_main);
        final RadioGroup grp=findViewById(R.id.grp);
        final Button nxt=findViewById(R.id.next);
        final TextView Qnumber=findViewById(R.id.questionNB);
        final TextView Q=findViewById(R.id.question);
        final TextView questionGroup=findViewById(R.id.questionGroup);
        final String[] mQuestions = new String[]{
                getString(R.string.g1),
                getString(R.string.g2),
                getString(R.string.g3),
                getString(R.string.g4),
                getString(R.string.g5),
                getString(R.string.g6),
                getString(R.string.g7),
                getString(R.string.g8),
                getString(R.string.g9),
                getString(R.string.g10),
                getString(R.string.g11),
                getString(R.string.g12),
                getString(R.string.g13),
                getString(R.string.g14),
                getString(R.string.g15),
                getString(R.string.g16),
                getString(R.string.g17),
                getString(R.string.g18),
                getString(R.string.g19),
                getString(R.string.g20),
                getString(R.string.g21),
                getString(R.string.g22),
                getString(R.string.g23),
                getString(R.string.g24),
                getString(R.string.g25),
                getString(R.string.g26),
                getString(R.string.g27),
                getString(R.string.g28),
                getString(R.string.g29),
                getString(R.string.g30),
                getString(R.string.g31),
                getString(R.string.g32),
                getString(R.string.g33),
                getString(R.string.g34),
                getString(R.string.g35),
                getString(R.string.g36),
                getString(R.string.g37),
                getString(R.string.g38),
                getString(R.string.g39),
                getString(R.string.g40),
                getString(R.string.g41),
                getString(R.string.g42)
        };
        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer radioButtonID = grp.getCheckedRadioButtonId();
                if(radioButtonID==-1)
                    Toast.makeText(Gilliam_main.this,getString(R.string.Please_choose_an_answer) , Toast.LENGTH_SHORT).show();
                else{

                    RadioButton radioButton = (RadioButton) grp.findViewById(radioButtonID);
                    int idx = grp.indexOfChild(radioButton);
                    grp.clearCheck();
                    total_score+=idx;
                    if(questionNumber<=13)
                    {
                        Stereotyped_Behaviors_score+=idx;
                        if(questionNumber==13)
                         questionGroup.setText(R.string.Communication_Behaviors);
                    }
                    if(questionNumber>13 & questionNumber<=27)
                    {
                        Communication_Behaviors_score+=idx;
                        if(questionNumber==27)
                            questionGroup.setText(R.string.Social_Interaction_Behaviors);
                    }
                    if(questionNumber>27)
                    {
                        Social_Interaction_Behaviors_score+=idx;
                    }

                    if(questionNumber==42)
                    {
                        Intent toScore= new Intent(Gilliam_main.this, Gilliam_score.class);
                        toScore.putExtra("totalScore", total_score);
                        toScore.putExtra("Stereotyped_Behaviors_score", Stereotyped_Behaviors_score);
                        toScore.putExtra("Communication_Behaviors_score", Communication_Behaviors_score);
                        toScore.putExtra("Social_Interaction_Behaviors_score", Social_Interaction_Behaviors_score);
                        Gilliam_main.this.startActivity(toScore);
                    }
                    else{
                    Q.setText(mQuestions[questionNumber]);
                    questionNumber++;
                    Qnumber.setText(String.valueOf(questionNumber));
                    }
                }
            }
        });
    }

}