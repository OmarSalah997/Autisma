package com.example.autisma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.example.autisma.Alarm_module.Alarms_main;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import static com.example.autisma.LOGIN.IP;

public class MainHOME extends AppCompatActivity {
    private DrawerLayout Drawer_layout;
    private ActionBarDrawerToggle ToggleButton;
    private NavigationView navigation;
    private Button Alarms ,child_tests,Games,helpfulInst,doctors;
    private String currentLangCode;
    private TextView userNameTextview,welcomeMessage;
    private ImageView userPhotoImageview;
    private String username;
    private String user_photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        Communication com=new Communication(this);
        SharedPreferences prefs= getSharedPreferences("settings_lang", Activity.MODE_PRIVATE);
        SharedPreferences prefs2= getSharedPreferences("MY_APP", Activity.MODE_PRIVATE);
        currentLangCode=prefs.getString("my lang","");
        welcomeMessage=findViewById(R.id.welcomeText);
        welcomeMessage.append(" "+prefs2.getString("name","")+",");
        Drawer_layout=findViewById(R.id.drawer);
        Alarms=findViewById(R.id.button_alarms);
        child_tests=findViewById(R.id.button_Child_tests);
        Games=findViewById(R.id.button_Games);
        helpfulInst =findViewById(R.id.button_instetutions);
        doctors=findViewById(R.id.button_doctors);
        ToggleButton = new ActionBarDrawerToggle(this,Drawer_layout,R.string.open,R.string.close);
        Drawer_layout.addDrawerListener(ToggleButton);
        ToggleButton.syncState();
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        navigation=findViewById(R.id.NavigationView);

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                Drawer_layout.closeDrawers();
                switch (id){
                    case R.id.myalarms:
                        Intent alarms = new Intent(getApplicationContext(), Alarms_main.class);
                        startActivity(alarms);
                       break;
                    case R.id.settings:
                        Intent settings = new Intent(getApplicationContext(), Settings.class);
                        startActivity(settings);                        break;
                    case R.id.loguot:
                        //add navigation drawer item onclick method here
                        break;
                }
                return false;
            }
        });
        View headerView = navigation.getHeaderView(0);
        userNameTextview=(TextView)headerView.findViewById(R.id.user_name);
        userNameTextview.setText(prefs2.getString("name",""));
        username=userNameTextview.getText().toString();
        userPhotoImageview=headerView.findViewById(R.id.user_photo);
        user_photo=userPhotoImageview.getDrawable().toString();
        Alarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Alarms_main.class));
            }
        });

        child_tests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),child_tests.class));
            }
        });
        Games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Games.class));
            }
        });
        helpfulInst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Helpful_institiutions.class));
            }
        });
        doctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Doctors.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(ToggleButton.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!currentLangCode.equals(getResources().getConfiguration().locale.getLanguage())) {
            currentLangCode = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }
    }
public void ToUserProfile(View v){
        Intent intent=new Intent(getApplicationContext(), UserProfile.class);
        intent.putExtra("USERNAME", username);
    userPhotoImageview.buildDrawingCache();
    Bitmap image= userPhotoImageview.getDrawingCache();
    Bundle extras = new Bundle();
    extras.putParcelable("imagebitmap", image);
        intent.putExtra("USERPHOTO", extras);
        startActivity(intent);
}

public String getUserName(Context con){
    Communication com=new Communication(this);
    final String[] name = new String[1];
    SharedPreferences preferences = getSharedPreferences("MY_APP",Activity.MODE_PRIVATE);
    String Token  = preferences.getString("TOKEN",null);//second parameter default value.
    String url = IP+"profile"; // route
    JSONObject jsonBody = new JSONObject();//Json body data
    com.REQUEST_AUTHORIZE(Token,Request.Method.GET, url, jsonBody,
            new Communication.VolleyCallback() {
                @Override
                public void onSuccessResponse(JSONObject response) throws JSONException {

                     name[0] = response.getString("name");

                }
            });
    return  name[0];
}
}