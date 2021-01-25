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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.autisma.Communication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import static com.example.autisma.LOGIN.IP;

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
                String username=userName.getText().toString();
                final String EMAIL=email.getText().toString();
                String password=Password.getText().toString();
                String Repassword=rewritePass.getText().toString();
        if(username.isEmpty())
        {
            Toast.makeText(signUP.this, getString(R.string.enterValidName), Toast.LENGTH_SHORT).show();
            userName.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            Toast.makeText(signUP.this, getString(R.string.enterPass), Toast.LENGTH_SHORT).show();
            Password.requestFocus();
            return;
        }
        if(Repassword.isEmpty()||!(Repassword.equals(password)))
        {
            Toast.makeText(signUP.this, getString(R.string.reEnterPass), Toast.LENGTH_SHORT).show();
            rewritePass.requestFocus();
            return;
        }
        if(EMAIL.isEmpty())
        {
            Toast.makeText(signUP.this, getString(R.string.enterValidEmail), Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        }
        if( !passCheck(password))
        {
            Toast.makeText(signUP.this, getString(R.string.passlength), Toast.LENGTH_LONG).show();
            Password.requestFocus();
             return;
        }

               ///////////////////
                Communication c = new Communication(signUP.this);
                String url = IP+"signup"; // route
                JSONObject jsonBody = new JSONObject();//Json body data
                try {
                    jsonBody.put( "user_name",username);
                    jsonBody.put( "email",EMAIL);
                    jsonBody.put( "password",password);
                    jsonBody.put( "type",0);
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
                                String result = response.getString("operation");

                                if(result.equals("success"))
                                {
                                    SharedPreferences preferences = signUP.this.getSharedPreferences("confirm",LOGIN.MODE_PRIVATE);
                                    preferences.edit().putString("WaitingForConfirm","true").apply();
                                    preferences.edit().putString("MAIL",EMAIL).apply();
                                    Intent intent= new Intent(signUP.this, confirm.class);
                                    startActivity(intent);
                                }
                               else
                               {
                                   String error = response.getString("error_code");
                                   Toast.makeText(signUP.this, error, Toast.LENGTH_SHORT).show();

                               }

                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                int err_Code=error.networkResponse.statusCode;
                                switch (err_Code)
                                {
                                    case 1001:
                                        Toast.makeText(signUP.this, getString(R.string.connectionError), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2001:
                                        Toast.makeText(signUP.this, getString(R.string.authFail), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2002:
                                        Toast.makeText(signUP.this, getString(R.string.email_notConfirmed), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2003:
                                        Toast.makeText(signUP.this, getString(R.string.invalidConfirmation), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2004:
                                        Toast.makeText(signUP.this, getString(R.string.mailOrPassWrong), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2005:
                                        Toast.makeText(signUP.this, getString(R.string.invalidTokan), Toast.LENGTH_LONG).show();
                                        break;
                                    case 2006:
                                        Toast.makeText(signUP.this, getString(R.string.emailUsed), Toast.LENGTH_LONG).show();
                                        break;

                                    default:break;
                                }
                            }
                        });



               ////////////////

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