package com.example.autisma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.security.cert.PKIXRevocationChecker;

public class Settings extends AppCompatActivity {
    private Dialog Select_lang;
    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        String [] data;
        
        list=findViewById(R.id.settings_list);
        ArrayAdapter<Menu> adapter=new ArrayAdapter<Menu>(this,R.layout.settings_row,R.id.setting_itemText,);
    }
}