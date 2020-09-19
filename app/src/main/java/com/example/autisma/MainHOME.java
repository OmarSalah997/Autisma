package com.example.autisma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.material.navigation.NavigationView;

public class MainHOME extends AppCompatActivity {
    private DrawerLayout Drawer_layout;
    private ActionBarDrawerToggle ToggleButton;
    private ActionBar actionBar;
    private NavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        Drawer_layout=findViewById(R.id.drawer);
        ToggleButton = new ActionBarDrawerToggle(this,Drawer_layout,R.string.open,R.string.close);
        Drawer_layout.addDrawerListener(ToggleButton);
        ToggleButton.syncState();
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar=getSupportActionBar();
        if(actionBar!=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        navigation=findViewById(R.id.NavigationView);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                Drawer_layout.closeDrawers();
                switch (id){
                    case R.id.myalarms:
                        Intent alarms = new Intent(getApplicationContext(), Alarms.class);
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
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(ToggleButton.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}