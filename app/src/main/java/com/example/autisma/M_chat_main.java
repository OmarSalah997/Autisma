package com.example.autisma;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class M_chat_main extends AppCompatActivity {
    int Score=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadlocale();
        setContentView(R.layout.activity_m_chat_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        final ArrayList<RadioButton> listOfRadioButtons = new ArrayList<RadioButton>();
        final RadioButton y1,y2,y3,y4,y5,y6,y7,y8,y9,y10,y11,y12,
                y13,y14,y15,y16,y17,y18,y19,y20,
                    n1,n2,n3,n4,n5,n6,n7,
                    n8,n9,n10,n11,n12,n13,
                    n14,n15,n16,n17,n18,n19,n20;
        y1=findViewById(R.id.Mchat_yes1);
        y2=findViewById(R.id.Mchat_yes2);
        y3=findViewById(R.id.Mchat_yes3);
        y4=findViewById(R.id.Mchat_yes4);
        y5=findViewById(R.id.Mchat_yes5);
        y6=findViewById(R.id.Mchat_yes6);
        y7=findViewById(R.id.Mchat_yes7);
        y8=findViewById(R.id.Mchat_yes8);
        y9=findViewById(R.id.Mchat_yes9);
        y10=findViewById(R.id.Mchat_yes10);
        y11=findViewById(R.id.Mchat_yes11);
        y12=findViewById(R.id.Mchat_yes12);
        y13=findViewById(R.id.Mchat_yes13);
        y14=findViewById(R.id.Mchat_yes14);
        y15=findViewById(R.id.Mchat_yes15);
        y16=findViewById(R.id.Mchat_yes16);
        y17=findViewById(R.id.Mchat_yes17);
        y18=findViewById(R.id.Mchat_yes18);
        y19=findViewById(R.id.Mchat_yes19);
        y20=findViewById(R.id.Mchat_yes20);
        n1=findViewById(R.id.Mchat_no1);
        n2=findViewById(R.id.Mchat_no2);
        n3=findViewById(R.id.Mchat_no3);
        n4=findViewById(R.id.Mchat_no4);
        n5=findViewById(R.id.Mchat_no5);
        n6=findViewById(R.id.Mchat_no6);
        n7=findViewById(R.id.Mchat_no7);
        n8=findViewById(R.id.Mchat_no8);
        n9=findViewById(R.id.Mchat_no9);
        n10=findViewById(R.id.Mchat_no10);
        n11=findViewById(R.id.Mchat_no11);
        n12=findViewById(R.id.Mchat_no12);
        n13=findViewById(R.id.Mchat_no13);
        n14=findViewById(R.id.Mchat_no14);
        n15=findViewById(R.id.Mchat_no15);
        n16=findViewById(R.id.Mchat_no16);
        n17=findViewById(R.id.Mchat_no17);
        n18=findViewById(R.id.Mchat_no18);
        n19=findViewById(R.id.Mchat_no19);
        n20=findViewById(R.id.Mchat_no20);
        listOfRadioButtons.add(y1);
        listOfRadioButtons.add(n1);
        listOfRadioButtons.add(y2);
        listOfRadioButtons.add(n2);
        listOfRadioButtons.add(y3);
        listOfRadioButtons.add(n3);
        listOfRadioButtons.add(y4);
        listOfRadioButtons.add(n4);
        listOfRadioButtons.add(y5);
        listOfRadioButtons.add(n5);
        listOfRadioButtons.add(y6);
        listOfRadioButtons.add(n6);
        listOfRadioButtons.add(y7);
        listOfRadioButtons.add(n7);
        listOfRadioButtons.add(y8);
        listOfRadioButtons.add(n8);
        listOfRadioButtons.add(y9);
        listOfRadioButtons.add(n9);
        listOfRadioButtons.add(y10);
        listOfRadioButtons.add(n10);
        listOfRadioButtons.add(y11);
        listOfRadioButtons.add(n11);
        listOfRadioButtons.add(y12);
        listOfRadioButtons.add(n12);
        listOfRadioButtons.add(y13);
        listOfRadioButtons.add(n13);
        listOfRadioButtons.add(y14);
        listOfRadioButtons.add(n14);
        listOfRadioButtons.add(y15);
        listOfRadioButtons.add(n15);
        listOfRadioButtons.add(y16);
        listOfRadioButtons.add(n16);
        listOfRadioButtons.add(y17);
        listOfRadioButtons.add(n17);
        listOfRadioButtons.add(y18);
        listOfRadioButtons.add(n18);
        listOfRadioButtons.add(y19);
        listOfRadioButtons.add(n19);
        listOfRadioButtons.add(y20);
        listOfRadioButtons.add(n20);
        Button submit=findViewById(R.id.submit_mchat);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            int dummy=0;
            for(int i=1;i<40;i+=2){
               int j=i-1;
               if(listOfRadioButtons.get(j).isChecked()||
                       listOfRadioButtons.get(i).isChecked())
                   dummy++;
            }
            if(dummy!=20)
                Toast.makeText(M_chat_main.this, getString(R.string.ansrall), Toast.LENGTH_SHORT).show();
            else
            {
                for(int i=0;i<40;i++)
                    onRadioButtonClicked(listOfRadioButtons.get(i));
                Intent intent = new Intent(getBaseContext(), M_chat_score.class);
                intent.putExtra("score",Score);
                startActivity(intent);
                finish();
            }
            }
        });



    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.Mchat_no1:
            case R.id.Mchat_no3:
            case R.id.Mchat_no4:
            case R.id.Mchat_no6:
            case R.id.Mchat_no7:
            case R.id.Mchat_no8:
            case R.id.Mchat_no9:
            case R.id.Mchat_no10:
            case R.id.Mchat_no11:
            case R.id.Mchat_no13:
            case R.id.Mchat_no14:
            case R.id.Mchat_no15:
            case R.id.Mchat_no16:
            case R.id.Mchat_no17:
            case R.id.Mchat_no18:
            case R.id.Mchat_no19:
            case R.id.Mchat_no20:
            case R.id.Mchat_yes2:
            case R.id.Mchat_yes5:
            case R.id.Mchat_yes12:
                if (checked)
                    Score++;
                    break;
        }
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
            language= Resources.getSystem().getConfiguration().locale.getLanguage();
        setLocale(language);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder warning = new AlertDialog.Builder(this);
        warning.setIcon(R.drawable.wrong_sign);
        warning.setTitle(R.string.warning);
        warning.setMessage(R.string.resLost);
        warning.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(getBaseContext(), child_tests.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        warning.setNegativeButton(getString(R.string.cancel),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        warning.show();
    }
}