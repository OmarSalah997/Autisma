package com.example.autisma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autisma.Communication;
import java.util.Locale;

public class signUP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadlocale();
        setContentView(R.layout.activity_sign_up);
        TextView ToSignIn=findViewById(R.id.to_sign_in);
        Button signUp=findViewById(R.id.sign_up_button);
        final EditText userName=findViewById(R.id.username_signup);
        final EditText Password=findViewById(R.id.password_signup);
        final EditText rewritePass=findViewById(R.id.reEnterpassword_signup2);
        final EditText email=findViewById(R.id.email_signup);

        ToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        if(userName.getText().toString().isEmpty())
        {
            Toast.makeText(signUP.this, getString(R.string.enterValidName), Toast.LENGTH_SHORT).show();
            userName.requestFocus();
            return;
        }
        if(Password.getText().toString().isEmpty())
        {
            Toast.makeText(signUP.this, getString(R.string.enterPass), Toast.LENGTH_SHORT).show();
            Password.requestFocus();
            return;
        }
        if(rewritePass.getText().toString().isEmpty())
        {
            Toast.makeText(signUP.this, getString(R.string.reEnterPass), Toast.LENGTH_SHORT).show();
            rewritePass.requestFocus();
            return;
        }
        if(email.getText().toString().isEmpty())
        {
            Toast.makeText(signUP.this, getString(R.string.enterValidEmail), Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        }

            }
        });
    }



    private void setLocale(String s) {
        Locale locale= new Locale(s);
        Locale.setDefault(locale);
        Configuration config=new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor= getSharedPreferences("settings",MODE_PRIVATE).edit();
        editor.putString("my lang",s);
        editor.apply();
    }
    private void loadlocale() {
        SharedPreferences prefs= getSharedPreferences("settings_lang", Activity.MODE_PRIVATE);
        String language=prefs.getString("my lang","");
        if(language.equals(""))
            language= Resources.getSystem().getConfiguration().locale.getLanguage();
        setLocale(language);
    }
}