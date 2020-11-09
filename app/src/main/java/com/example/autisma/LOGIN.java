package com.example.autisma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class LOGIN extends AppCompatActivity  {
    private String currentLangCode;
    public static String IP="http://192.168.194.126:5002/";
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

        Button signin =findViewById(R.id.sign_in_button);
        final EditText email=findViewById(R.id.username);
        final EditText pass=findViewById(R.id.password);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Communication c = new Communication(LOGIN.this);
                String url = IP+"login"; // route
                String EMAIL=email.getText().toString();
                String password=pass.getText().toString();
                if(EMAIL.isEmpty())
                {
                    Toast.makeText(LOGIN.this, getString(R.string.enterValidEmail), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.isEmpty())
                {
                    Toast.makeText(LOGIN.this, getString(R.string.enterPass), Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject jsonBody = new JSONObject();//Json body data
                try {
                    jsonBody.put( "email","elnagmy45@gmail.com");
                    jsonBody.put( "password","badr1230");
                   // jsonBody.put( "type","1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                c.REQUEST_NO_AUTHORIZE(Request.Method.POST, url, jsonBody,
                        new Communication.VolleyCallback() {
                            @Override
                            public void onSuccessResponse(JSONObject response) throws JSONException {
                                //Toast.makeText(LOGIN.this, "(CharSequence) response",Toast.LENGTH_SHORT ).show();
                                // do your work with response object
                                String token = response.getString("token");
                                SharedPreferences preferences = LOGIN.this.getSharedPreferences("MY_APP",LOGIN.MODE_PRIVATE);
                                preferences.edit().putString("TOKEN",token).apply();

                                try {
                                     getUserName(LOGIN.this);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        });



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
    public void getUserName(Context con) throws InterruptedException {
        Communication com=new Communication(this);
        final String[] name = new String[1];
        final SharedPreferences preferences = getSharedPreferences("MY_APP",Activity.MODE_PRIVATE);
        String Token  = preferences.getString("TOKEN",null);//second parameter default value.
        String url = IP+"profile"; // route
        JSONObject jsonBody = new JSONObject();//Json body data
        com.REQUEST_AUTHORIZE(Token,Request.Method.GET, url, jsonBody,
                new Communication.VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject response) throws JSONException {
                        String name= response.getString("name");;
                        String image = null;
                        preferences.edit().putString("name",name).apply();
                        preferences.edit().putString("img",image).apply();
                        Intent intent= new Intent(LOGIN.this, MainHOME.class);

                        startActivity(intent);

                    }
                });

    }
}