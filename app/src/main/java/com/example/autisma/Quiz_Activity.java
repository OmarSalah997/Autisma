package com.example.autisma;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Quiz_Activity extends AppCompatActivity {


    private String[] mQuestions;
    ArrayList<Integer> mAnswers = new ArrayList<Integer>();

    private TextView mScoreView;
        private TextView mQuestionView;
        private TextView questionNB ;
        private Button mButtonChoice1;
        private Button mButtonChoice2;
        private Button mButtonChoice3;
        private Button mButtonChoice4;
        private Button Next;
        private Boolean Chosen=false ;
        private int mScore = 0;
        private int mQuestionNumber = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.quiz_activity);
            mQuestions = new String[]{
                    getString(R.string.question1),
                    getString(R.string.question2),
                    getString(R.string.question3),
                    getString(R.string.question4),
                    getString(R.string.question5),
                    getString(R.string.question6),
                    getString(R.string.question7),
                    getString(R.string.question8),
                    getString(R.string.question9),
                    getString(R.string.question10)

            };

            mQuestionView = (TextView)findViewById(R.id.question);
            mButtonChoice1 = (Button)findViewById(R.id.choice1);
            mButtonChoice2 = (Button)findViewById(R.id.choice2);
            mButtonChoice3 = (Button)findViewById(R.id.choice3);
            mButtonChoice4 = (Button)findViewById(R.id.choice4);
            Next=(Button)findViewById(R.id.next);
            questionNB=(TextView)findViewById(R.id.questionNB);
            updateQuestion();

            //Start of Button Listener for Button1
            mButtonChoice1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //My logic for Button goes in here
                    mAnswers.add(1);
                    mButtonChoice1.setBackground(getDrawable(R.drawable.checked_button_shape));
                   Chosen=true;
                }
            });

            //End of Button Listener for Button1

            //Start of Button Listener for Button2
            mButtonChoice2.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //My logic for Button goes in here
                    mAnswers.add(2);
                    mButtonChoice2.setBackground(getDrawable(R.drawable.checked_button_shape));
                    Chosen=true;
                }
            });

            //End of Button Listener for Button2


            //Start of Button Listener for Button3
            mButtonChoice3.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //My logic for Button goes in here
                    mAnswers.add(3);
                    mButtonChoice3.setBackground(getDrawable(R.drawable.checked_button_shape));
                    Chosen=true;

                }
            });
            mButtonChoice4.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //My logic for Button goes in here
                    mAnswers.add(4);
                    mButtonChoice4.setBackground(getDrawable(R.drawable.checked_button_shape));
                    Chosen=true;

                }
            });
            Next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Chosen){

                        if(mQuestionNumber == 10)
                            Next.setText(getString(R.string.submit));
                        mButtonChoice1.setBackground(getDrawable(R.drawable.button_shape));
                        mButtonChoice2.setBackground(getDrawable(R.drawable.button_shape));
                        mButtonChoice3.setBackground(getDrawable(R.drawable.button_shape));
                        mButtonChoice4.setBackground(getDrawable(R.drawable.button_shape));
                        Chosen=false;
                        if((Next.getText().equals("Sumbit")||Next.getText().equals("ارسال")))
                            {
                            //start another activity
                            startActivity(new Intent(getApplicationContext(),PersonalDetails.class));
                            return;
                            }
                        updateQuestion();
                    }
                    else
                        {
                        Toast.makeText(Quiz_Activity.this, getString(R.string.Please_choose_an_answer), Toast.LENGTH_SHORT).show();
                        }
                }
            });

        }

        private void updateQuestion(){
            mQuestionView.setText(getQuestion(mQuestionNumber));
            mQuestionNumber++;
            questionNB.setText(Integer.toString(mQuestionNumber));

        }


        private void updateScore(int point) {

        }
    public String getQuestion(int a) {
        return mQuestions[a];
    }



}
