package com.example.autisma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class PersonalDetails extends AppCompatActivity {
    Spinner spinner ;
    Spinner spinner1 ;
    EditText age;
    CheckBox jaundice;
    CheckBox familyHistory;
    CheckBox male ;
    CheckBox female;
    ArrayList<String> mAnswers = new ArrayList<String>();
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;
    private File answersFile ;
    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter ;
    ArrayAdapter<CharSequence> adapter1 ;
    Button finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_details);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        File answersFolder = new File(
                Environment.getExternalStorageDirectory(), "autisma_details");
        if (!answersFolder.exists())
            answersFolder.mkdirs(); // <----

        answersFile = new File(answersFolder, System.currentTimeMillis()+".txt");  //file name + extension is .txt
       //  spinner = (Spinner) findViewById(R.id.country_spinner);
         spinner1= (Spinner)findViewById(R.id.ethnicity_spinner);
         finish=(Button)findViewById(R.id.Finish);
        age=(EditText)findViewById(R.id.age);
      jaundice=(CheckBox)findViewById(R.id.checkbox_yes_jaundice);
      familyHistory=(CheckBox)findViewById(R.id.checkbox_yes_family);
      male=(CheckBox)findViewById(R.id.checkbox_male) ;
      female=(CheckBox)findViewById(R.id.checkbox_female) ;
        // Create an ArrayAdapter using the string array and a default spinner layout
    //   adapter = ArrayAdapter.createFromResource(this, R.array.Country_array, android.R.layout.simple_spinner_item);
       adapter1 = ArrayAdapter.createFromResource(this, R.array.ethnicity_array, android.R.layout.simple_spinner_item);
     //  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//spinner.setAdapter(adapter);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter1);
        finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //My logic for Button goes in here
                mAnswers.add(String.valueOf(age.getText()));
                if(male.isChecked())
                mAnswers.add("male");
                else
                    mAnswers.add("female");

                mAnswers.add(spinner1.getSelectedItem().toString());
                if(jaundice.isChecked())
                mAnswers.add("jaundice");
                else
                    mAnswers.add("no_jaundice");
                if(familyHistory.isChecked())
                    mAnswers.add("history");
                else
                    mAnswers.add("no_history");
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
               startActivity(new Intent(getApplicationContext(),MainHOME.class));
            }
        });

    }
    private void upload_tofirebase(){
      if(answersFile.exists())
    {
        filePath= Uri.fromFile(answersFile);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        //   progressDialog.show();
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("text/plain")
                .build();
        StorageReference ref = storageReference.child("details/"+ UUID.randomUUID().toString());
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
                        Toast.makeText(PersonalDetails.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
