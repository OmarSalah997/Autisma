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
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class LOGIN extends AppCompatActivity  {
    private String currentLangCode;
    public static String IP= "http://6a2784ebb2c8.ngrok.io/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(checkForToken())
        {
            Intent intent= new Intent(LOGIN.this, MainHOME.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }
        loadlocale();
        currentLangCode = getResources().getConfiguration().locale.getLanguage();
        setContentView(R.layout.login);
        TextView ToSignUp=findViewById(R.id.to_sign_up);
        TextView forgot_pass=findViewById(R.id.to_forgot_pass);
        Button signin =findViewById(R.id.sign_in_button);
        final EditText email=findViewById(R.id.username);
        final EditText pass=findViewById(R.id.password);
        final TextView language=findViewById(R.id.change_lang);
        TextView newpass=findViewById(R.id.newpasscode);
        TextView newmail=findViewById(R.id.newmailcode);
        newpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LOGIN.this, ConfirmNewPass.class);
                startActivity(intent);
            }
        });
        newmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LOGIN.this, confirm.class);
                startActivity(intent);
            }
        });

        ToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LOGIN.this, signUP.class);
                startActivity(intent);
            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LOGIN.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Communication c = new Communication(LOGIN.this);
                String url = IP+"login"; // route
                String EMAIL=email.getText().toString();
                String password=pass.getText().toString();
                if(EMAIL.isEmpty())
                {
                    //Toast.makeText(LOGIN.this, getString(R.string.enterValidEmail), Toast.LENGTH_SHORT).show();
                    //email.requestFocus();
                    //return;
                }
                if(password.isEmpty())
                {
                    //pass.requestFocus();
                   // return;
                }
                if(!passCheck(password))
                {
                    //Toast.makeText(LOGIN.this, getString(R.string.passlength), Toast.LENGTH_LONG).show();
                    //pass.requestFocus();
                    // return;
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
                                String token = response.getString("token");
                                SharedPreferences preferences = LOGIN.this.getSharedPreferences("MY_APP",LOGIN.MODE_PRIVATE);
                                preferences.edit().putString("TOKEN",token).apply();

                                try {
                                     getUserName(LOGIN.this);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                int err_Code=error.networkResponse.statusCode;
                                switch (err_Code)
                                {
                                    case 1001:
                                        Toast.makeText(LOGIN.this, getString(R.string.connectionError), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2001:
                                        Toast.makeText(LOGIN.this, getString(R.string.authFail), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2002:
                                        Toast.makeText(LOGIN.this, getString(R.string.email_notConfirmed), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2003:
                                        Toast.makeText(LOGIN.this, getString(R.string.invalidConfirmation), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2004:
                                        Toast.makeText(LOGIN.this, getString(R.string.mailOrPassWrong), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2005:
                                        Toast.makeText(LOGIN.this, getString(R.string.invalidTokan), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2006:
                                        Toast.makeText(LOGIN.this, getString(R.string.emailUsed), Toast.LENGTH_LONG).show();
                                        break;

                                    default:break;
                                }

                            }
                        });



            }
        });

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

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    int err_Code=error.networkResponse.statusCode;
                    switch (err_Code)
                    {
                        case 1001:
                            Toast.makeText(LOGIN.this, getString(R.string.connectionError), Toast.LENGTH_LONG).show();
                            break;
                        case 2001:
                            Toast.makeText(LOGIN.this, getString(R.string.authFail), Toast.LENGTH_LONG).show();
                            break;
                        case 2002:
                            Toast.makeText(LOGIN.this, getString(R.string.email_notConfirmed), Toast.LENGTH_LONG).show();
                            break;
                        case 2003:
                            Toast.makeText(LOGIN.this, getString(R.string.invalidConfirmation), Toast.LENGTH_LONG).show();
                            break;
                        case 2004:
                            Toast.makeText(LOGIN.this, getString(R.string.mailOrPassWrong), Toast.LENGTH_LONG).show();
                            break;
                        case 2005:
                            Toast.makeText(LOGIN.this, getString(R.string.invalidTokan), Toast.LENGTH_LONG).show();
                            break;
                        case 2006:
                            Toast.makeText(LOGIN.this, getString(R.string.emailUsed), Toast.LENGTH_LONG).show();
                            break;

                        default:break;
                    }


                    }
                });

    }

    boolean checkForToken()
    {
        SharedPreferences pref= getSharedPreferences("MY_APP", Activity.MODE_PRIVATE);
        String token=pref.getString("TOKEN",null);
        return token!=null;
    }
    boolean passCheck(String password){
        char element;
        boolean correct = true;
        if (password.length() < 8)
        {
            correct= false;
        }
        if(correct){
        int digit = 0;
        /* Check if the password has 2 or more digits */
        for(int index = 0; index < password.length(); index++ )
        {
            element = password.charAt( index );
            if( Character.isDigit(element) )
            {
                digit++;
            }
        }
        if( digit < 2 ){
            correct= false;
        }}
    return  correct;
    }
}