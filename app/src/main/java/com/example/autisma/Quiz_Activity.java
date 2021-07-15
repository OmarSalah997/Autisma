package com.example.autisma;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Quiz_Activity extends AppCompatActivity {


    private String[] mQuestions;
    ArrayList<Integer> mAnswers = new ArrayList<Integer>();

    private TextView mScoreView;
        private TextView mQuestionView;
        private TextView questionNB ;
        private RadioButton mButtonChoice1;
        private RadioButton mButtonChoice2;
        private RadioButton mButtonChoice3;
        private RadioButton mButtonChoice4;
        private RadioGroup grp;
        private Button Next;
        private Boolean Chosen=false ;
        private int mScore = 0;
        private int mQuestionNumber = 0;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;
    private File answersFile ;

    String Token  ;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.quiz_activity);
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            File answersFolder = new File(
                    Environment.getExternalStorageDirectory(), "autisma_answers");
            if (!answersFolder.exists())
                answersFolder.mkdirs(); // <----

            answersFile = new File(answersFolder, System.currentTimeMillis()+".txt");  //file name + extension is .txt
            SharedPreferences preferences ;
            preferences= getSharedPreferences("MY_APP",Activity.MODE_PRIVATE);
            Token=preferences.getString("TOKEN",null);
            if(getSupportActionBar()!=null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ActionBar actionBar = getSupportActionBar();
            if(actionBar !=null)
                actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
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
            mButtonChoice1 = (RadioButton)findViewById(R.id.choice1);
            mButtonChoice2 = (RadioButton)findViewById(R.id.choice2);
            mButtonChoice3 = (RadioButton)findViewById(R.id.choice3);
            mButtonChoice4 = (RadioButton)findViewById(R.id.choice4);
            grp=(RadioGroup)findViewById(R.id.grp);
            Next=(Button)findViewById(R.id.next);
            questionNB=(TextView)findViewById(R.id.questionNB);
            updateQuestion();

            //Start of Button Listener for Button1
            mButtonChoice1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //My logic for Button goes in here
                    if(mButtonChoice1.isChecked()){
                    mAnswers.add(1);

             //       mButtonChoice1.setBackground(getDrawable(R.drawable.checked_button_shape));
                   Chosen=true;}
                }
            });

            //End of Button Listener for Button1

            //Start of Button Listener for Button2
            mButtonChoice2.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //My logic for Button goes in here
                    if(mButtonChoice2.isChecked()){
                    mAnswers.add(2);

               //     mButtonChoice2.setBackground(getDrawable(R.drawable.checked_button_shape));
                    Chosen=true;}
                }
            });

            //End of Button Listener for Button2


            //Start of Button Listener for Button3
            mButtonChoice3.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //My logic for Button goes in here
                    if(mButtonChoice3.isChecked()) {
                        mAnswers.add(3);

                        //  mButtonChoice3.setBackground(getDrawable(R.drawable.checked_button_shape));
                        Chosen = true;
                    }
                }
            });
            mButtonChoice4.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //My logic for Button goes in here
                    if(mButtonChoice4.isChecked()){
                    mAnswers.add(4);

               //     mButtonChoice4.setBackground(getDrawable(R.drawable.checked_button_shape));
                    Chosen=true;}

                }
            });
            Next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Chosen){
grp.clearCheck();

                        if(mQuestionNumber == 10)
                            Next.setText(getString(R.string.submit));


                        Chosen=false;
                        if((Next.getText().equals("Submit")||Next.getText().equals("ارسال")))
                            {
                            //start another activity
                                /*
                                Communication com=new Communication(Quiz_Activity.this);

                                String url = IP+"mcq"; // route
                                JSONObject jsonBody = new JSONObject();
                                String answers=mAnswers.toString();

                                try {
                                    jsonBody.put( "answers",answers);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                com.REQUEST_AUTHORIZE(Token,Request.Method.POST, url, jsonBody,new Communication.VolleyCallback() {
                                    @Override
                                    public void onSuccessResponse(JSONObject response) throws JSONException {

                                        }

                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });*/
                                String answers=mAnswers.toString();
                                // write to file to upload to firebase
                                FileOutputStream fOut = null;
                                try {
                                    fOut = new FileOutputStream(answersFile);
                                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                                    myOutWriter.append(answers);

                                    myOutWriter.close();

                                    fOut.flush();
                                    fOut.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                upload_tofirebase();
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

public void upload_tofirebase(){

    if(answersFile.exists())
    {
        filePath= Uri.fromFile(answersFile);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        //   progressDialog.show();
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("text/plain")
                .build();
        StorageReference ref = storageReference.child("questions/"+ UUID.randomUUID().toString());
        ref.putFile(filePath,metadata)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                       answersFile.delete();
                        //
                        //    Toast.makeText(RecorderService.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Quiz_Activity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });
        //    video.delete();
    }
}
}


