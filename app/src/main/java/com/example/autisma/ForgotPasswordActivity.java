package com.example.autisma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import static com.example.autisma.LOGIN.IP;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadlocale();
        SharedPreferences prefs2= getSharedPreferences("MY_APP", Activity.MODE_PRIVATE);
        final String token=prefs2.getString("TOKEN",null);
        setContentView(R.layout.forgot_password);
        final EditText EMAIL=findViewById(R.id.registered_mail);
        Button send=findViewById(R.id.send_mail);
        ImageView back_arrow=findViewById(R.id.back_from_forgot_pass);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView to_sign_up=findViewById(R.id.to_sign_up3);
        to_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(ForgotPasswordActivity.this, signUP.class);
                startActivity(intent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=EMAIL.getText().toString();
                if(mail.isEmpty())
                {
                    Toast.makeText(ForgotPasswordActivity.this,getString(R.string.enterValidEmail) ,Toast.LENGTH_SHORT ).show();
                    return;
                }
                Communication c = new Communication(ForgotPasswordActivity.this);
                String url = IP+"forgetpass"; // route
                JSONObject JSONObody=new JSONObject();
                try {
                    JSONObody.put("email",mail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                c.REQUEST_AUTHORIZE(token, Request.Method.POST, url,  JSONObody,
                        new Communication.VolleyCallback() {
                            @Override
                            public void onSuccessResponse(JSONObject response) throws JSONException {
                                //Toast.makeText(LOGIN.this, "(CharSequence) response",Toast.LENGTH_SHORT ).show();
                                // do your work with response object
                                String result = response.getString("operation");
                                if(result.equals("success"))
                                {
                                    Toast.makeText(ForgotPasswordActivity.this,getString(R.string.forgotPassMail) ,Toast.LENGTH_LONG ).show();
                                    Intent intent= new Intent(ForgotPasswordActivity.this, ConfirmNewPass.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    ForgotPasswordActivity.this.finish();
                                }


                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                int err_Code=error.networkResponse.statusCode;
                                switch (err_Code)
                                {
                                    case 1001:
                                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.connectionError), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2001:
                                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.authFail), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2002:
                                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.email_notConfirmed), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2003:
                                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.invalidConfirmation), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2004:
                                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.mailOrPassWrong), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2005:
                                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.invalidTokan), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2006:
                                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.emailUsed), Toast.LENGTH_LONG).show();
                                        break;

                                    default:break;
                                }

                            }


                        });


            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();

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
}