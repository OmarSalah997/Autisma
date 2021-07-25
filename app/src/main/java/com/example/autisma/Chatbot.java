package com.example.autisma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Chatbot extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ImageView imageView = (ImageView)findViewById(R.id.chatbotIV);
        TextView textView = (TextView)findViewById(R.id.chatbotTV);
        EditText editText = (EditText)findViewById(R.id.questionET);
        Button button = (Button)findViewById(R.id.chatbotBTN);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                String[] echoTags = {"المصاداة", "مصاداة" , "صدي" , "التكرار" , "تكرار" , "يكرر" , "تكرر" , "كرر" , "يعيد" , "إعادة" , "أعادة" , "اعادة" , "تعيد"};
                String[] therapyTags = {"حل", "الحل", "حلول", "الحلول", "علاج", "العلاج"};
                String[] geneticTags = {"وراثي", "الوراثي", "وراثة", "جين", "الجين", "جيني", "الجيني", "الوراثة", "جينات", "الجينات" };
                String[] sexualDesireTags = {"جنسية", "جنس", "بلوغ", "البلوغ"};
                String[] entertainmentTags = {"تسلية", "التسلية", "أسلي", "يسلي", "تسلي", "ترفيه", "الترفيه", "أرفه", "ارفه", "ترفه"};
                String[] pronounsTags = {"ضمير", "الضمير", "ضمائر", "الضمائر"};
                String[] isolationTags = {"انعزال", "الانعزال", "عزلة", "العزلة", "منعزل", "ينعزل", "تنعزل"};
                String[] screamingTags = {"صراخ", "الصراخ", "الصريخ", "صريخ", "يصرخ", "تصرخ", "الصياح", "صياح", "يصيح", "تصيح", "عدم النوم", "لا ينام", "لا تنام", "قلة النوم"};
                String[] tantrumsTags = {"غضب", "غضبان", "يغضب", "الغضب", "تغضب"};
                String[] sabotageTags = {"تخريب", "التخريب", "تكسير", "التكسير", "يخرب", "تخرب", "يكسر", "تكسر"};
                String[] fearTags = {"خوف", "الخوف", "يخاف", "تخاف", "يرهب", "ترهب", "رهبة", "الرهبة"};
                String[] selfHarmTags = {"تضرب", "يضرب", "تجرح", "يجرح", "تؤذي", "يؤذي", "الايذاء", "ايذاء", "الأيذاء", "أيذاء", "الإيذاء", "إيذاء"};
                String[] nutritionTags = {"الطعام", "طعام", "تاكل", "ياكل", "الاكل", "اكل", "تأكل", "يأكل", "الأكل", "أكل", "تتغذي", "يتغذي", "الغذاء", "غذاء", "التغذية", "تغذية"};
                String[] adaptionTags = {"تتكيف", "يتكيف", "تكيف", "التكيف", "تحفظية", "تحفظي", "متحفظة", "متحفظ", "التحفظ", "تحفظ", "التغيير", "تغيير"};
                boolean sucess = false;
                if((str.contains("التوحد") == false)&&(str.contains("التوحد") == false))
                {
                    sucess = false;
                }
                else
                {
                    for (String tag : echoTags) {
                        if (str.contains(tag)) {
                            sucess = true;
                            startActivity(new Intent(Chatbot.this, Echolalia.class));
                        }
                    }
                    for (String tag : therapyTags) {
                        if (str.contains(tag)) {
                            sucess = true;
                            startActivity(new Intent(Chatbot.this, Therapy.class));
                        }
                    }
                    for (String tag : geneticTags) {
                        if (str.contains(tag)) {
                            sucess = true;
                            startActivity(new Intent(Chatbot.this, Genetic.class));
                        }
                    }
                    for (String tag : sexualDesireTags) {
                        if (str.contains(tag)) {
                            sucess = true;
                            startActivity(new Intent(Chatbot.this, SexualDesire.class));
                        }
                    }
                    for (String tag : entertainmentTags) {
                        if (str.contains(tag)) {
                            sucess = true;
                            startActivity(new Intent(Chatbot.this, Entertainment.class));
                        }
                    }
                    for (String tag : pronounsTags) {
                        if (str.contains(tag)) {
                            sucess = true;
                            startActivity(new Intent(Chatbot.this, Pronouns.class));
                        }
                    }
                    for (String tag : isolationTags) {
                        if (str.contains(tag)) {
                            sucess = true;
                            startActivity(new Intent(Chatbot.this, Isolation.class));
                        }
                    }
                    for (String tag : screamingTags) {
                        if (str.contains(tag)) {
                            sucess = true;
                            startActivity(new Intent(Chatbot.this, Screaming.class));
                        }
                    }
                    for (String tag : tantrumsTags) {
                        if (str.contains(tag)) {
                            sucess = true;
                            startActivity(new Intent(Chatbot.this, Tantrums.class));
                        }
                    }
                    for (String tag : sabotageTags) {
                        if (str.contains(tag)) {
                            sucess = true;
                            startActivity(new Intent(Chatbot.this, Sabotage.class));
                        }
                    }
                    for (String tag : fearTags) {
                        if (str.contains(tag)) {
                            sucess = true;
                            startActivity(new Intent(Chatbot.this, Fear.class));
                        }
                    }
                    for (String tag : selfHarmTags) {
                        if (str.contains(tag)) {
                            sucess = true;
                            startActivity(new Intent(Chatbot.this, SelfHarm.class));
                        }
                    }
                    for (String tag : nutritionTags) {
                        if (str.contains(tag)) {
                            sucess = true;
                            startActivity(new Intent(Chatbot.this, Nutrition.class));
                        }
                    }
                    for (String tag : adaptionTags) {
                        if (str.contains(tag)) {
                            sucess = true;
                            startActivity(new Intent(Chatbot.this, Adaptation.class));
                        }
                    }
                }
                if(sucess == false)
                {
                    Toast.makeText(Chatbot.this,"نتأسف لعدم وجود إجابة لسؤالك ... حاول كتابة السؤال بصيغة أخري",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}

