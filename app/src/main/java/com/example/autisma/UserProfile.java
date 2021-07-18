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
                        else{profile_name.setText(editText.getText().toString());
                            final SharedPreferences preferences = getSharedPreferences("MY_APP",Activity.MODE_PRIVATE);
                            preferences.edit().putString("name",editText.getText().toString()).apply();
                            ((ViewGroup)editText.getParent()).removeView(editText);
                            editText.getText().clear();
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
    }
}