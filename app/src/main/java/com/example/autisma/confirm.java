package com.example.autisma;

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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.autisma.LOGIN.IP;

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
                String code=cofrimCode.getText().toString();
                Communication c = new Communication(confirm.this);
                String url = IP+"confirm"; // route
                if(code.isEmpty())
                {
                    Toast.makeText(confirm.this, getString(R.string.enterConfirmCode), Toast.LENGTH_SHORT).show();
                    cofrimCode.requestFocus();
                    return;
                }
                ///////////////////////
                SharedPreferences pref= getSharedPreferences("confirm", Activity.MODE_PRIVATE);
                String mail=pref.getString("MAIL","");
                JSONObject jsonBody = new JSONObject();//Json body data
                try {
                    jsonBody.put( "code",code);
                    jsonBody.put( "email",mail);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                c.REQUEST_NO_AUTHORIZE(Request.Method.POST, url, jsonBody,
                        new Communication.VolleyCallback() {
                            @Override
                            public void onSuccessResponse(JSONObject response) throws JSONException {
                                //Toast.makeText(LOGIN.this, "(CharSequence) response",Toast.LENGTH_SHORT ).show();
                                // do your work with response object
                                String result = response.getString("operation");
                                if(result.equals("success"))
                                {
                                    SharedPreferences preferences = confirm.this.getSharedPreferences("confirm",confirm.MODE_PRIVATE);
                                    String token = response.getString("token");
                                    SharedPreferences preferences2 = confirm.this.getSharedPreferences("MY_APP",confirm.MODE_PRIVATE);
                                    preferences.edit().putString("TOKEN",token).apply();

                                    try {
                                        getUserName(confirm.this);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Intent intent= new Intent(confirm.this, MainHOME.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    confirm.this.finish();
                                }
                                else
                                {
                                    Toast.makeText(confirm.this, "something is wrong,i can feel it", Toast.LENGTH_SHORT).show();

                                }

                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                int err_Code=error.networkResponse.statusCode;
                                switch (err_Code)
                                {
                                    case 1001:
                                        Toast.makeText(confirm.this, getString(R.string.connectionError), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2001:
                                        Toast.makeText(confirm.this, getString(R.string.authFail), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2002:
                                        Toast.makeText(confirm.this, getString(R.string.email_notConfirmed), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2003:
                                        Toast.makeText(confirm.this, getString(R.string.invalidConfirmation), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2004:
                                        Toast.makeText(confirm.this, getString(R.string.mailOrPassWrong), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2005:
                                        Toast.makeText(confirm.this, getString(R.string.invalidTokan), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2006:
                                        Toast.makeText(confirm.this, getString(R.string.emailUsed), Toast.LENGTH_LONG).show();
                                        break;

                                    default:break;
                                }
                            }
                        });



                ///////////////////////




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
                        Intent intent= new Intent(confirm.this, MainHOME.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

    }
}