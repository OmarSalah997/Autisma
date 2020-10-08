package com.example.autisma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.Locale;

public class LOGIN extends AppCompatActivity  {
    private String currentLangCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadlocale();
        currentLangCode = getResources().getConfiguration().locale.getLanguage();
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
        Button signInWithGoogle=findViewById(R.id.sign_with_google);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        final TextView language=findViewById(R.id.change_lang);
        SharedPreferences pref= getSharedPreferences("settings_lang", Activity.MODE_PRIVATE);
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
        SharedPreferences.Editor editor= getSharedPreferences("settings_lang",MODE_PRIVATE).edit();
        editor.putString("my lang",s);
        editor.apply();
    }

    private void loadlocale() {
        SharedPreferences prefs= getSharedPreferences("settings_lang", Activity.MODE_PRIVATE);
        String language=prefs.getString("my lang","");
        if(language.equals(""))
            language=Resources.getSystem().getConfiguration().locale.getLanguage();
        setLocale(language);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!currentLangCode.equals(getResources().getConfiguration().locale.getLanguage())) {
            currentLangCode = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }
    }
}