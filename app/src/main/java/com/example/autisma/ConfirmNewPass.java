package com.example.autisma;

import android.app.Activity;
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

public class ConfirmNewPass extends AppCompatActivity {
    private String currentLangCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_pass);
        loadlocale();
        currentLangCode = getResources().getConfiguration().locale.getLanguage();
        final EditText code=findViewById(R.id.Code);
        final EditText mail=findViewById(R.id.email);
        final EditText newpass=findViewById(R.id.password);
        Button done=findViewById(R.id.sign_in_button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_code=code.getText().toString();
                String str_mail=mail.getText().toString();
                String str_pass=newpass.getText().toString();
                if(str_pass.isEmpty())
                {
                    Toast.makeText(ConfirmNewPass.this, getString(R.string.enterPass), Toast.LENGTH_SHORT).show();
                    newpass.requestFocus();
                    return;
                }
                if(!passCheck(str_pass))
                {
                    Toast.makeText(ConfirmNewPass.this, getString(R.string.passlength), Toast.LENGTH_SHORT).show();
                    newpass.requestFocus();
                    return;
                }
                if(str_mail.isEmpty())
                {
                    Toast.makeText(ConfirmNewPass.this, getString(R.string.enterValidEmail), Toast.LENGTH_SHORT).show();
                    mail.requestFocus();
                    return;
                }
                if(str_code.isEmpty())
                {
                    Toast.makeText(ConfirmNewPass.this, getString(R.string.enterConfirmCode), Toast.LENGTH_SHORT).show();
                    code.requestFocus();
                    return;
                }

                Communication c = new Communication(ConfirmNewPass.this);
                String url = IP+"setnewpass"; // route
                JSONObject jsonBody = new JSONObject();//Json body data
                try {
                    jsonBody.put( "code",str_code);
                    jsonBody.put( "email",str_mail);
                    jsonBody.put( "password",str_pass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                c.REQUEST_NO_AUTHORIZE(Request.Method.POST, url, jsonBody,
                        new Communication.VolleyCallback() {
                            @Override
                            public void onSuccessResponse(JSONObject response) throws JSONException {

                                Toast.makeText(ConfirmNewPass.this, getString(R.string.success), Toast.LENGTH_LONG).show();
                                Intent intent= new Intent(ConfirmNewPass.this, LOGIN.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                ConfirmNewPass.this.finish();

                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                int err_Code=error.networkResponse.statusCode;
                                switch (err_Code)
                                {
                                    case 1001:
                                        Toast.makeText(ConfirmNewPass.this, getString(R.string.connectionError), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2001:
                                        Toast.makeText(ConfirmNewPass.this, getString(R.string.authFail), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2002:
                                        Toast.makeText(ConfirmNewPass.this, getString(R.string.email_notConfirmed), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2003:
                                        Toast.makeText(ConfirmNewPass.this, getString(R.string.invalidConfirmation), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2004:
                                        Toast.makeText(ConfirmNewPass.this, getString(R.string.mailOrPassWrong), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2005:
                                        Toast.makeText(ConfirmNewPass.this, getString(R.string.invalidTokan), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2006:
                                        Toast.makeText(ConfirmNewPass.this, getString(R.string.emailUsed), Toast.LENGTH_LONG).show();
                                        break;

                                    default:break;
                                }

                            }
                        });

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
            language= Resources.getSystem().getConfiguration().locale.getLanguage();
        setLocale(language);
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