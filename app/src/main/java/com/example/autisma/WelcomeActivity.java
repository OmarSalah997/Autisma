package com.example.autisma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {
    Button start;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        start = (Button) findViewById(R.id.start_button);
        name = (EditText) findViewById(R.id.uname);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")){
                    Toast.makeText(WelcomeActivity.this, getString(R.string.emptyname), Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                    return;
                }
                final SharedPreferences preferences = getSharedPreferences("MY_APP", Activity.MODE_PRIVATE);
                preferences.edit().putString("name",name.getText().toString()).apply();


                Intent intent = new Intent(getApplicationContext(), MainHOME.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });




    }
}