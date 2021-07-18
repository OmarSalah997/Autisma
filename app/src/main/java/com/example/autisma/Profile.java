package com.example.autisma;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        getIntent().hasExtra("inst_img");
        String profile_name = getIntent().getStringExtra("inst_name");
        String img_name = getIntent().getStringExtra("inst_img");
        String description = getIntent().getStringExtra("inst_desc");
        String number = getIntent().getStringExtra("inst_number");
        String address = getIntent().getStringExtra("inst_address");
        final String fbPage = getIntent().getStringExtra("inst_fbPage");
        final String webPage = getIntent().getStringExtra("inst_webpage");
        TextView name = findViewById(R.id.profile_name);
        name.setText(profile_name);
        ImageView img = findViewById(R.id.profile_img);
        Resources resources = getApplicationContext().getResources();
        int drawableId = resources.getIdentifier(img_name, "drawable", getApplicationContext().getPackageName());
        img.setImageResource(drawableId);

        TextView desc = findViewById(R.id.profile_description);
        desc.setText(description);
        TextView addr = findViewById(R.id.profile_address);
        addr.setText(address);
        TextView nb = findViewById(R.id.profile_number);
        nb.setText(number);
        TextView fbpage = findViewById(R.id.facebookLink);

        if (fbPage != null) {

            fbpage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse(fbPage));
                    startActivity(viewIntent);
                }
            });
        } else {
            LinearLayout fblayout = findViewById(R.id.fb_layout);
            fblayout.setVisibility(View.INVISIBLE);
        }
        TextView webpage = findViewById(R.id.websiteLink);
        if (webPage != null) {
            webpage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse(webPage));
                    startActivity(viewIntent);
                }
            });

        } else {
            LinearLayout weblayout = findViewById(R.id.web_layout);
            weblayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Profile.this,Helpful_institiutions.class);
        startActivity(intent);
        finish();
    }
}