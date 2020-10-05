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


    private String mQuestions [] = {
            "He/She often notices small sounds when others don't",
            "He/She usually concentrates more on the whole picture rather than the small details",
            "In a social group ,He/She can easily keep track of several different people's conversations",
            "He/She finds it easy to go back and fourth between different activites",
            "He/She doesn't know how to keep a conversation going with his/her peers",
            "He/She is good at social chit-chat",
            "When He/She reads a story,he finds it difficult to work out the charachter's intenons or feelings",
            "When He/She was in preschool,he/she used to enjoy playing games involving pretending with other children",
            "He/She finds it easy to work out what someone is thinking or feeling just by looking at their face",
            "He/She finds it hard to make new friends"

    };
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
                    mButtonChoice1.setBackgroundColor(Color.BLUE);
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
                    mButtonChoice2.setBackgroundColor(Color.BLUE);
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
                    mButtonChoice3.setBackgroundColor(Color.BLUE);
                    Chosen=true;

                }
            });
            mButtonChoice4.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //My logic for Button goes in here
                    mAnswers.add(4);
                    mButtonChoice4.setBackgroundColor(Color.BLUE);
                    Chosen=true;

                }
            });
            Next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Chosen== true){
                    updateQuestion();
                    if(mQuestionNumber == 9)
                    Next.setText("Sumbit");
                    mButtonChoice1.setBackgroundColor(0xFF4D68EA);
                    mButtonChoice2.setBackgroundColor(0xFF4D68EA);
                    mButtonChoice3.setBackgroundColor(0xFF4D68EA);
                    mButtonChoice4.setBackgroundColor(0xFF4D68EA);
                        Chosen=false;
                    if(Next.getText()=="Sumbit"){
                        //start another activity
                        startActivity(new Intent(getApplicationContext(),PersonalDetails.class));
                    }
}else{
                        Toast.makeText(Quiz_Activity.this, "Please choose an answer", Toast.LENGTH_SHORT).show();
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
        String question = mQuestions[a];
        return question;
    }



}
