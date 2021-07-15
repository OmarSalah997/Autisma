package com.example.autisma;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.autisma.LOGIN.IP;

public class UserProfile extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        SharedPreferences prefs2= getSharedPreferences("MY_APP", Activity.MODE_PRIVATE);
        final String token=prefs2.getString("TOKEN",null);
        final TextView profile_name = findViewById(R.id.profile_name);
        ImageView profile_photo = findViewById(R.id.profile_photo);
        TextView change_username=findViewById(R.id.profile_change_username);
        TextView change_password=findViewById(R.id.profile_change_password);
        TextView change_picture=findViewById(R.id.profile_change_picture);
        /////////////////////////////////////////get image and name from nav header /////////////////////////
        profile_name.setText(getIntent().getStringExtra("USERNAME"));
        Bundle extras = getIntent().getBundleExtra("USERPHOTO");
        assert extras != null;
        Bitmap bmp = (Bitmap) extras.getParcelable("imagebitmap");
        profile_photo.setImageBitmap(bmp);
        ////////////////////////////// setup name changing dialog/////////////////////////////////////
        final EditText editText=new EditText(this);
        final AlertDialog.Builder alertDiaog=new AlertDialog.Builder(this);

        change_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDiaog.setTitle(R.string.change_user_name);
                alertDiaog.setView(editText);
                editText.setHint(R.string.enterNewName);
                alertDiaog.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(editText.getText().toString().isEmpty())
                            Toast.makeText(getApplicationContext(),R.string.blankName,Toast.LENGTH_SHORT).show();
                        else{
                            Communication c = new Communication(UserProfile.this);
                            String url = IP+"changename"; // route
                            JSONObject jsonBody = new JSONObject();//Json body data
                            try {
                                jsonBody.put( "name",editText.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            c.REQUEST_AUTHORIZE(token, Request.Method.POST, url,  jsonBody,
                                    new Communication.VolleyCallback() {
                                        @Override
                                        public void onSuccessResponse(JSONObject response) throws JSONException {
                                            String result = response.getString("operation");
                                            if(result.equals("success"))
                                            {
                                                profile_name.setText(editText.getText().toString());
                                                final SharedPreferences preferences = getSharedPreferences("MY_APP",Activity.MODE_PRIVATE);
                                                preferences.edit().putString("name",editText.getText().toString()).apply();
                                                ((ViewGroup)editText.getParent()).removeView(editText);
                                                editText.getText().clear();
                                            }

                                        }

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            ((ViewGroup)editText.getParent()).removeView(editText);
                                            editText.getText().clear();
                                            int err_Code=error.networkResponse.statusCode;
                                            switch (err_Code)
                                            {
                                                case 1001:
                                                    Toast.makeText(UserProfile.this, getString(R.string.connectionError), Toast.LENGTH_LONG).show();
                                                    break;
                                                case 2001:
                                                    Toast.makeText(UserProfile.this, getString(R.string.authFail), Toast.LENGTH_LONG).show();
                                                    break;
                                                case 2002:
                                                    Toast.makeText(UserProfile.this, getString(R.string.email_notConfirmed), Toast.LENGTH_LONG).show();
                                                    break;
                                                case 2003:
                                                    Toast.makeText(UserProfile.this, getString(R.string.invalidConfirmation), Toast.LENGTH_LONG).show();
                                                    break;
                                                case 2004:
                                                    Toast.makeText(UserProfile.this, getString(R.string.mailOrPassWrong), Toast.LENGTH_LONG).show();
                                                    break;
                                                case 2005:
                                                    Toast.makeText(UserProfile.this, getString(R.string.invalidTokan), Toast.LENGTH_LONG).show();
                                                    break;
                                                case 2006:
                                                    Toast.makeText(UserProfile.this, getString(R.string.emailUsed), Toast.LENGTH_LONG).show();
                                                    break;

                                                default:break;
                                            }
                                        }
                                    });

                        }


                    }
                });
                alertDiaog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((ViewGroup)editText.getParent()).removeView(editText);
                        editText.getText().clear();
                    }
                });
                alertDiaog.show();
            }
        });
        /////////////////////////////////// setup password change dialog///////////////////////////////////////////////
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDiaog.setTitle(R.string.change_password);
                alertDiaog.setView(editText);
                editText.setHint(R.string.enterNewPass);
                alertDiaog.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(editText.getText().toString().isEmpty())
                            Toast.makeText(getApplicationContext(),R.string.blankPass,Toast.LENGTH_SHORT).show();
                        else{
                            Communication c = new Communication(UserProfile.this);
                            String url = IP+"changepass"; // route
                            JSONObject jsonBody = new JSONObject();//Json body data
                            try {
                                jsonBody.put( "password",editText.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            c.REQUEST_AUTHORIZE(token, Request.Method.POST, url,  jsonBody,
                                    new Communication.VolleyCallback() {
                                        @Override
                                        public void onSuccessResponse(JSONObject response) throws JSONException {
                                            String result = response.getString("operation");
                                            if(result.equals("success"))
                                            {
                                                Toast.makeText(UserProfile.this, "sucess", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            int err_Code=error.networkResponse.statusCode;
                                            switch (err_Code)
                                            {
                                                case 1001:
                                                    Toast.makeText(UserProfile.this, getString(R.string.connectionError), Toast.LENGTH_LONG).show();
                                                    break;
                                                case 2001:
                                                    Toast.makeText(UserProfile.this, getString(R.string.authFail), Toast.LENGTH_LONG).show();
                                                    break;
                                                case 2002:
                                                    Toast.makeText(UserProfile.this, getString(R.string.email_notConfirmed), Toast.LENGTH_LONG).show();
                                                    break;
                                                case 2003:
                                                    Toast.makeText(UserProfile.this, getString(R.string.invalidConfirmation), Toast.LENGTH_LONG).show();
                                                    break;
                                                case 2004:
                                                    Toast.makeText(UserProfile.this, getString(R.string.mailOrPassWrong), Toast.LENGTH_LONG).show();
                                                    break;
                                                case 2005:
                                                    Toast.makeText(UserProfile.this, getString(R.string.invalidTokan), Toast.LENGTH_LONG).show();
                                                    break;
                                                case 2006:
                                                    Toast.makeText(UserProfile.this, getString(R.string.emailUsed), Toast.LENGTH_LONG).show();
                                                    break;

                                                default:break;
                                            }
                                        }
                                    });

                        }
                            //here we call the request
                        ((ViewGroup)editText.getParent()).removeView(editText);
                        editText.getText().clear();
                    }
                });
                alertDiaog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((ViewGroup)editText.getParent()).removeView(editText);
                        editText.getText().clear();
                    }
                });
                alertDiaog.show();
            }
        });
        //////////////////////////////////// setup change pic
        change_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
                        Intent intent= new Intent(UserProfile.this, MainHOME.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int err_Code=error.networkResponse.statusCode;
                        switch (err_Code)
                        {
                            case 1001:
                                Toast.makeText(UserProfile.this, getString(R.string.connectionError), Toast.LENGTH_LONG).show();
                                break;
                            case 2001:
                                Toast.makeText(UserProfile.this, getString(R.string.authFail), Toast.LENGTH_LONG).show();
                                break;
                            case 2002:
                                Toast.makeText(UserProfile.this, getString(R.string.email_notConfirmed), Toast.LENGTH_LONG).show();
                                break;
                            case 2003:
                                Toast.makeText(UserProfile.this, getString(R.string.invalidConfirmation), Toast.LENGTH_LONG).show();
                                break;
                            case 2004:
                                Toast.makeText(UserProfile.this, getString(R.string.mailOrPassWrong), Toast.LENGTH_LONG).show();
                                break;
                            case 2005:
                                Toast.makeText(UserProfile.this, getString(R.string.invalidTokan), Toast.LENGTH_LONG).show();
                                break;
                            case 2006:
                                Toast.makeText(UserProfile.this, getString(R.string.emailUsed), Toast.LENGTH_LONG).show();
                                break;

                            default:break;
                        }


                    }
                });

    }
}