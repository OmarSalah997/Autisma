package com.example.autisma;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        getIntent().hasExtra("inst_img");
      String profile_name=  getIntent().getStringExtra("inst_name");
      String img_name=getIntent().getStringExtra("inst_img");
      String description=getIntent().getStringExtra("inst_desc");
      String number=getIntent().getStringExtra("inst_number");
      String address=getIntent().getStringExtra("inst_address");
        TextView name=findViewById(R.id.profile_name);
        name.setText(profile_name);
        ImageView img=findViewById(R.id.profile_img);
        Resources resources =getApplicationContext().getResources();
        int drawableId = resources.getIdentifier(img_name, "drawable", getApplicationContext().getPackageName());
        img.setImageResource(drawableId);

        TextView desc=findViewById(R.id.profile_description);
        desc.setText(description);
        TextView addr=findViewById(R.id.profile_address);
        addr.setText(address);
        TextView nb=findViewById(R.id.profile_number);
        nb.setText(number);


    }
}