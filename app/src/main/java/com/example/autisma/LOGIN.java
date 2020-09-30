package com.example.autisma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class LOGIN extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadlocale();
        setContentView(R.layout.login);
        TextView ToSignUp=findViewById(R.id.to_sign_up);
        ToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LOGIN.this, signUP.class);
                startActivity(intent);
            }
        });
        TextView forgot_pass=findViewById(R.id.to_forgot_pass);
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LOGIN.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        Button signup=findViewById(R.id.sign_in_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent= new Intent(LOGIN.this, MainHOME.class);
                startActivity(intent);

            }
        });
        final TextView language=findViewById(R.id.change_lang);
        SharedPreferences pref= getSharedPreferences("settings", Activity.MODE_PRIVATE);
        String lang=pref.getString("my lang","");
        language.setText(lang);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(language.getText().toString().equals("en"))
                {
                    setLocale("ar");
                    recreate();
                    language.setText("ar");
                }
                else{
                    setLocale("en");
                    language.setText("en");
                    recreate();
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
       /* String s=Locale.getDefault().getDisplayLanguage();
        if(s.equals("العربية"))
            setLocale("ar");
        else
            setLocale("en");*/
        SharedPreferences prefs= getSharedPreferences("settings", Activity.MODE_PRIVATE);
        String language=prefs.getString("my lang","");
        setLocale(language);
    }
}