package com.example.autisma;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
                        else
                            profile_name.setText(editText.getText());
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
                        else
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
}