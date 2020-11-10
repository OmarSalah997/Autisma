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
import android.widget.Toast;

import java.util.Locale;

public class confirm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadlocale();
        setContentView(R.layout.activity_confirm);
        final EditText cofrimCode=findViewById(R.id.confirm_edittext);
        Button sendConfirm=findViewById(R.id.button_confirmMail);
        sendConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cofrimCode.getText().toString().isEmpty())
                {
                    Toast.makeText(confirm.this, getString(R.string.enterConfirmCode), Toast.LENGTH_SHORT).show();
                    cofrimCode.requestFocus();
                    return;
                }

            }
        });
    }
    private void loadlocale() {
        SharedPreferences prefs= getSharedPreferences("settings_lang", Activity.MODE_PRIVATE);
        String language=prefs.getString("my lang","");
        if(language.equals(""))
            language= Resources.getSystem().getConfiguration().locale.getLanguage();
        setLocale(language);
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
}