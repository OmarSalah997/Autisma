package com.example.autisma;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class PersonalDetails extends AppCompatActivity {
    Spinner spinner ;
    Spinner spinner1 ;
    EditText age;
    CheckBox jaundice;
    CheckBox familyHistory;
    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter ;
    ArrayAdapter<CharSequence> adapter1 ;
    Button finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_details);
         spinner = (Spinner) findViewById(R.id.country_spinner);
         spinner1= (Spinner)findViewById(R.id.ethnicity_spinner);
         finish=(Button)findViewById(R.id.Finish);
        age=(EditText)findViewById(R.id.age);
      jaundice=(CheckBox)findViewById(R.id.checkbox_yes_jaundice);
      familyHistory=(CheckBox)findViewById(R.id.checkbox_yes_family);
        // Create an ArrayAdapter using the string array and a default spinner layout
       adapter = ArrayAdapter.createFromResource(this, R.array.Country_array, android.R.layout.simple_spinner_item);
       adapter1 = ArrayAdapter.createFromResource(this, R.array.ethnicity_array, android.R.layout.simple_spinner_item);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
spinner.setAdapter(adapter);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner1.setAdapter(adapter1);
        finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //My logic for Button goes in here
               startActivity(new Intent(getApplicationContext(),MainHOME.class));
            }
        });

    }

}
